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
import static com.example.servlet.DBConfig.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        String error = null;
        String message = null;

        if (username == null || username.isEmpty() || password == null || password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
            error = "ユーザー名とパスワードは必須です。";
        } else if (!password.equals(confirmPassword)) {
            error = "パスワードが一致しません。";
        } else {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                    // ユーザー名重複チェック
                    String checkSql = "SELECT id FROM users WHERE username = ?";
                    try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                        checkStmt.setString(1, username);
                        try (ResultSet rs = checkStmt.executeQuery()) {
                            if (rs.next()) {
                                error = "そのユーザー名は既に使われています。";
                            }
                        }
                    }
                    // 重複していなければINSERT
                    if (error == null) {
                        String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setString(1, username);
                            insertStmt.setString(2, password);
                            int result = insertStmt.executeUpdate();
                            if (result > 0) {
                                message = "ユーザー登録が完了しました。ログインしてください。";
                            } else {
                                error = "登録に失敗しました。";
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                error = "データベースエラー: " + e.getMessage();
            }
        }
        request.setAttribute("error", error);
        request.setAttribute("message", message);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
} 