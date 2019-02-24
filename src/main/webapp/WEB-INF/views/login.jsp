<%--
  Created by IntelliJ IDEA.
  User: macura
  Date: 12/1/2018
  Time: 1:28 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

<h3>Login Form</h3>

<br/>
<form action="/upload/login/process_login" method="post">
    Username:<input type="text" name="username"/><br/><br/>
    Password:<input type="password" name="password"/><br/><br/>
    <input type="submit" value="login"/>
</form>

</body>
</html>
