<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>シンプルランダム書籍</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .book-info {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .book-title {
            font-size: 24px;
            color: #333;
            margin-bottom: 10px;
        }
        .book-author {
            font-size: 18px;
            color: #666;
            margin-bottom: 15px;
        }
        .book-details {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 10px;
            margin-bottom: 15px;
        }
        .detail-item {
            background: #f8f9fa;
            padding: 10px;
            border-radius: 4px;
        }
        .detail-label {
            font-weight: bold;
            color: #495057;
            font-size: 12px;
        }
        .detail-value {
            color: #212529;
            margin-top: 5px;
        }
        .book-opening {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 15px;
            font-style: italic;
        }
        .book-links {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
        .book-link {
            display: inline-block;
            padding: 10px 20px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .book-link:hover {
            background: #0056b3;
        }
        .error {
            background: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 4px;
            border: 1px solid #f5c6cb;
        }
        .actions {
            text-align: center;
            margin-top: 20px;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            margin: 0 10px;
            background: #28a745;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .btn:hover {
            background: #218838;
        }
        .btn-secondary {
            background: #6c757d;
        }
        .btn-secondary:hover {
            background: #545b62;
        }
    </style>
</head>
<body>
    <h1>📚 シンプルランダム書籍</h1>
    
    <%
    // リクエスト属性からデータを取得
    Boolean success = (Boolean) request.getAttribute("success");
    String error = (String) request.getAttribute("error");
    String warning = (String) request.getAttribute("warning");
    String source = (String) request.getAttribute("source");
    Object bookObj = request.getAttribute("book");
    
    if (success != null && success && bookObj != null) {
        com.example.servlet.RandomBookServlet.BookInfo book = 
            (com.example.servlet.RandomBookServlet.BookInfo) bookObj;
    %>
        <%
        if (warning != null && !warning.isEmpty()) {
        %>
            <div style="background: #fff3cd; color: #856404; padding: 15px; border-radius: 4px; border: 1px solid #ffeaa7; margin-bottom: 20px;">
                <strong>⚠️ 注意:</strong> <%= warning %>
            </div>
        <%
        }
        %>
        
        <%
        if (source != null && !source.isEmpty()) {
        %>
            <div style="background: #d1ecf1; color: #155724; padding: 10px; border-radius: 4px; border: 1px solid #c3e6cb; margin-bottom: 20px; text-align: center; font-weight: bold;">
                📊 CSVデータから取得
            </div>
        <%
        }
        %>
        
        <div class="book-info">
            <div class="book-title"><%= book.作品名 %></div>
            <div class="book-author">著者: <%= book.姓名 %></div>
            
            <div class="book-details">
                <div class="detail-item">
                    <div class="detail-label">作品ID</div>
                    <div class="detail-value"><%= book.作品ID %></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">カテゴリ</div>
                    <div class="detail-value"><%= book.カテゴリ %></div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">文字数</div>
                    <div class="detail-value"><%= book.文字数 %>文字</div>
                </div>
                <div class="detail-item">
                    <div class="detail-label">公開日</div>
                    <div class="detail-value"><%= book.公開日 %></div>
                </div>
            </div>
            
            <%
            if (book.書き出し != null && !book.書き出し.isEmpty()) {
            %>
                <div class="book-opening">
                    <strong>書き出し:</strong><br>
                    <%= book.書き出し %>
                </div>
            <%
            }
            %>
            
            <div class="book-links">
                <%
                if (book.図書カードURL != null && !book.図書カードURL.isEmpty()) {
                %>
                    <a href="<%= book.図書カードURL %>" target="_blank" class="book-link">
                        📖 図書カードを見る
                    </a>
                <%
                }
                %>
                <%
                if (book.XHTML_HTMLファイルURL != null && !book.XHTML_HTMLファイルURL.isEmpty()) {
                %>
                    <a href="<%= book.XHTML_HTMLファイルURL %>" target="_blank" class="book-link">
                        📄 HTML版を読む
                    </a>
                <%
                }
                %>
            </div>
        </div>
    <%
    } else {
    %>
        <div class="error">
            <h3>エラーが発生しました</h3>
            <p><%= error != null ? error : "不明なエラーが発生しました" %></p>
        </div>
    <%
    }
    %>
    
    <div class="actions">
        <a href="random-book" class="btn">🎲 別の書籍を取得</a>
        <a href="index.jsp" class="btn btn-secondary">🏠 ホームに戻る</a>
    </div>
</body>
</html> 