<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>青空文庫 ランダム選書 - ホーム</title>
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
            max-width: 600px;
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
        
        .welcome-message {
            color: #666;
            font-size: 1.2em;
            margin-bottom: 30px;
        }
        
        .feature-card {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            border-radius: 15px;
            padding: 30px;
            margin: 20px 0;
            border-left: 5px solid #667eea;
            transition: transform 0.3s ease;
        }
        
        .feature-card:hover {
            transform: translateY(-5px);
        }
        
        .feature-title {
            font-size: 1.5em;
            color: #333;
            margin-bottom: 15px;
            font-weight: bold;
        }
        
        .feature-description {
            color: #666;
            line-height: 1.6;
            margin-bottom: 20px;
        }
        
        .btn {
            display: inline-block;
            padding: 15px 30px;
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
        
        .btn-danger {
            background: linear-gradient(45deg, #ff6b6b, #ee5a52);
            color: white;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        }
        
        .nav-links {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
        
        .nav-links a {
            margin: 0 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📚 青空文庫 ランダム選書</h1>
            <div class="welcome-message">
                ようこそ、<strong><%= user %></strong>さん！
            </div>
        </div>
        
        <div class="feature-card">
            <div class="feature-title">🎲 ランダム選書機能</div>
            <div class="feature-description">
                青空文庫の膨大な作品の中から、ランダムに1冊を選んでおすすめします。
                新しい発見や、忘れていた名作との再会が待っているかもしれません。
            </div>
            <a href="randombook" class="btn btn-primary">
                📖 本を選んでもらう
            </a>
        </div>
        
        <div class="nav-links">
            <a href="edit.jsp" class="btn btn-secondary">👤 ユーザー情報編集</a>
            <a href="logout" class="btn btn-danger">🚪 ログアウト</a>
        </div>
    </div>
</body>
</html> 