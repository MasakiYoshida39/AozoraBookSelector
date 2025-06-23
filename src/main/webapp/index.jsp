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
    <title>AozoraBookSelector - ホーム</title>
    <style>
        body {
            font-family: 'Hiragino Kaku Gothic ProN', 'Yu Gothic', 'Meiryo', sans-serif;
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
            padding: 40px;
            text-align: center;
        }
        .header h1 {
            margin: 0;
            font-size: 2.5em;
            font-weight: 300;
        }
        .welcome-text {
            margin: 10px 0 0 0;
            font-size: 1.2em;
            opacity: 0.9;
        }
        .content {
            padding: 40px;
        }
        .feature-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 30px;
            margin-bottom: 40px;
        }
        .feature-card {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
            padding: 30px;
            border-radius: 15px;
            text-align: center;
            transition: all 0.3s ease;
            cursor: pointer;
            text-decoration: none;
            display: block;
        }
        .feature-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px rgba(0,0,0,0.3);
        }
        .feature-card.random {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        .feature-card.edit {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
            color: #2c3e50;
        }
        .feature-card.logout {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
            color: #2c3e50;
        }
        .feature-icon {
            font-size: 3em;
            margin-bottom: 15px;
            display: block;
        }
        .feature-title {
            font-size: 1.3em;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .feature-description {
            font-size: 0.9em;
            opacity: 0.9;
            line-height: 1.4;
        }
        .stats {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 30px;
            text-align: center;
        }
        .stats h3 {
            color: #495057;
            margin-bottom: 15px;
        }
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 20px;
        }
        .stat-item {
            background: white;
            padding: 15px;
            border-radius: 8px;
            border: 1px solid #e9ecef;
        }
        .stat-number {
            font-size: 1.5em;
            font-weight: bold;
            color: #4facfe;
        }
        .stat-label {
            font-size: 0.9em;
            color: #6c757d;
            margin-top: 5px;
        }
        .footer {
            text-align: center;
            padding: 20px;
            color: #6c757d;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📚 AozoraBookSelector</h1>
            <p class="welcome-text">ようこそ、<%= user %>さん！</p>
        </div>
        
        <div class="content">
            <div class="stats">
                <h3>📊 今日の統計</h3>
                <div class="stats-grid">
                    <div class="stat-item">
                        <div class="stat-number">🎲</div>
                        <div class="stat-label">ランダム書籍</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">📖</div>
                        <div class="stat-label">青空文庫</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">✨</div>
                        <div class="stat-label">新しい発見</div>
                    </div>
                </div>
            </div>
            
            <div class="feature-grid">
                <a href="random-book" class="feature-card random">
                    <span class="feature-icon">🎲</span>
                    <div class="feature-title">ランダム書籍</div>
                    <div class="feature-description">
                        青空文庫からランダムに一冊を選んでご紹介します。<br>
                        新しい作品との出会いをお楽しみください。
                    </div>
                </a>
                
                <a href="edit.jsp" class="feature-card edit">
                    <span class="feature-icon">⚙️</span>
                    <div class="feature-title">ユーザー情報編集</div>
                    <div class="feature-description">
                        アカウント情報の変更や<br>
                        設定の調整を行えます。
                    </div>
                </a>
                
                <a href="logout" class="feature-card logout">
                    <span class="feature-icon">🚪</span>
                    <div class="feature-title">ログアウト</div>
                    <div class="feature-description">
                        セッションを終了して<br>
                        ログイン画面に戻ります。
                    </div>
                </a>
            </div>
        </div>
        
        <div class="footer">
            <p>青空文庫APIを使用して書籍情報を提供しています</p>
        </div>
    </div>
</body>
</html> 