<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Login</title>
</head>
<body>
<h2>Login</h2>
<form action="/login" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required/>
    <br/>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required/>
    <br/>
    <button type="submit">Login</button>
</form>
<p th:if="${param.logout}">You have been logged out successfully.</p>
<p th:if="${param.error}">Invalid username or password.</p>
</body>
</html>