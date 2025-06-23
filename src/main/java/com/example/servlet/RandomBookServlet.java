package com.example.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/random-book")
public class RandomBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CSV_FILE_PATH = "/WEB-INF/aozora_books.csv";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // CSVファイルからランダムに書籍を取得
            BookInfo bookInfo = getRandomBookFromCSV();
            
            if (bookInfo != null) {
                request.setAttribute("book", bookInfo);
                request.setAttribute("success", true);
                request.setAttribute("source", "CSV");
            } else {
                request.setAttribute("success", false);
                request.setAttribute("error", "CSVデータの読み込みに失敗しました。");
            }
            
        } catch (Exception e) {
            request.setAttribute("success", false);
            request.setAttribute("error", "データ取得に失敗しました: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/simple-random.jsp").forward(request, response);
    }
    
    private BookInfo getRandomBookFromCSV() throws IOException {
        List<BookInfo> books = new ArrayList<>();
        
        // デバッグ情報を追加
        System.out.println("CSVファイル読み込み開始");
        
        try (InputStream is = getServletContext().getResourceAsStream(CSV_FILE_PATH)) {
            if (is == null) {
                System.out.println("CSVファイルが見つかりません: " + CSV_FILE_PATH);
                // 代替パスを試行
                try (InputStream is2 = getClass().getClassLoader().getResourceAsStream("aozora_books.csv")) {
                    if (is2 == null) {
                        System.out.println("代替パスでもCSVファイルが見つかりません");
                        return null;
                    }
                    System.out.println("代替パスでCSVファイルを読み込みました");
                    return processCSVFile(is2);
                }
            }
            
            System.out.println("CSVファイルを読み込みました: " + CSV_FILE_PATH);
            return processCSVFile(is);
            
        } catch (Exception e) {
            System.out.println("CSVファイル読み込みエラー: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    private BookInfo processCSVFile(InputStream is) throws IOException {
        List<BookInfo> books = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String line;
            boolean firstLine = true;
            int lineCount = 0;
            
            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (firstLine) {
                    firstLine = false; // ヘッダー行をスキップ
                    System.out.println("ヘッダー行をスキップ: " + line.substring(0, Math.min(50, line.length())));
                    continue;
                }
                
                BookInfo book = parseCSVLine(line);
                if (book != null && !book.作品名.isEmpty()) {
                    books.add(book);
                }
                
                // 最初の数行のデバッグ情報
                if (lineCount <= 5) {
                    System.out.println("行 " + lineCount + ": " + line.substring(0, Math.min(50, line.length())));
                }
            }
            
            System.out.println("総行数: " + lineCount + ", 有効な書籍数: " + books.size());
        }
        
        if (books.isEmpty()) {
            System.out.println("有効な書籍データが見つかりませんでした");
            return null;
        }
        
        Random random = new Random();
        BookInfo selectedBook = books.get(random.nextInt(books.size()));
        System.out.println("選択された書籍: " + selectedBook.作品名 + " by " + selectedBook.姓名);
        
        return selectedBook;
    }
    
    private BookInfo parseCSVLine(String line) {
        try {
            // 簡単なCSVパース（カンマ区切り）
            String[] fields = line.split(",");
            System.out.println("フィールド数: " + fields.length);
            
            if (fields.length < 30) {
                System.out.println("フィールド数が不足: " + fields.length);
                return null;
            }
            
            BookInfo book = new BookInfo();
            book.作品ID = getField(fields, 0);
            book.作品名 = getField(fields, 1);
            book.姓名 = getField(fields, 15);
            book.カテゴリ = getField(fields, 35);
            book.文字数 = getField(fields, 32);
            book.公開日 = getField(fields, 11);
            book.分類番号 = getField(fields, 8);
            book.文字遣い種別 = getField(fields, 9);
            book.書き出し = getField(fields, 33);
            book.図書カードURL = getField(fields, 13);
            book.XHTML_HTMLファイルURL = getField(fields, 28);
            book.テキストファイルURL = getField(fields, 25);
            
            // デバッグ情報
            if (!book.作品名.isEmpty()) {
                System.out.println("書籍情報取得成功: " + book.作品名 + " (" + book.作品ID + ")");
            }
            
            return book;
        } catch (Exception e) {
            System.out.println("CSV行パースエラー: " + e.getMessage());
            return null;
        }
    }
    
    private String getField(String[] fields, int index) {
        if (index < fields.length && fields[index] != null) {
            return fields[index].trim();
        }
        return "";
    }
    
    // 書籍情報を格納する内部クラス
    public static class BookInfo {
        public String 作品ID = "";
        public String 作品名 = "";
        public String 姓名 = "";
        public String カテゴリ = "";
        public String 文字数 = "";
        public String 公開日 = "";
        public String 分類番号 = "";
        public String 文字遣い種別 = "";
        public String 書き出し = "";
        public String 図書カードURL = "";
        public String XHTML_HTMLファイルURL = "";
        public String テキストファイルURL = "";
    }
} 