<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>ログイン</title>
</head>
<body>
    <h2>ログイン</h2>
    <form method="post" action="login">
        <label>ユーザー名: <input type="text" name="username" /></label><br />
        <label>パスワード: <input type="password" name="password" /></label><br />
        <input type="submit" value="ログイン" />
    </form>
    <div style="color:red">
        <c:if test="${not empty error}">
            ${error}
        </c:if>
    </div>
    <a href="register.jsp">新規ユーザー登録</a>
</body>
</html> 