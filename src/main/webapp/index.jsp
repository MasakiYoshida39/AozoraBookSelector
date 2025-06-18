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
</head>
<body>
    <h2>ようこそ、<%= user %>さん！</h2>
    <p>ログインに成功しました。</p>
</body>
</html> 