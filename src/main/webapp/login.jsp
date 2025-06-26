<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>ログイン - 青空文庫 ランダム書籍選択</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
            max-width: 400px;
            width: 100%;
            text-align: center;
        }
        
        h2 {
            color: #333;
            margin-bottom: 30px;
            font-size: 2.5em;
            font-weight: 300;
        }
        
        .form-group {
            margin-bottom: 20px;
            text-align: left;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: 500;
        }
        
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e1e8ed;
            border-radius: 10px;
            font-size: 16px;
            transition: border-color 0.3s;
            box-sizing: border-box;
        }
        
        input[type="text"]:focus, input[type="password"]:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .login-button {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 15px 30px;
            border-radius: 25px;
            font-size: 1.1em;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
            width: 100%;
            margin-top: 10px;
        }
        
        .login-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.2);
        }
        
        .register-link {
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
        
        .register-link a {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.3s;
        }
        
        .register-link a:hover {
            color: #764ba2;
        }
        
        .guest-link {
            margin-top: 15px;
        }
        .guest-link a {
            color: #27ae60;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.3s;
        }
        .guest-link a:hover {
            color: #229954;
        }
        
        .error-message {
            background: #ffe6e6;
            color: #d63031;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            border-left: 4px solid #e74c3c;
        }
        
        .success-message {
            background: #e8f5e8;
            color: #2d5a2d;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            border-left: 4px solid #4caf50;
        }
        
        .app-title {
            color: #667eea;
            font-size: 0.9em;
            margin-bottom: 30px;
            opacity: 0.8;
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
        <div class="app-title">📚 青空文庫 ランダム書籍選択</div>
    <h2>
    <div class="guest-link">
            <a href="random-book">🎲 ゲストでランダム書籍を見る</a>
        </div>
        <!-- ログイン -->
    </h2>
        
        <% if ("1".equals(request.getParameter("logout"))) { %>
            <div class="success-message">ログアウトしました。</div>
        <% } %>
        
        <% if (request.getParameter("registered") != null) { %>
            <div class="success-message">
                ✅ ユーザー登録が完了しました！自動的にログインしました。
            </div>
        <% } %>
        
        <% if (request.getParameter("deleted") != null) { %>
            <div class="success-message">
                ✅ アカウントが正常に削除されました。ご利用ありがとうございました。
            </div>
        <% } %>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                ❌ ユーザー名またはパスワードが間違っています。
            </div>
        <% } %>
        
    <form method="post" action="login">
            <div class="form-group">
                <label for="username">ユーザー名</label>
                <input type="text" id="username" name="username" required value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>" />
            </div>
            
            <div class="form-group">
                <label for="password">パスワード</label>
                <input type="password" id="password" name="password" required />
            </div>
            
            <button type="submit" class="login-button">🚪 ログイン(工事中の為使用不可)</button>
    </form>
        
        <div class="register-link">
            <a href="register.jsp">📝 新規ユーザー登録(工事中の為使用不可)</a>
        </div>
        
    </div>
    
    <div class="copyright">
        © 2025 AozoraBookSelector Project | 
        <a href="https://www.aozora.gr.jp/" target="_blank">青空文庫</a>の作品データを使用
    </div>
</body>
</html> 