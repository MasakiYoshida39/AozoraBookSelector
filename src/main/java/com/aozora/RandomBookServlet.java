package com.aozora;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/random-book")
public class RandomBookServlet extends HttpServlet {
    
    // サンプルデータ（一時的な解決策）
    private static final List<Book> SAMPLE_BOOKS = Arrays.asList(
        new Book("773", "こころ", "夏目漱石", "小説", "私（わたくし）はその人を常に先生と呼んでいた。"),
        new Book("45630", "〔雨ニモマケズ〕", "宮沢賢治", "詩", "雨ニモマケズ 風ニモマケズ"),
        new Book("35", "走れメロス", "太宰治", "短編小説", "メロスは激怒した。必ず、かの邪智暴虐の王を除かなければならぬと決意した。"),
        new Book("119", "山月記", "中島敦", "短編小説", "隴西の李徴は博学才穎、天宝の末年、若くして名を虎榜に連ね、"),
        new Book("879", "羅生門", "芥川竜之介", "短編小説", "ある日の暮方の事である。一人の下人が、羅生門の下で雨やみを待っていた。"),
        new Book("647", "野菊の墓", "伊藤左千夫", "小説", "後（のち）の月という時分が来ると、どうも思わずには居られない。"),
        new Book("472", "やまなし", "宮沢賢治", "短編小説", "小さな谷川の底を写した二枚の青い幻燈です。"),
        new Book("18376", "桃太郎", "楠山正雄", "童話", "むかし、むかし、あるところに、おじいさんとおばあさんがありました。"),
        new Book("45761", "河童", "芥川竜之介", "小説", "どうか Kappa と発音して下さい。"),
        new Book("773", "こころ", "夏目漱石", "小説", "私（わたくし）はその人を常に先生と呼んでいた。")
    );
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            // サンプルデータからランダムに選択
            Random random = new Random();
            Book selectedBook = SAMPLE_BOOKS.get(random.nextInt(SAMPLE_BOOKS.size()));
            
            // リクエスト属性に設定
            request.setAttribute("book", selectedBook);
            request.setAttribute("message", "サンプルデータから選択されました");
            
            // JSPにフォワード
            RequestDispatcher dispatcher = request.getRequestDispatcher("/random-book.jsp");
            dispatcher.forward(request, response);
            
        } catch (Exception e) {
            // エラーハンドリング
            request.setAttribute("error", "データ取得中にエラーが発生しました: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }
    
    // 書籍情報を格納する内部クラス
    public static class Book {
        private String id;
        private String title;
        private String author;
        private String category;
        private String excerpt;
        
        public Book(String id, String title, String author, String category, String excerpt) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.category = category;
            this.excerpt = excerpt;
        }
        
        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getCategory() { return category; }
        public String getExcerpt() { return excerpt; }
    }
} 