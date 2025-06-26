<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
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
    <title>ユーザー情報編集 - 青空文庫 ランダム書籍選択</title>
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
        
        .current-user {
            background: #f8f9fa;
            border: 1px solid #e9ecef;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 30px;
            color: #495057;
        }
        
        .current-user strong {
            color: #667eea;
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
        
        .update-button {
            background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%);
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
        
        .update-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.2);
        }
        
        .home-link {
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }
        
        .home-link a {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.3s;
        }
        
        .home-link a:hover {
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
        
        .password-note {
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            border-radius: 8px;
            padding: 15px;
            margin-top: 20px;
            font-size: 0.9em;
            color: #856404;
        }
        
        .password-note h4 {
            margin: 0 0 10px 0;
            color: #856404;
            font-size: 1em;
        }
        
        .password-note p {
            margin: 0;
            line-height: 1.4;
        }
        
        .delete-account-section {
            margin-top: 30px;
            padding-top: 30px;
            border-top: 2px solid #e74c3c;
        }
        
        .delete-account-section h3 {
            color: #e74c3c;
            margin-bottom: 15px;
            font-size: 1.2em;
        }
        
        .delete-button {
            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
            color: white;
            border: none;
            padding: 12px 25px;
            border-radius: 20px;
            font-size: 1em;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        
        .delete-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 15px rgba(231, 76, 60, 0.3);
        }
        
        .delete-warning {
            background: #fff5f5;
            border: 1px solid #fed7d7;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 20px;
            font-size: 0.9em;
            color: #c53030;
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
        <h2>ユーザー情報編集</h2>
        
        <div class="current-user">
            現在のユーザー: <strong><%= user %></strong>
        </div>
        
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
        
        <form method="post" action="edit">
            <div class="form-group">
                <label for="username">ユーザー名</label>
                <input type="text" id="username" name="username" required 
                       value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : user %>" />
            </div>
            
            <div class="form-group">
                <label for="password">新しいパスワード</label>
                <input type="password" id="password" name="password" />
            </div>
            
            <div class="form-group">
                <label for="confirm_password">新しいパスワード（確認）</label>
                <input type="password" id="confirm_password" name="confirm_password" />
            </div>
            
            <button type="submit" class="update-button">🔄 情報を更新</button>
        </form>
        
        <div class="password-note">
            <h4>💡 パスワード変更について</h4>
            <p>パスワードを変更しない場合は、パスワード欄を空欄のままにしてください。</p>
        </div>
        
        <div class="delete-account-section">
            <h3>⚠️ アカウント削除</h3>
            <div class="delete-warning">
                <strong>注意:</strong> アカウントを削除すると、すべてのデータが完全に削除され、復元できません。
            </div>
            <form method="post" action="delete-account" onsubmit="return confirmDelete()">
                <button type="submit" class="delete-button">🗑️ アカウントを削除</button>
            </form>
        </div>
        
        <div class="home-link">
            <a href="index.jsp">🏠 メインページへ戻る</a>
        </div>
    </div>
    
    <div class="copyright">
        © 2024 AozoraBookSelector Project | 
        <a href="https://www.aozora.gr.jp/" target="_blank">青空文庫</a>の作品データを使用
    </div>
    
    <script>
        function confirmDelete() {
            return confirm("本当にアカウントを削除しますか？\nこの操作は取り消せません。");
        }
    </script>
</body>
</html> 