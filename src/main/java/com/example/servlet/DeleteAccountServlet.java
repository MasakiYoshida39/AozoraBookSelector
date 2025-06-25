package com.example.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import static com.example.servlet.DBConfig.*;

@WebServlet("/delete-account")
public class DeleteAccountServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
                // ユーザーを削除
                String deleteSql = "DELETE FROM users WHERE username = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setString(1, user);
                    int result = deleteStmt.executeUpdate();
                    
                    if (result > 0) {
                        // セッションを無効化
                        session.invalidate();
                        // ログイン画面にリダイレクト（退会完了メッセージ付き）
                        response.sendRedirect("login.jsp?deleted=1");
                    } else {
                        // 削除に失敗
                        request.setAttribute("error", "退会処理に失敗しました。");
                        request.getRequestDispatcher("edit.jsp").forward(request, response);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "データベースエラー: " + e.getMessage());
            request.getRequestDispatcher("edit.jsp").forward(request, response);
        }
    }
} 