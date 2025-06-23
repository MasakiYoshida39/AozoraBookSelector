package com.example.servlet;

import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@jakarta.servlet.annotation.WebServlet("/random-book")
public class RandomBookServlet extends jakarta.servlet.http.HttpServlet {
    private BookSelector bookSelector;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // クラスパスからCSVファイルを読み込み
            InputStream is = getClass().getClassLoader().getResourceAsStream("books.csv");
            if (is == null) {
                throw new ServletException("books.csv が見つかりません。src/main/resources/books.csv に配置してください。");
            }
            bookSelector = new BookSelector(is);
        } catch (IOException e) {
            throw new ServletException("CSVの読み込みに失敗しました: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Book book = bookSelector.getRandomBook();
        if (book == null) {
            request.setAttribute("error", "書籍データがありません");
        } else {
            request.setAttribute("book", book);
        }
        request.getRequestDispatcher("/random-book.jsp").forward(request, response);
    }
} 