<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

<% String success = request.getParameter("success"); %>
<% if (success != null) { %>
    <p style="color: green;"><%= success %></p>
<% } %>

<h2>Login</h2>
<form action="LoginServlet" method="post">
    <label>Username:</label>
    <input type="text" name="username" required /><br>
    <label>Password:</label>
    <input type="password" name="password" required /><br>
    <button type="submit">Login</button>
</form>

</body>
</html>
