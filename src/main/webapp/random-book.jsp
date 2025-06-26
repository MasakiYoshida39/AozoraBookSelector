<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>青空文庫 ランダム書籍選択</title>
    <style>
        body {
            font-family: 'Hiragino Kaku Gothic Pro', 'Meiryo', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            margin: 0;
            padding: 20px;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            padding: 40px;
            max-width: 600px;
            width: 100%;
            text-align: center;
        }
        
        h1 {
            color: #333;
            margin-bottom: 30px;
            font-size: 2.5em;
            font-weight: 300;
        }
        
        .book-card {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            border-radius: 15px;
            padding: 30px;
            margin: 20px 0;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }
        
        .book-title {
            font-size: 1.8em;
            color: #2c3e50;
            margin-bottom: 10px;
            font-weight: bold;
        }
        
        .book-author {
            font-size: 1.2em;
            color: #7f8c8d;
            margin-bottom: 15px;
        }
        
        .book-category {
            display: inline-block;
            background: #3498db;
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 0.9em;
            margin-bottom: 20px;
        }
        
        .book-excerpt {
            font-size: 1.1em;
            line-height: 1.6;
            color: #34495e;
            text-align: left;
            background: white;
            padding: 20px;
            border-radius: 10px;
            border-left: 4px solid #3498db;
        }
        
        .book-links {
            margin-top: 20px;
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            justify-content: center;
        }
        
        .book-links .button {
            background: linear-gradient(135deg, #27ae60 0%, #2ecc71 100%);
            font-size: 0.9em;
            padding: 10px 20px;
        }
        
        .book-links .button:hover {
            background: linear-gradient(135deg, #229954 0%, #27ae60 100%);
        }
        
        .message {
            background: #e8f5e8;
            color: #2d5a2d;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            border-left: 4px solid #4caf50;
        }
        
        .button {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 15px 30px;
            border-radius: 25px;
            font-size: 1.1em;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
            text-decoration: none;
            display: inline-block;
            margin: 10px;
        }
        
        .button:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.2);
        }
        
        .error {
            background: #ffe6e6;
            color: #d63031;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            border-left: 4px solid #e74c3c;
        }
        
        .footer {
            margin-top: 30px;
            color: #7f8c8d;
            font-size: 0.9em;
        }
        
        .copyright {
            position: fixed;
            bottom: 10px;
            left: 50%;
            transform: translateX(-50%);
            color: rgba(255, 255, 255, 0.8);
            font-size: 0.8em;
            text-align: center;
            z-index: 1000;
        }
        
        .copyright a {
            color: rgba(255, 255, 255, 0.9);
            text-decoration: none;
        }
        
        .copyright a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>📚 青空文庫 ランダム書籍選択</h1>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        
        <% if (request.getAttribute("message") != null) { %>
            <div class="message">
                <%= request.getAttribute("message") %>
            </div>
        <% } %>
        
        <% if (request.getAttribute("book") != null) { %>
            <div class="book-card">
                <div class="book-title">${book.title}</div>
                <div class="book-author">著者: ${book.author}</div>
                <div class="book-category">${book.category}</div>
                <div class="book-excerpt">
                    <strong>冒頭:</strong><br>
                    ${book.excerpt}
                </div>
                <div class="book-links">
                    <a href="${book.cardUrl}" target="_blank" class="button">📖 図書カードを見る</a>
                    <a href="${book.htmlUrl}" target="_blank" class="button">🌐 青空文庫で読む</a>
                </div>
            </div>
        <% } %>
        
        <div>
            <a href="random-book" class="button">🎲 別の書籍を選択</a>
            <a href="index.jsp" class="button">🏠 ホームに戻る</a>
        </div>
        
        <div class="footer">
            <p>青空文庫の作品をランダムに表示しています</p>
        </div>
    </div>
    
    <div class="copyright">
        © 2024 AozoraBookSelector Project | 
        <a href="https://www.aozora.gr.jp/" target="_blank">青空文庫</a>の作品データを使用
    </div>
</body>
</html> 