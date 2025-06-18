<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>新規ユーザー登録</title>
</head>
<body>
    <h2>新規ユーザー登録</h2>
    <form method="post" action="register">
        <label>ユーザー名: <input type="text" name="username" required /></label><br />
        <label>パスワード: <input type="password" name="password" required /></label><br />
        <input type="submit" value="登録" />
    </form>
    <div style="color:red">
        <c:if test="${not empty error}">
            ${error}
        </c:if>
        <c:if test="${not empty message}">
            <span style="color:green">${message}</span>
        </c:if>
    </div>
    <a href="login.jsp">ログイン画面へ戻る</a>
</body>
</html> 