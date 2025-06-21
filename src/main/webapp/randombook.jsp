<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.servlet.AozoraBookSelector.Book" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>青空文庫 ランダム選書</title>
    <style>
        body {
            font-family: 'Hiragino Kaku Gothic Pro', 'Meiryo', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            margin: 0;
            padding: 20px;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        
        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            padding: 40px;
            max-width: 700px;
            width: 100%;
            text-align: center;
        }
        
        .header {
            margin-bottom: 30px;
        }
        
        .header h1 {
            color: #333;
            font-size: 2.5em;
            margin-bottom: 10px;
            background: linear-gradient(45deg, #667eea, #764ba2);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
        
        .header p {
            color: #666;
            font-size: 1.1em;
        }
        
        .book-card {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            border-radius: 15px;
            padding: 30px;
            margin: 20px 0;
            border-left: 5px solid #667eea;
        }
        
        .book-title {
            font-size: 1.8em;
            color: #333;
            margin-bottom: 10px;
            font-weight: bold;
        }
        
        .book-author {
            font-size: 1.2em;
            color: #666;
            margin-bottom: 20px;
        }
        
        .book-info {
            display: flex;
            justify-content: space-around;
            flex-wrap: wrap;
            margin: 20px 0;
            gap: 15px;
        }
        
        .info-item {
            background: white;
            padding: 10px 15px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            min-width: 120px;
        }
        
        .info-label {
            font-size: 0.9em;
            color: #666;
            margin-bottom: 5px;
        }
        
        .info-value {
            font-size: 1.1em;
            color: #333;
            font-weight: bold;
        }
        
        .book-description {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin: 20px 0;
            text-align: left;
            line-height: 1.6;
            color: #555;
        }
        
        .book-link {
            display: inline-block;
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
            padding: 12px 30px;
            text-decoration: none;
            border-radius: 25px;
            font-weight: bold;
            margin: 10px;
            transition: transform 0.3s ease;
        }
        
        .book-link:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .button-group {
            margin-top: 30px;
        }
        
        .btn {
            display: inline-block;
            padding: 12px 30px;
            margin: 10px;
            border: none;
            border-radius: 25px;
            font-size: 1.1em;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
        }
        
        .btn-primary {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
        }
        
        .btn-secondary {
            background: linear-gradient(45deg, #f093fb, #f5576c);
            color: white;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        
        .loading {
            display: none;
            margin: 20px 0;
        }
        
        .spinner {
            border: 4px solid #f3f3f3;
            border-top: 4px solid #667eea;
            border-radius: 50%;
            width: 40px;
            height: 40px;
            animation: spin 1s linear infinite;
            margin: 0 auto;
        }
        
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        
        .error {
            background: #ffe6e6;
            border: 1px solid #ff9999;
            color: #cc0000;
            padding: 15px;
            border-radius: 10px;
            margin: 20px 0;
        }
        
        .api-info {
            background: #e8f4fd;
            border: 1px solid #b3d9ff;
            color: #0066cc;
            padding: 10px;
            border-radius: 8px;
            margin: 15px 0;
            font-size: 0.9em;
        }
        
        .fallback-info {
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            color: #856404;
            padding: 10px;
            border-radius: 8px;
            margin: 15px 0;
            font-size: 0.9em;
        }
        
        .data-source-badge {
            display: inline-block;
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 0.8em;
            font-weight: bold;
            margin-left: 10px;
        }
        
        .data-source-zorapi {
            background: #d4edda;
            color: #155724;
        }
        
        .data-source-webscraping {
            background: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📚 青空文庫 ランダム選書</h1>
            <p>今日は何を読みましょうか？</p>
        </div>
        
        <%
        Book selectedBook = (Book) request.getAttribute("selectedBook");
        if (selectedBook != null) {
        %>
            <div class="book-card">
                <div class="book-title">
                    「<%= selectedBook.getTitle() %>」
                    <span class="data-source-badge <%= "ZORAPI".equals(selectedBook.getDataSource()) ? "data-source-zorapi" : "data-source-webscraping" %>">
                        <%= selectedBook.getDataSource() != null ? selectedBook.getDataSource() : "Unknown" %>
                    </span>
                </div>
                <div class="book-author">著者: <%= selectedBook.getAuthor() %></div>
                
                <div class="book-info">
                    <div class="info-item">
                        <div class="info-label">カテゴリ</div>
                        <div class="info-value">
                            <% if (selectedBook.getCategory() != null && !selectedBook.getCategory().isEmpty() && !"不明".equals(selectedBook.getCategory())) { %>
                                <%= selectedBook.getCategory() %>
                            <% } else { %>
                                不明
                            <% } %>
                        </div>
                    </div>
                    
                    <div class="info-item">
                        <div class="info-label">アクセス数</div>
                        <div class="info-value">
                            <% if (selectedBook.getAccessCount() > 0) { %>
                                <%= String.format("%,d", selectedBook.getAccessCount()) %>
                            <% } else { %>
                                不明
                            <% } %>
                        </div>
                    </div>
                    
                    <div class="info-item">
                        <div class="info-label">作品ID</div>
                        <div class="info-value"><%= selectedBook.getId() %></div>
                    </div>
                </div>
                
                <% if (selectedBook.getDescription() != null && !selectedBook.getDescription().isEmpty()) { %>
                    <div class="book-description">
                        <strong>📖 書き出し:</strong><br>
                        <%= selectedBook.getDescription() %>
                    </div>
                <% } %>
                
                <% if (!selectedBook.getUrl().startsWith("エラー")) { %>
                    <a href="<%= selectedBook.getUrl() %>" target="_blank" class="book-link">
                        📖 青空文庫で読む
                    </a>
                <% } %>
            </div>
            
            <% if ("ZORAPI".equals(selectedBook.getDataSource())) { %>
                <div class="api-info">
                    💡 この情報は <a href="https://api.bungomail.com/" target="_blank" style="color: #0066cc;">ZORAPI（ゾラピ）</a> から取得しています
                </div>
            <% } else if ("WebScraping".equals(selectedBook.getDataSource())) { %>
                <div class="fallback-info">
                    ⚠️ ZORAPIが利用できないため、Webスクレイピングでデータを取得しています。一部の情報（カテゴリ、アクセス数）は利用できません。
                </div>
            <% } %>
        <% } else { %>
            <div class="error">
                本の取得に失敗しました。しばらく時間をおいてから再度お試しください。
            </div>
        <% } %>
        
        <div class="button-group">
            <button onclick="selectNewBook()" class="btn btn-primary">
                🎲 別の本を選ぶ
            </button>
            <a href="index.jsp" class="btn btn-secondary">
                🏠 ホームに戻る
            </a>
        </div>
        
        <div class="loading" id="loading">
            <div class="spinner"></div>
            <p>本を選んでいます...</p>
        </div>
    </div>
    
    <script>
        function selectNewBook() {
            document.getElementById('loading').style.display = 'block';
            window.location.href = 'randombook';
        }
        
        // ページ読み込み時にローディングを非表示
        window.onload = function() {
            document.getElementById('loading').style.display = 'none';
        };
    </script>
</body>
</html> 