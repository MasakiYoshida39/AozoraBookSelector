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

import static com.example.servlet.DBConfig.*;

@WebServlet("/edit")
public class EditServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String currentUser = (session != null) ? (String) session.getAttribute("user") : null;
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String newUsername = request.getParameter("username");
        String newPassword = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        String error = null;
        String message = null;

        if (newUsername == null || newUsername.isEmpty()) {
            error = "ユーザー名は必須です。";
        } else if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
            error = "パスワードが一致しません。";
        } else {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                    // ユーザー名が変更されている場合、重複チェック
                    if (!currentUser.equals(newUsername)) {
                        String checkSql = "SELECT id FROM users WHERE username = ?";
                        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                            checkStmt.setString(1, newUsername);
                            try (ResultSet rs = checkStmt.executeQuery()) {
                                if (rs.next()) {
                                    error = "そのユーザー名は既に使われています。";
                                }
                            }
                        }
                    }
                    // エラーがなければUPDATE
                    if (error == null) {
                        String updateSql;
                        if (newPassword != null && !newPassword.isEmpty()) {
                            updateSql = "UPDATE users SET username = ?, password = ? WHERE username = ?";
                        } else {
                            updateSql = "UPDATE users SET username = ? WHERE username = ?";
                        }
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, newUsername);
                            if (newPassword != null && !newPassword.isEmpty()) {
                                updateStmt.setString(2, newPassword);
                                updateStmt.setString(3, currentUser);
                            } else {
                                updateStmt.setString(2, currentUser);
                            }
                            int result = updateStmt.executeUpdate();
                            if (result > 0) {
                                message = "ユーザー情報を更新しました。";
                                session.setAttribute("user", newUsername);
                            } else {
                                error = "更新に失敗しました。";
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
        request.setAttribute("username", newUsername);
        request.getRequestDispatcher("edit.jsp").forward(request, response);
    }
} 