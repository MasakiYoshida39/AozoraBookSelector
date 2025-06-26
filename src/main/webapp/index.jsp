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
    <title>メインページ</title>
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
            position: relative;
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
        
        h2 {
            color: #333;
            margin-bottom: 30px;
            font-size: 2.5em;
            font-weight: 300;
        }
        
        .welcome-message {
            font-size: 1.2em;
            color: #666;
            margin-bottom: 30px;
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
        
        .logout-button {
            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
        }
        
        .logout-button:hover {
            background: linear-gradient(135deg, #c0392b 0%, #a93226 100%);
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
    <h2>ようこそ、<%= user %>さん！</h2>
        <div class="welcome-message">
            ログインに成功しました。
        </div>
        
        <div>
            <a href="random-book" class="button">📚 ランダム書籍選択</a>
            <a href="edit.jsp" class="button">👤 ユーザー情報編集</a>
            <a href="logout" class="button logout-button">🚪 ログアウト</a>
        </div>
    </div>
    
    <div class="copyright">
        © 2024 AozoraBookSelector Project | 
        <a href="https://www.aozora.gr.jp/" target="_blank">青空文庫</a>の作品データを使用
    </div>
</body>
</html> 