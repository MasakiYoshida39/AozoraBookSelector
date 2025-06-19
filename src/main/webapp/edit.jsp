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
    <title>ユーザー情報編集</title>
</head>
<body>
    <h2>ユーザー情報編集</h2>
    <form method="post" action="edit">
        <label>ユーザー名: <input type="text" name="username" required value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : user %>" /></label><br />
        <label>新しいパスワード: <input type="password" name="password" /></label><br />
        <label>新しいパスワード（確認）: <input type="password" name="confirm_password" /></label><br />
        <input type="submit" value="更新" />
    </form>
    <div style="color:red">
        <c:if test="${not empty error}">
            ${error}
        </c:if>
    </div>
    <div style="color:green">
        <c:if test="${not empty message}">
            ${message}
        </c:if>
    </div>
    <a href="index.jsp">メインページへ戻る</a>
</body>
</html> 