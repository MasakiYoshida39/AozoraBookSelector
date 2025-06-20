<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.example.servlet.BookSelectorServlet.Book" %>
<%
    String user = null;
    if (session != null) {
        user = (String) session.getAttribute("user");
    }
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>書籍選択</title>
    <style>
        .book-info {
            border: 1px solid #ccc;
            padding: 20px;
            margin: 20px 0;
            border-radius: 5px;
        }
        .book-title {
            font-size: 24px;
            font-weight: bold;
            color: #333;
            margin-bottom: 10px;
        }
        .book-author {
            font-size: 18px;
            color: #666;
            margin-bottom: 15px;
        }
        .book-description {
            line-height: 1.6;
            margin-bottom: 15px;
        }
        .button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 5px;
            margin: 5px;
            display: inline-block;
        }
        .button:hover {
            background-color: #45a049;
        }
        .error {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <h2>ランダム書籍選択</h2>
    
    <% if (request.getAttribute("warning") != null) { %>
        <div style="color:orange; font-weight:bold; margin-bottom:10px;">
            <%= request.getAttribute("warning") %>
        </div>
    <% } %>
    
    <% if (request.getAttribute("error") != null) { %>
        <div class="error">
            <%= request.getAttribute("error") %>
        </div>
    <% } else if (request.getAttribute("book") != null) { %>
        <% Book book = (Book) request.getAttribute("book"); %>
        <div class="book-info">
            <div class="book-title">
                <%= book.getTitle() != null ? book.getTitle() : "タイトル不明" %>
            </div>
            <div class="book-author">
                著者: <%= book.getAuthor() != null ? book.getAuthor() : "著者不明" %>
            </div>
            <% if (book.getFileUrl() != null && !book.getFileUrl().isEmpty()) { %>
                <div>
                    <a href="<%= book.getFileUrl() %>" target="_blank" class="button">
                        青空文庫で読む
                    </a>
                </div>
            <% } %>
        </div>
        
        <p>総書籍数: <%= request.getAttribute("totalBooks") %>冊</p>
    <% } %>
    
    <div>
        <a href="book-selector" class="button">別の書籍を選ぶ</a>
        <a href="index.jsp" class="button">メインページへ戻る</a>
    </div>
</body>
</html> 