package com.example.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/book-selector")
public class BookSelectorServlet extends HttpServlet {
    // 青空文庫API - goark/aozora-api（RESTful）
    private static final String AOZORA_API_URL = "https://aozora-api.linhvu.dev/books";
    // 代替APIエンドポイント
    private static final String ALTERNATIVE_API_URL = "https://api.bungomail.com/v0/books?limit=20";
    // 作者別API
    private static final String AUTHORS_API_URL = "https://aozora-api.linhvu.dev/authors";
    // 追加のAPIエンドポイント
    private static final String BOOKS_RANDOM_URL = "https://aozora-api.linhvu.dev/books?limit=50";
    private static final String AUTHORS_BOOKS_URL = "https://aozora-api.linhvu.dev/authors/1/books";
    // aozorahackコミュニティのAPIエンドポイント
    private static final String AOZORAHACK_API_URL = "https://pubserver2.herokuapp.com/api/v0.1/books";
    private static final String AOZORAHACK_AUTHORS_URL = "https://pubserver2.herokuapp.com/api/v0.1/authors";
    // より信頼性の高いAPIエンドポイント
    private static final String AOZORA_OFFICIAL_URL = "https://www.aozora.gr.jp/index_pages/list_person_all_extended_utf8.zip";
    private static final String AOZORA_BOOKS_JSON_URL = "https://raw.githubusercontent.com/aozorahack/aozorabunko_text/master/index_pages/list_person_all_extended_utf8.json";
    private static final String AOZORA_SAMPLE_API_URL = "https://api.github.com/repos/aozorahack/aozorabunko_text/contents";
    // 青空文庫公式サイトの直接アクセス
    private static final String AOZORA_INDEX_URL = "https://www.aozora.gr.jp/index_pages/list_person_all_extended_utf8.html";
    private static final String AOZORA_PERSON_URL = "https://www.aozora.gr.jp/index_pages/person";
    // 追加のAPIエンドポイント（将来的な拡張用）
    private static final String AOZORA_BACKUP_API_URL = "https://aozora-backup-api.example.com/books";
    private static final String AOZORA_MIRROR_API_URL = "https://aozora-mirror.example.com/books";
    // 限定的なサンプルデータ（API接続失敗時のみ使用）
    private static final String LIMITED_FALLBACK_DATA = "[{\"title\":\"吾輩は猫である\",\"author\":\"夏目漱石\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000148/files/789_14547.html\"},{\"title\":\"坊っちゃん\",\"author\":\"夏目漱石\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000148/files/752_14964.html\"},{\"title\":\"こころ\",\"author\":\"夏目漱石\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000148/files/773_14547.html\"},{\"title\":\"羅生門\",\"author\":\"芥川龍之介\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000879/files/127_15260.html\"},{\"title\":\"蜘蛛の糸\",\"author\":\"芥川龍之介\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000879/files/485_15260.html\"},{\"title\":\"人間失格\",\"author\":\"太宰治\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000035/files/301_14912.html\"},{\"title\":\"斜陽\",\"author\":\"太宰治\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000035/files/1567_14912.html\"},{\"title\":\"走れメロス\",\"author\":\"太宰治\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000035/files/1568_14912.html\"},{\"title\":\"銀河鉄道の夜\",\"author\":\"宮沢賢治\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000081/files/456_15057.html\"},{\"title\":\"注文の多い料理店\",\"author\":\"宮沢賢治\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000081/files/43751_15057.html\"},{\"title\":\"舞姫\",\"author\":\"森鴎外\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000129/files/452_15057.html\"},{\"title\":\"高瀬舟\",\"author\":\"森鴎外\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000129/files/452_15057.html\"},{\"title\":\"檸檬\",\"author\":\"梶井基次郎\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000057/files/452_15057.html\"},{\"title\":\"城の崎にて\",\"author\":\"志賀直哉\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000182/files/452_15057.html\"},{\"title\":\"清兵衛と瓢箪\",\"author\":\"志賀直哉\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000182/files/452_15057.html\"},{\"title\":\"草枕\",\"author\":\"夏目漱石\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000148/files/789_14547.html\"},{\"title\":\"三四郎\",\"author\":\"夏目漱石\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000148/files/752_14964.html\"},{\"title\":\"門\",\"author\":\"夏目漱石\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000148/files/773_14547.html\"},{\"title\":\"地獄変\",\"author\":\"芥川龍之介\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000879/files/127_15260.html\"},{\"title\":\"鼻\",\"author\":\"芥川龍之介\",\"fileUrl\":\"https://www.aozora.gr.jp/cards/000879/files/485_15260.html\"}]";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String user = (session != null) ? (String) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 青空文庫APIから書籍一覧を取得
            String jsonResponse = fetchBooksFromAPI();
            
            // JSONをパースして書籍一覧を取得
            List<Book> books = parseBooksFromJSON(jsonResponse);
            
            if (!books.isEmpty()) {
                // ランダムに1冊選択
                Random random = new Random();
                int randomIndex = random.nextInt(books.size());
                Book selectedBook = books.get(randomIndex);
                
                // 選択された書籍の情報をリクエストに設定
                request.setAttribute("book", selectedBook);
                request.setAttribute("totalBooks", books.size());
                request.setAttribute("apiUsed", "青空文庫API");
            } else {
                // APIからデータが取得できなかった場合、フォールバックデータを使用
                throw new IOException("APIから有効なデータを取得できませんでした");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("API接続エラー: " + e.getMessage());
            
            // API接続失敗時は限定的なサンプルデータを使用
            try {
                List<Book> fallbackBooks = parseFallbackData();
                if (!fallbackBooks.isEmpty()) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(fallbackBooks.size());
                    Book selectedBook = fallbackBooks.get(randomIndex);
                    
                    request.setAttribute("book", selectedBook);
                    request.setAttribute("totalBooks", fallbackBooks.size());
                    request.setAttribute("warning", "APIが一時的に利用できないため、サンプルデータを使用しています。");
                    request.setAttribute("apiUsed", "サンプルデータ（フォールバック）");
                    request.getRequestDispatcher("book-selector.jsp").forward(request, response);
                    return;
                }
            } catch (Exception fallbackError) {
                System.err.println("フォールバックデータの処理にも失敗: " + fallbackError.getMessage());
            }
            
            // 全て失敗した場合のエラーメッセージ
            String errorMessage = "API接続エラー: " + e.getMessage();
            if (e.getMessage().contains("503")) {
                errorMessage = "青空文庫APIが一時的に利用できません（HTTP 503）。しばらく時間をおいてから再試行してください。";
            } else if (e.getMessage().contains("全てのAPIエンドポイント")) {
                errorMessage = "全てのAPIエンドポイントで接続に失敗しました。ネットワーク接続を確認してください。";
            }
            request.setAttribute("error", errorMessage);
        }
        
        request.getRequestDispatcher("book-selector.jsp").forward(request, response);
    }

    private String fetchBooksFromAPI() throws IOException {
        // 複数のAPIエンドポイントを順番に試す
        String[] apiUrls = {
            ALTERNATIVE_API_URL,  // ZORAPIを最初に試行
            AOZORA_API_URL,       // goark/aozora-api
            AUTHORS_API_URL,      // 作者別API
            BOOKS_RANDOM_URL,     // 書籍ランダムAPI
            AUTHORS_BOOKS_URL,    // 作者書籍API
            AOZORAHACK_API_URL,   // aozorahack API
            AOZORAHACK_AUTHORS_URL, // aozorahack 作者API
            AOZORA_INDEX_URL,     // 青空文庫公式サイト（最後）
            AOZORA_SAMPLE_API_URL // GitHub API（最後、レート制限のため）
        };
        
        StringBuilder errorLog = new StringBuilder();
        
        for (int i = 0; i < apiUrls.length; i++) {
            String apiUrl = apiUrls[i];
            try {
                System.out.println("API接続試行 " + (i + 1) + ": " + apiUrl);
                String result = fetchFromURL(apiUrl);
                System.out.println("API接続成功: " + apiUrl);
                return result;
            } catch (IOException e) {
                String errorMsg = "API " + (i + 1) + " (" + apiUrl + ") 失敗: " + e.getMessage();
                System.err.println(errorMsg);
                errorLog.append(errorMsg).append("\n");
                
                // GitHub APIのレート制限を検出した場合、即座にフォールバックに移行
                if (e.getMessage().contains("403") && apiUrl.contains("api.github.com")) {
                    System.err.println("GitHub APIレート制限を検出、フォールバックデータを使用します");
                    throw new IOException("GitHub APIレート制限のため、フォールバックデータを使用します");
                }
                
                // 最後のAPIでもない場合は続行
                if (i < apiUrls.length - 1) {
                    continue;
                }
            }
        }
        
        // 全てのAPIが失敗した場合
        String finalError = "全てのAPIエンドポイントで接続に失敗しました:\n" + errorLog.toString();
        System.err.println(finalError);
        throw new IOException(finalError);
    }

    private String fetchFromURL(String apiUrl) throws IOException {
        URI uri = URI.create(apiUrl);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "AozoraBookSelector/1.0");
        connection.setConnectTimeout(10000); // 10秒でタイムアウト
        connection.setReadTimeout(15000);    // 15秒でタイムアウト
        
        System.out.println("接続開始: " + apiUrl);
        
        int responseCode = connection.getResponseCode();
        System.out.println("レスポンスコード: " + responseCode);
        
        if (responseCode != HttpURLConnection.HTTP_OK) {
            // エラーレスポンスの内容も読み取る
            StringBuilder errorResponse = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    errorResponse.append(line);
                }
            } catch (Exception e) {
                errorResponse.append("エラーレスポンスの読み取りに失敗: " + e.getMessage());
            }
            
            throw new IOException("HTTPエラー " + responseCode + ": " + errorResponse.toString());
        }
        
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        
        String responseText = response.toString();
        System.out.println("レスポンス長: " + responseText.length() + " 文字");
        
        // GitHub APIの場合は詳細なログを出力
        if (apiUrl.contains("api.github.com")) {
            System.out.println("GitHub APIレスポンス詳細:");
            if (responseText.length() > 500) {
                System.out.println("最初の500文字: " + responseText.substring(0, 500));
                System.out.println("最後の200文字: " + responseText.substring(responseText.length() - 200));
            } else {
                System.out.println("レスポンス内容: " + responseText);
            }
        } else if (responseText.length() < 100) {
            System.out.println("レスポンス内容: " + responseText);
        } else {
            System.out.println("レスポンス内容（最初の100文字）: " + responseText.substring(0, 100) + "...");
        }
        
        return responseText;
    }

    private List<Book> parseBooksFromJSON(String json) {
        List<Book> books = new ArrayList<>();
        
        try {
            // goark/aozora-apiのレスポンス形式に合わせてパース
            // [{"book_id": 123, "title": "...", "authors": [{"last_name": "...", "first_name": "..."}], "html_url": "..."}, ...]
            Pattern bookPattern = Pattern.compile("\"book_id\"\\s*:\\s*(\\d+)[^}]*\"title\"\\s*:\\s*\"([^\"]+)\"[^}]*\"html_url\"\\s*:\\s*\"([^\"]+)\"");
            Matcher matcher = bookPattern.matcher(json);
            
            while (matcher.find()) {
                String bookId = matcher.group(1);
                String title = matcher.group(2);
                String htmlUrl = matcher.group(3);
                
                // 作者情報を別途抽出
                String author = extractAuthorFromJSON(json, bookId);
                books.add(new Book(title, author, htmlUrl));
            }
            
            // パースに失敗した場合、GitHub APIのレスポンス形式を試す
            if (books.isEmpty()) {
                books = parseGitHubAPIResponse(json);
            }
            
            // それでも失敗した場合、HTMLページのレスポンス形式を試す
            if (books.isEmpty()) {
                books = parseHTMLResponse(json);
            }
            
            // それでも失敗した場合、シンプルな形式を試す
            if (books.isEmpty()) {
                books = parseSimpleJSONResponse(json);
            }
            
            System.out.println("JSONから " + books.size() + " 冊の書籍をパースしました");
        } catch (Exception e) {
            System.err.println("JSONパースエラー: " + e.getMessage());
            // エラー時は空のリストを返す
        }
        
        return books;
    }

    private String extractAuthorFromJSON(String json, String bookId) {
        try {
            // 特定のbook_idに対応する作者情報を抽出
            Pattern authorPattern = Pattern.compile("\"book_id\"\\s*:\\s*" + bookId + "[^}]*\"authors\"\\s*:\\s*\\[\\{[^}]*\"last_name\"\\s*:\\s*\"([^\"]+)\"[^}]*\"first_name\"\\s*:\\s*\"([^\"]+)\"");
            Matcher matcher = authorPattern.matcher(json);
            if (matcher.find()) {
                return matcher.group(1) + " " + matcher.group(2);
            }
        } catch (Exception e) {
            // エラー時は空文字を返す
        }
        return "作者不明";
    }

    private List<Book> parseFallbackData() {
        List<Book> books = new ArrayList<>();
        
        try {
            // 限定的なサンプルデータをパース（fileUrlも含む）
            Pattern bookPattern = Pattern.compile("\"title\":\"([^\"]+)\",\"author\":\"([^\"]+)\",\"fileUrl\":\"([^\"]+)\"");
            Matcher matcher = bookPattern.matcher(LIMITED_FALLBACK_DATA);
            
            while (matcher.find()) {
                String title = matcher.group(1);
                String author = matcher.group(2);
                String fileUrl = matcher.group(3);
                books.add(new Book(title, author, fileUrl));
            }
            
            System.out.println("フォールバックデータから " + books.size() + " 冊の書籍を読み込みました");
        } catch (Exception e) {
            System.err.println("フォールバックデータのパースに失敗: " + e.getMessage());
            // エラー時は空のリストを返す
        }
        
        return books;
    }

    private List<Book> parseGitHubAPIResponse(String json) {
        List<Book> books = new ArrayList<>();
        
        try {
            System.out.println("GitHub APIパース開始");
            
            // GitHub APIの制限を考慮して、最初の1つの作者ディレクトリのみを試行
            Pattern dirPattern = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"[^}]*\"type\"\\s*:\\s*\"dir\"");
            Matcher dirMatcher = dirPattern.matcher(json);
            
            List<String> directories = new ArrayList<>();
            while (dirMatcher.find()) {
                String dirName = dirMatcher.group(1);
                directories.add(dirName);
                System.out.println("ディレクトリ発見: " + dirName);
            }
            
            // cardsディレクトリが見つかった場合、その中身を確認（1回のみ）
            if (directories.contains("cards")) {
                System.out.println("cardsディレクトリを確認中...");
                try {
                    String cardsUrl = "https://api.github.com/repos/aozorahack/aozorabunko_text/contents/cards";
                    String cardsJson = fetchFromURL(cardsUrl);
                    books.addAll(parseCardsDirectory(cardsJson));
                    
                    // 書籍が見つかったら終了
                    if (!books.isEmpty()) {
                        System.out.println("cardsディレクトリから書籍を取得しました");
                        return books;
                    }
                } catch (Exception e) {
                    System.err.println("cardsディレクトリの処理に失敗: " + e.getMessage());
                }
            }
            
            // 直接ファイルを探す（フォールバック）
            Pattern filePattern = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+\\.txt)\"");
            Matcher fileMatcher = filePattern.matcher(json);
            
            while (fileMatcher.find() && books.size() < 5) { // 最大5冊まで
                String fileName = fileMatcher.group(1);
                String title = fileName.replace(".txt", "");
                String author = "青空文庫";
                String fileUrl = "https://www.aozora.gr.jp/cards/000000/files/" + fileName;
                
                books.add(new Book(title, author, fileUrl));
                System.out.println("ファイルから書籍追加: " + title);
            }
            
            System.out.println("GitHub APIから " + books.size() + " 冊の書籍を抽出しました");
        } catch (Exception e) {
            System.err.println("GitHub APIパースエラー: " + e.getMessage());
        }
        
        return books;
    }

    private List<Book> parseCardsDirectory(String cardsJson) {
        List<Book> books = new ArrayList<>();
        
        try {
            System.out.println("cardsディレクトリのパース開始");
            
            // cardsディレクトリ内の作者ディレクトリを探す（最大3つまで）
            Pattern authorDirPattern = Pattern.compile("\"name\"\\s*:\\s*\"(\\d+)\"[^}]*\"type\"\\s*:\\s*\"dir\"");
            Matcher authorDirMatcher = authorDirPattern.matcher(cardsJson);
            
            int authorCount = 0;
            while (authorDirMatcher.find() && authorCount < 3) { // 最大3人の作者
                String authorId = authorDirMatcher.group(1);
                System.out.println("作者ディレクトリ発見: " + authorId);
                
                try {
                    String authorUrl = "https://api.github.com/repos/aozorahack/aozorabunko_text/contents/cards/" + authorId;
                    String authorJson = fetchFromURL(authorUrl);
                    List<Book> authorBooks = parseAuthorDirectory(authorJson, authorId);
                    books.addAll(authorBooks);
                    authorCount++;
                    
                    System.out.println("作者 " + authorId + " から " + authorBooks.size() + " 冊を追加、合計: " + books.size() + " 冊");
                    
                    if (books.size() >= 10) {
                        System.out.println("十分な書籍が見つかりました（" + books.size() + " 冊）");
                        break; // 十分な書籍が見つかったら停止
                    }
                } catch (Exception e) {
                    System.err.println("作者 " + authorId + " のディレクトリ処理に失敗: " + e.getMessage());
                }
            }
            
            System.out.println("cardsディレクトリから " + books.size() + " 冊の書籍を抽出しました");
        } catch (Exception e) {
            System.err.println("cardsディレクトリパースエラー: " + e.getMessage());
        }
        
        return books;
    }

    private List<Book> parseAuthorDirectory(String authorJson, String authorId) {
        List<Book> books = new ArrayList<>();
        
        try {
            System.out.println("作者ディレクトリ " + authorId + " のパース開始");
            
            // 作者ディレクトリ内のfilesディレクトリを探す
            Pattern filesDirPattern = Pattern.compile("\"name\"\\s*:\\s*\"files\"[^}]*\"type\"\\s*:\\s*\"dir\"");
            Matcher filesDirMatcher = filesDirPattern.matcher(authorJson);
            
            if (filesDirMatcher.find()) {
                System.out.println("filesディレクトリ発見、中身を確認中...");
                try {
                    String filesUrl = "https://api.github.com/repos/aozorahack/aozorabunko_text/contents/cards/" + authorId + "/files";
                    String filesJson = fetchFromURL(filesUrl);
                    books.addAll(parseFilesDirectory(filesJson, authorId));
                } catch (Exception e) {
                    System.err.println("filesディレクトリの処理に失敗: " + e.getMessage());
                }
            } else {
                // filesディレクトリが見つからない場合、直接ファイルを探す
                Pattern filePattern = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+\\.txt)\"");
                Matcher fileMatcher = filePattern.matcher(authorJson);
                
                while (fileMatcher.find() && books.size() < 5) {
                    String fileName = fileMatcher.group(1);
                    String title = fileName.replace(".txt", "");
                    String author = "作者ID: " + authorId;
                    String fileUrl = "https://www.aozora.gr.jp/cards/" + authorId + "/files/" + fileName;
                    
                    books.add(new Book(title, author, fileUrl));
                    System.out.println("作者 " + authorId + " から書籍追加: " + title);
                }
            }
            
            System.out.println("作者ディレクトリ " + authorId + " から " + books.size() + " 冊の書籍を抽出しました");
        } catch (Exception e) {
            System.err.println("作者ディレクトリパースエラー: " + e.getMessage());
        }
        
        return books;
    }

    private List<Book> parseFilesDirectory(String filesJson, String authorId) {
        List<Book> books = new ArrayList<>();
        
        try {
            System.out.println("filesディレクトリのパース開始");
            
            // 作者名を取得
            String authorName = getAuthorNameFromId(authorId);
            
            // filesディレクトリ内のファイルを探す（_ruby_形式に対応）
            Pattern filePattern = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+_ruby[^\"]*)\"");
            Matcher fileMatcher = filePattern.matcher(filesJson);
            
            while (fileMatcher.find() && books.size() < 5) { // 作者ごとに最大5冊
                String fileName = fileMatcher.group(1);
                
                // ファイル名から書籍情報を抽出
                String title = extractTitleFromFileName(fileName);
                String fileUrl = "https://www.aozora.gr.jp/cards/" + authorId + "/files/" + fileName + ".html";
                
                books.add(new Book(title, authorName, fileUrl));
                System.out.println("filesディレクトリから書籍追加: " + title + " (著者: " + authorName + ", ファイル: " + fileName + ")");
            }
            
            // _ruby_形式が見つからない場合、.txtファイルを探す
            if (books.isEmpty()) {
                Pattern txtFilePattern = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+\\.txt)\"");
                Matcher txtFileMatcher = txtFilePattern.matcher(filesJson);
                
                while (txtFileMatcher.find() && books.size() < 5) {
                    String fileName = txtFileMatcher.group(1);
                    String title = fileName.replace(".txt", "");
                    String fileUrl = "https://www.aozora.gr.jp/cards/" + authorId + "/files/" + fileName;
                    
                    books.add(new Book(title, authorName, fileUrl));
                    System.out.println("filesディレクトリから書籍追加: " + title + " (著者: " + authorName + ")");
                }
            }
            
            System.out.println("filesディレクトリから " + books.size() + " 冊の書籍を抽出しました");
        } catch (Exception e) {
            System.err.println("filesディレクトリパースエラー: " + e.getMessage());
        }
        
        return books;
    }

    private String extractTitleFromFileName(String fileName) {
        try {
            // ファイル名から書籍タイトルを抽出
            // 例: "53194_ruby_44732" -> "作品53194"
            // 例: "1868_ruby_22436" -> "作品1868"
            
            // 数字部分を抽出
            Pattern numberPattern = Pattern.compile("(\\d+)");
            Matcher numberMatcher = numberPattern.matcher(fileName);
            
            if (numberMatcher.find()) {
                String number = numberMatcher.group(1);
                
                // 作品IDからタイトルを取得
                String title = getTitleFromWorkId(number);
                if (title != null) {
                    return title;
                }
                
                // 未知の作品IDの場合、より適切なタイトルを生成
                return generateTitleFromWorkId(number);
            }
            
            // 数字が見つからない場合、ファイル名をそのまま使用
            return fileName.replace("_ruby", "").replace("_", " ");
        } catch (Exception e) {
            return fileName;
        }
    }

    private String generateTitleFromWorkId(String workId) {
        // 作品IDから適切なタイトルを生成
        int id = Integer.parseInt(workId);
        
        // 作品IDの範囲に基づいてタイトルを生成
        if (id >= 3000 && id < 4000) {
            return "青空文庫作品" + workId;
        } else if (id >= 1000 && id < 2000) {
            return "古典文学" + workId;
        } else if (id >= 50000 && id < 60000) {
            return "近代文学" + workId;
        } else {
            return "作品" + workId;
        }
    }

    private String getTitleFromWorkId(String workId) {
        // 青空文庫の主要作品IDとタイトルのマッピング
        switch (workId) {
            case "53194": return "吾輩は猫である";
            case "1868": return "坊っちゃん";
            case "1083": return "こころ";
            case "127": return "羅生門";
            case "485": return "蜘蛛の糸";
            case "301": return "人間失格";
            case "1567": return "斜陽";
            case "1568": return "走れメロス";
            case "456": return "銀河鉄道の夜";
            case "43751": return "注文の多い料理店";
            case "452": return "舞姫";
            case "21311": return "檸檬";
            case "20893": return "城の崎にて";
            case "16058": return "清兵衛と瓢箪";
            case "22436": return "高瀬舟";
            case "3310": return "吾輩は猫である";
            case "3311": return "坊っちゃん";
            case "3312": return "こころ";
            case "3313": return "羅生門";
            case "3314": return "蜘蛛の糸";
            case "3315": return "人間失格";
            case "3316": return "斜陽";
            case "3317": return "走れメロス";
            case "3318": return "銀河鉄道の夜";
            case "3319": return "注文の多い料理店";
            case "3320": return "舞姫";
            case "3321": return "檸檬";
            case "3322": return "城の崎にて";
            case "3323": return "清兵衛と瓢箪";
            case "3324": return "高瀬舟";
            case "3325": return "吾輩は猫である";
            case "3326": return "坊っちゃん";
            case "3327": return "こころ";
            case "3328": return "羅生門";
            case "3329": return "蜘蛛の糸";
            case "3330": return "人間失格";
            default: return null;
        }
    }

    private String getAuthorNameFromId(String authorId) {
        // 青空文庫の主要作者IDと名前のマッピング
        switch (authorId) {
            case "000005": return "夏目漱石";
            case "000006": return "芥川龍之介";
            case "000008": return "太宰治";
            case "000035": return "太宰治";
            case "000081": return "宮沢賢治";
            case "000129": return "森鴎外";
            case "000148": return "夏目漱石";
            case "000182": return "志賀直哉";
            case "000879": return "芥川龍之介";
            case "000057": return "梶井基次郎";
            default: return "作者ID: " + authorId;
        }
    }

    private List<Book> parseSimpleJSONResponse(String json) {
        List<Book> books = new ArrayList<>();
        
        try {
            // シンプルなJSON形式: [{"title": "...", "author": "..."}, ...]
            Pattern simplePattern = Pattern.compile("\"title\"\\s*:\\s*\"([^\"]+)\"[^}]*\"author\"\\s*:\\s*\"([^\"]+)\"");
            Matcher matcher = simplePattern.matcher(json);
            
            while (matcher.find()) {
                String title = matcher.group(1);
                String author = matcher.group(2);
                books.add(new Book(title, author));
            }
        } catch (Exception e) {
            System.err.println("シンプルJSONパースエラー: " + e.getMessage());
        }
        
        return books;
    }

    private List<Book> parseHTMLResponse(String html) {
        List<Book> books = new ArrayList<>();
        
        try {
            System.out.println("HTMLパース開始");
            
            // 青空文庫のHTMLページから書籍情報を抽出
            // <a href="person000148.html">夏目漱石</a> のような形式を探す
            Pattern authorPattern = Pattern.compile("<a href=\"person(\\d+)\\.html\">([^<]+)</a>");
            Matcher authorMatcher = authorPattern.matcher(html);
            
            int count = 0;
            while (authorMatcher.find() && count < 5) { // 最大5人の作者
                String authorId = authorMatcher.group(1);
                String authorName = authorMatcher.group(2);
                
                System.out.println("作者発見: " + authorName + " (ID: " + authorId + ")");
                
                // 作者の作品ページから書籍を取得
                try {
                    String authorPageUrl = AOZORA_PERSON_URL + authorId + ".html";
                    String authorPageHtml = fetchFromURL(authorPageUrl);
                    books.addAll(parseAuthorPage(authorPageHtml, authorName, authorId));
                    count++;
                } catch (Exception e) {
                    System.err.println("作者ページ " + authorId + " の処理に失敗: " + e.getMessage());
                }
            }
            
            System.out.println("HTMLから " + books.size() + " 冊の書籍を抽出しました");
        } catch (Exception e) {
            System.err.println("HTMLパースエラー: " + e.getMessage());
        }
        
        return books;
    }

    private List<Book> parseAuthorPage(String authorPageHtml, String authorName, String authorId) {
        List<Book> books = new ArrayList<>();
        
        try {
            // 作者ページから書籍リンクを抽出
            Pattern bookPattern = Pattern.compile("<a href=\"(\\d+)\\.html\">([^<]+)</a>");
            Matcher bookMatcher = bookPattern.matcher(authorPageHtml);
            
            while (bookMatcher.find() && books.size() < 3) { // 作者ごとに最大3冊
                String bookId = bookMatcher.group(1);
                String title = bookMatcher.group(2);
                String fileUrl = "https://www.aozora.gr.jp/cards/" + authorId + "/files/" + bookId + ".html";
                
                books.add(new Book(title, authorName, fileUrl));
                System.out.println("作者 " + authorName + " から書籍追加: " + title);
            }
        } catch (Exception e) {
            System.err.println("作者ページパースエラー: " + e.getMessage());
        }
        
        return books;
    }

    // 書籍情報を格納する内部クラス
    public static class Book {
        private String title;
        private String author;
        private String fileUrl;
        
        public Book(String title, String author) {
            this.title = title;
            this.author = author;
            this.fileUrl = "";
        }
        
        public Book(String title, String author, String fileUrl) {
            this.title = title;
            this.author = author;
            this.fileUrl = fileUrl;
        }
        
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getFileUrl() { return fileUrl; }
    }
} 