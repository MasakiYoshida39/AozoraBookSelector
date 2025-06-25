<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>新規ユーザー登録 - 青空文庫 ランダム書籍選択</title>
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
            max-width: 450px;
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
        
        .register-button {
            background: linear-gradient(135deg, #27ae60 0%, #2ecc71 100%);
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
        
        .register-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.2);
        }
        
        .login-link {
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
        
        .login-link a {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.3s;
        }
        
        .login-link a:hover {
            color: #764ba2;
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
        
        .password-requirements {
            background: #f8f9fa;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 15px;
            margin-top: 20px;
            font-size: 0.9em;
            color: #6c757d;
        }
        
        .password-requirements h4 {
            margin: 0 0 10px 0;
            color: #495057;
            font-size: 1em;
        }
        
        .password-requirements ul {
            margin: 0;
            padding-left: 20px;
        }
        
        .password-requirements li {
            margin-bottom: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="app-title">📚 青空文庫 ランダム書籍選択</div>
    <h2>新規ユーザー登録</h2>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        
        <% if (request.getAttribute("message") != null) { %>
            <div class="success-message">
                <%= request.getAttribute("message") %>
            </div>
        <% } %>
        
    <form method="post" action="register">
            <div class="form-group">
                <label for="username">ユーザー名</label>
                <input type="text" id="username" name="username" required 
                       value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>" />
            </div>
            
            <div class="form-group">
                <label for="password">パスワード</label>
                <input type="password" id="password" name="password" required />
            </div>
            
            <div class="form-group">
                <label for="confirm_password">パスワード（確認）</label>
                <input type="password" id="confirm_password" name="confirm_password" required />
            </div>
            
            <button type="submit" class="register-button">📝 アカウント作成</button>
    </form>
        
        <div class="password-requirements">
            <h4>🔒 パスワード要件</h4>
            <ul>
                <li>8文字以上</li>
                <li>英数字を含む</li>
                <li>特殊文字を含むことを推奨</li>
            </ul>
        </div>
        
        <div class="login-link">
            <a href="login.jsp">🚪 ログイン画面へ戻る</a>
        </div>
    </div>
</body>
</html> 