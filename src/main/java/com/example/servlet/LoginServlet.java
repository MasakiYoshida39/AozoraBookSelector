package com.example.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    // DB接続情報（必要に応じて修正してください）
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/aozora_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root"; // ←MySQLのユーザー名に合わせて修正
    private static final String DB_PASSWORD = ""; // ←MySQLのパスワードに合わせて修正

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean authenticated = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            authenticated = true;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "データベース接続エラー: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (authenticated) {
            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            response.sendRedirect("index.jsp");
        } else {
            request.setAttribute("error", "ユーザー名またはパスワードが違います");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
} 