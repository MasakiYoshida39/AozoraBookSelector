package com.example.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@jakarta.servlet.annotation.WebServlet("/randombook")
public class RandomBookServlet extends jakarta.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // ランダムに本を選ぶ
        AozoraBookSelector.Book selectedBook = AozoraBookSelector.selectRandomBook();
        
        // リクエスト属性に設定
        request.setAttribute("selectedBook", selectedBook);
        
        // JSPにフォワード
        request.getRequestDispatcher("/randombook.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // POSTリクエストもGETと同じ処理
        doGet(request, response);
    }
} 