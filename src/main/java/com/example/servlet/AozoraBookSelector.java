package com.example.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AozoraBookSelector {
    
    // ZORAPI（ゾラピ）のエンドポイント
    private static final String ZORAPI_BASE_URL = "https://api.bungomail.com/v0";
    private static final String BOOKS_ENDPOINT = ZORAPI_BASE_URL + "/books";
    private static final String PERSONS_ENDPOINT = ZORAPI_BASE_URL + "/persons";
    
    // 青空文庫のベースURL（フォールバック用）
    private static final String AOZORA_BASE_URL = "https://www.aozora.gr.jp";
    
    // キャッシュ機能
    private static final ConcurrentHashMap<String, List<Book>> bookCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long> cacheTimestamps = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION = TimeUnit.HOURS.toMillis(24); // 24時間キャッシュ
    
    // 主要な作家の人物ID（ZORAPI用）
    private static final String[] MAJOR_AUTHORS = {
        "148", // 夏目漱石
        "879", // 芥川龍之介
        "35",  // 太宰治
        "119", // 森鴎外
        "125", // 宮沢賢治
        "61",  // 川端康成
        "82",  // 谷崎潤一郎
        "52",  // 志賀直哉
        "31",  // 有島武郎
        "108"  // 武者小路実篤
    };
    
    // 主要な作者の作品一覧ページ（フォールバック用）
    private static final String[] AUTHOR_PAGES = {
        "https://www.aozora.gr.jp/index_pages/person148.html", // 夏目漱石
        "https://www.aozora.gr.jp/index_pages/person879.html", // 芥川龍之介
        "https://www.aozora.gr.jp/index_pages/person9.html",   // 太宰治
        "https://www.aozora.gr.jp/index_pages/person119.html", // 森鴎外
        "https://www.aozora.gr.jp/index_pages/person125.html", // 宮沢賢治
        "https://www.aozora.gr.jp/index_pages/person61.html",  // 川端康成
        "https://www.aozora.gr.jp/index_pages/person82.html",  // 谷崎潤一郎
        "https://www.aozora.gr.jp/index_pages/person52.html",  // 志賀直哉
        "https://www.aozora.gr.jp/index_pages/person31.html",  // 有島武郎
        "https://www.aozora.gr.jp/index_pages/person108.html"  // 武者小路実篤
    };
    
    public static class Book {
        private String id;
        private String title;
        private String author;
        private String authorId;
        private String url;
        private String description;
        private String category;
        private int accessCount;
        private String opening;
        private String dataSource; // "ZORAPI" または "WebScraping"
        
        public Book(String id, String title, String author, String authorId, String url) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.authorId = authorId;
            this.url = url;
        }
        
        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getAuthorId() { return authorId; }
        public String getUrl() { return url; }
        public String getDescription() { return description; }
        public String getCategory() { return category; }
        public int getAccessCount() { return accessCount; }
        public String getOpening() { return opening; }
        public String getDataSource() { return dataSource; }
        
        // Setters
        public void setDescription(String description) { this.description = description; }
        public void setCategory(String category) { this.category = category; }
        public void setAccessCount(int accessCount) { this.accessCount = accessCount; }
        public void setOpening(String opening) { this.opening = opening; }
        public void setDataSource(String dataSource) { this.dataSource = dataSource; }
        
        @Override
        public String toString() {
            return "「" + title + "」 by " + author;
        }
    }
    
    /**
     * 青空文庫からランダムに1冊選ぶ（ZORAPI + フォールバック）
     */
    public static Book selectRandomBook() {
        try {
            List<Book> books = getAllBooks();
            if (books.isEmpty()) {
                return new Book("0", "エラー", "システム", "0", "本の取得に失敗しました");
            }
            
            Random random = new Random();
            int index = random.nextInt(books.size());
            Book selectedBook = books.get(index);
            
            return selectedBook;
        } catch (Exception e) {
            e.printStackTrace();
            return new Book("0", "エラー", "システム", "0", "本の取得中にエラーが発生しました: " + e.getMessage());
        }
    }
    
    /**
     * 特定の作家の作品をランダムに選ぶ
     */
    public static Book selectRandomBookByAuthor(String authorName) {
        try {
            List<Book> books = getBooksByAuthor(authorName);
            if (books.isEmpty()) {
                return new Book("0", "エラー", "システム", "0", "指定された作家の作品が見つかりませんでした");
            }
            
            Random random = new Random();
            int index = random.nextInt(books.size());
            return books.get(index);
        } catch (Exception e) {
            e.printStackTrace();
            return new Book("0", "エラー", "システム", "0", "作家別選択中にエラーが発生しました: " + e.getMessage());
        }
    }
    
    /**
     * 青空文庫の作品リストを取得（ZORAPI + フォールバック）
     */
    private static List<Book> getAllBooks() throws IOException {
        // キャッシュをチェック
        if (isCacheValid()) {
            List<Book> cachedBooks = getCachedBooks();
            if (!cachedBooks.isEmpty()) {
                System.out.println("キャッシュから本のリストを取得しました");
                return cachedBooks;
            }
        }
        
        // キャッシュが無効または空の場合、新しく取得
        List<Book> allBooks = new ArrayList<>();
        
        // まずZORAPIを試行
        try {
            System.out.println("ZORAPIからデータを取得中...");
            allBooks = getAllBooksFromZorapi();
            if (!allBooks.isEmpty()) {
                System.out.println("ZORAPIから " + allBooks.size() + " 冊の本を取得しました");
                // キャッシュに保存
                cacheBooks(allBooks);
                return allBooks;
            }
        } catch (Exception e) {
            System.err.println("ZORAPIでの取得に失敗: " + e.getMessage());
        }
        
        // ZORAPIが失敗した場合、Webスクレイピングにフォールバック
        System.out.println("Webスクレイピングにフォールバック中...");
        allBooks = getAllBooksFromWebScraping();
        
        // キャッシュに保存
        if (!allBooks.isEmpty()) {
            cacheBooks(allBooks);
        }
        
        return allBooks;
    }
    
    /**
     * ZORAPIから作品リストを取得
     */
    private static List<Book> getAllBooksFromZorapi() throws IOException {
        List<Book> allBooks = new ArrayList<>();
        
        // 主要な作家の作品を取得
        for (String authorId : MAJOR_AUTHORS) {
            try {
                List<Book> authorBooks = getBooksByAuthorId(authorId);
                allBooks.addAll(authorBooks);
            } catch (Exception e) {
                System.err.println("作家ID " + authorId + " の作品取得に失敗: " + e.getMessage());
            }
        }
        
        return allBooks;
    }
    
    /**
     * Webスクレイピングから作品リストを取得（フォールバック）
     */
    private static List<Book> getAllBooksFromWebScraping() throws IOException {
        List<Book> allBooks = new ArrayList<>();
        
        // 主要な作者の作品を取得
        for (String authorPage : AUTHOR_PAGES) {
            try {
                List<Book> authorBooks = getBooksFromAuthorPage(authorPage);
                allBooks.addAll(authorBooks);
            } catch (Exception e) {
                System.err.println("作者ページの取得に失敗: " + authorPage + " - " + e.getMessage());
            }
        }
        
        return allBooks;
    }
    
    /**
     * 作家IDで作品を取得（ZORAPI）
     */
    private static List<Book> getBooksByAuthorId(String authorId) throws IOException {
        List<Book> books = new ArrayList<>();
        String url = BOOKS_ENDPOINT + "?人物ID=" + authorId + "&limit=50";
        
        String jsonResponse = fetchApiResponse(url);
        books = parseBooksFromJson(jsonResponse);
        
        // データソースを設定
        for (Book book : books) {
            book.setDataSource("ZORAPI");
        }
        
        return books;
    }
    
    /**
     * 作家名で作品を取得（ZORAPI）
     */
    private static List<Book> getBooksByAuthor(String authorName) throws IOException {
        List<Book> books = new ArrayList<>();
        String url = BOOKS_ENDPOINT + "?姓名=" + authorName + "&limit=50";
        
        String jsonResponse = fetchApiResponse(url);
        books = parseBooksFromJson(jsonResponse);
        
        // データソースを設定
        for (Book book : books) {
            book.setDataSource("ZORAPI");
        }
        
        return books;
    }
    
    /**
     * 特定の作者の作品一覧を取得（Webスクレイピング）
     */
    private static List<Book> getBooksFromAuthorPage(String authorPageUrl) throws IOException {
        List<Book> books = new ArrayList<>();
        String html = fetchWebPage(authorPageUrl);
        
        // 作者名を取得
        String authorName = extractAuthorName(html);
        if (authorName == null) {
            authorName = extractAuthorNameFromUrl(authorPageUrl);
        }
        
        // 作品リンクを抽出
        Pattern pattern = Pattern.compile("<a href=\"([^\"]*)\"[^>]*>([^<]+)</a>");
        Matcher matcher = pattern.matcher(html);
        
        while (matcher.find()) {
            String url = matcher.group(1);
            String title = matcher.group(2).trim();
            
            // 作品ページのURLのみを対象とする
            if (url.contains("/cards/") && url.contains("/files/") && !title.isEmpty()) {
                // タイトルから不要な文字を除去
                title = cleanTitle(title);
                if (!title.isEmpty()) {
                    Book book = new Book("web-" + System.currentTimeMillis(), title, authorName, "web", AOZORA_BASE_URL + url);
                    book.setDataSource("WebScraping");
                    book.setCategory("不明");
                    book.setAccessCount(0);
                    books.add(book);
                }
            }
        }
        
        return books;
    }
    
    /**
     * HTMLから作者名を抽出
     */
    private static String extractAuthorName(String html) {
        Pattern pattern = Pattern.compile("<title>([^<]+)</title>");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            String title = matcher.group(1);
            // タイトルから作者名を抽出
            if (title.contains("青空文庫")) {
                String authorName = title.replace("青空文庫", "").replace("作品一覧", "").trim();
                return authorName;
            }
        }
        return null;
    }
    
    /**
     * URLから作者名を抽出
     */
    private static String extractAuthorNameFromUrl(String url) {
        Pattern pattern = Pattern.compile("/person(\\d+)\\.html");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String authorId = matcher.group(1);
            return convertAuthorIdToName(authorId);
        }
        return "不明";
    }
    
    /**
     * 作者IDを作者名に変換
     */
    private static String convertAuthorIdToName(String authorId) {
        switch (authorId) {
            case "148": return "夏目漱石";
            case "879": return "芥川龍之介";
            case "9": return "太宰治";
            case "119": return "森鴎外";
            case "125": return "宮沢賢治";
            case "61": return "川端康成";
            case "82": return "谷崎潤一郎";
            case "52": return "志賀直哉";
            case "31": return "有島武郎";
            case "108": return "武者小路実篤";
            default: return "作者ID: " + authorId;
        }
    }
    
    /**
     * タイトルをクリーンアップ
     */
    private static String cleanTitle(String title) {
        // 不要な文字や記号を除去
        title = title.replaceAll("\\[.*?\\]", ""); // 角括弧内を除去
        title = title.replaceAll("【.*?】", "");   // 全角角括弧内を除去
        title = title.replaceAll("（.*?）", "");   // 全角括弧内を除去
        title = title.replaceAll("\\(.*?\\)", ""); // 半角括弧内を除去
        title = title.trim();
        
        // 空文字列や短すぎるタイトルを除外
        if (title.length() < 2) {
            return "";
        }
        
        return title;
    }
    
    /**
     * JSONレスポンスからBookオブジェクトのリストを作成（簡易版）
     */
    private static List<Book> parseBooksFromJson(String jsonResponse) {
        List<Book> books = new ArrayList<>();
        
        try {
            // 簡易的なJSON解析（正規表現を使用）
            Pattern bookPattern = Pattern.compile("\"作品ID\":\\s*\"([^\"]+)\".*?\"作品名\":\\s*\"([^\"]+)\".*?\"姓名\":\\s*\"([^\"]+)\".*?\"人物ID\":\\s*\"([^\"]+)\".*?\"図書カードURL\":\\s*\"([^\"]+)\".*?\"カテゴリ\":\\s*\"([^\"]+)\".*?\"書き出し\":\\s*\"([^\"]+)\".*?\"累計アクセス数\":\\s*(\\d+)", Pattern.DOTALL);
            Matcher matcher = bookPattern.matcher(jsonResponse);
            
            while (matcher.find()) {
                String id = matcher.group(1);
                String title = matcher.group(2);
                String author = matcher.group(3);
                String authorId = matcher.group(4);
                String cardUrl = matcher.group(5);
                String category = matcher.group(6);
                String opening = matcher.group(7);
                int accessCount = Integer.parseInt(matcher.group(8));
                
                // 必須フィールドのチェック
                if (!id.isEmpty() && !title.isEmpty() && !author.isEmpty()) {
                    Book book = new Book(id, title, author, authorId, cardUrl);
                    book.setCategory(category);
                    book.setOpening(opening);
                    book.setAccessCount(accessCount);
                    
                    // 書き出しを説明として使用
                    if (!opening.isEmpty()) {
                        String description = opening;
                        if (description.length() > 200) {
                            description = description.substring(0, 200) + "...";
                        }
                        book.setDescription(description);
                    }
                    
                    books.add(book);
                }
            }
        } catch (Exception e) {
            System.err.println("JSON解析エラー: " + e.getMessage());
        }
        
        return books;
    }
    
    /**
     * ZORAPIからレスポンスを取得
     */
    private static String fetchApiResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "AozoraBookSelector/1.0");
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(10000); // 10秒
        connection.setReadTimeout(10000);    // 10秒
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        return content.toString();
    }
    
    /**
     * 指定されたURLからWebページの内容を取得
     */
    private static String fetchWebPage(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        connection.setConnectTimeout(10000); // 10秒
        connection.setReadTimeout(10000);    // 10秒
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        return content.toString();
    }
    
    /**
     * キャッシュが有効かチェック
     */
    private static boolean isCacheValid() {
        Long timestamp = cacheTimestamps.get("books");
        if (timestamp == null) {
            return false;
        }
        return System.currentTimeMillis() - timestamp < CACHE_DURATION;
    }
    
    /**
     * キャッシュから本のリストを取得
     */
    private static List<Book> getCachedBooks() {
        return bookCache.getOrDefault("books", new ArrayList<>());
    }
    
    /**
     * 本のリストをキャッシュに保存
     */
    private static void cacheBooks(List<Book> books) {
        bookCache.put("books", books);
        cacheTimestamps.put("books", System.currentTimeMillis());
        System.out.println("本のリストをキャッシュに保存しました（" + books.size() + "冊）");
    }
    
    /**
     * キャッシュをクリア
     */
    public static void clearCache() {
        bookCache.clear();
        cacheTimestamps.clear();
        System.out.println("キャッシュをクリアしました");
    }
    
    /**
     * キャッシュの状態を表示
     */
    public static void showCacheStatus() {
        System.out.println("=== キャッシュ状態 ===");
        System.out.println("キャッシュされた本の数: " + getCachedBooks().size());
        System.out.println("キャッシュの有効期限: " + (isCacheValid() ? "有効" : "無効"));
        
        Long timestamp = cacheTimestamps.get("books");
        if (timestamp != null) {
            long age = System.currentTimeMillis() - timestamp;
            System.out.println("キャッシュの年齢: " + (age / 1000 / 60) + "分");
        }
    }
    
    /**
     * 人気作品を取得（アクセス数順）
     */
    public static List<Book> getPopularBooks(int limit) throws IOException {
        List<Book> allBooks = getAllBooks();
        
        // アクセス数でソート
        allBooks.sort((b1, b2) -> Integer.compare(b2.getAccessCount(), b1.getAccessCount()));
        
        // 上位limit件を返す
        return allBooks.subList(0, Math.min(limit, allBooks.size()));
    }
    
    /**
     * テスト用メインメソッド
     */
    public static void main(String[] args) {
        System.out.println("=== 青空文庫ランダム選書（ZORAPI + フォールバック） ===");
        
        // キャッシュ状態を表示
        showCacheStatus();
        
        // ランダム選書
        System.out.println("\n--- ランダム選書 ---");
        Book selectedBook = selectRandomBook();
        System.out.println("選ばれた本: " + selectedBook);
        System.out.println("データソース: " + selectedBook.getDataSource());
        System.out.println("カテゴリ: " + selectedBook.getCategory());
        System.out.println("アクセス数: " + selectedBook.getAccessCount());
        if (selectedBook.getDescription() != null) {
            System.out.println("書き出し: " + selectedBook.getDescription());
        }
        
        // 作家別選書
        System.out.println("\n--- 太宰治の作品から選書 ---");
        Book dazaiBook = selectRandomBookByAuthor("太宰治");
        System.out.println("選ばれた本: " + dazaiBook);
        
        // 再度キャッシュ状態を表示
        showCacheStatus();
    }
} 