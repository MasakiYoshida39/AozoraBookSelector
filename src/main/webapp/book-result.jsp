<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.model.Book" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>書籍選択結果 - 青空文庫セレクター</title>
    <style>
        body {
            font-family: 'Hiragino Kaku Gothic ProN', 'Yu Gothic', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            margin: 0;
            padding: 20px;
            min-height: 100vh;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            overflow: hidden;
        }
        .header {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        .header h1 {
            margin: 0;
            font-size: 2.5em;
            font-weight: 300;
        }
        .content {
            padding: 40px;
        }
        .book-card {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 30px;
            margin-bottom: 30px;
            border-left: 5px solid #4facfe;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        .book-title {
            font-size: 1.8em;
            color: #2c3e50;
            margin-bottom: 15px;
            font-weight: 600;
        }
        .book-author {
            font-size: 1.2em;
            color: #7f8c8d;
            margin-bottom: 20px;
            font-style: italic;
        }
        .book-url {
            background: #3498db;
            color: white;
            padding: 12px 25px;
            text-decoration: none;
            border-radius: 25px;
            display: inline-block;
            transition: all 0.3s ease;
            font-weight: 500;
        }
        .book-url:hover {
            background: #2980b9;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(52, 152, 219, 0.4);
        }
        .back-button {
            background: #95a5a6;
            color: white;
            padding: 12px 25px;
            text-decoration: none;
            border-radius: 25px;
            display: inline-block;
            margin-top: 20px;
            transition: all 0.3s ease;
        }
        .back-button:hover {
            background: #7f8c8d;
            transform: translateY(-2px);
        }
        .api-info {
            background: #e8f5e8;
            border: 1px solid #4caf50;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 20px;
            color: #2e7d32;
        }
        .error-message {
            background: #ffebee;
            border: 1px solid #f44336;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 20px;
            color: #c62828;
        }
        .book-details {
            background: white;
            border-radius: 8px;
            padding: 20px;
            margin-top: 20px;
            border: 1px solid #e0e0e0;
        }
        .detail-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            padding: 8px 0;
            border-bottom: 1px solid #f0f0f0;
        }
        .detail-label {
            font-weight: 600;
            color: #555;
        }
        .detail-value {
            color: #333;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📚 青空文庫セレクター</h1>
            <p>あなたにおすすめの書籍を見つけました</p>
        </div>
        
        <div class="content">
            <% if (request.getAttribute("apiUsed") != null) { %>
                <div class="api-info">
                    <strong>API情報:</strong> <%= request.getAttribute("apiUsed") %>
                </div>
            <% } %>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <strong>エラー:</strong> <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <% 
            List<Book> books = (List<Book>) request.getAttribute("books");
            if (books != null && !books.isEmpty()) {
                for (Book book : books) {
            %>
                <div class="book-card">
                    <div class="book-title">📖 <%= book.getTitle() %></div>
                    <div class="book-author">✍️ <%= book.getAuthor() %></div>
                    
                    <div class="book-details">
                        <div class="detail-row">
                            <span class="detail-label">作品タイトル:</span>
                            <span class="detail-value"><%= book.getTitle() %></span>
                        </div>
                        <div class="detail-row">
                            <span class="detail-label">著者:</span>
                            <span class="detail-value"><%= book.getAuthor() %></span>
                        </div>
                        <div class="detail-row">
                            <span class="detail-label">ファイル形式:</span>
                            <span class="detail-value">HTML</span>
                        </div>
                        <div class="detail-row">
                            <span class="detail-label">データソース:</span>
                            <span class="detail-value">goark/aozora-api (青空文庫)</span>
                        </div>
                    </div>
                    
                    <a href="<%= book.getFileUrl() %>" target="_blank" class="book-url">
                        📖 青空文庫で読む
                    </a>
                </div>
            <% 
                }
            } else {
            %>
                <div class="error-message">
                    <strong>書籍が見つかりませんでした。</strong><br>
                    APIからの応答が正常に取得できませんでした。しばらく時間をおいて再度お試しください。
                </div>
            <% } %>
            
            <a href="book-selector.jsp" class="back-button">← 別の書籍を選択</a>
        </div>
    </div>
</body>
</html> 