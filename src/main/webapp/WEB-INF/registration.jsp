<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>User Registration</title>
</head>
<body>
<h2 th:if="${edit}">Edit User</h2>
<h2 th:if="${!edit}">Register New User</h2>
<form action="#" th:action="@{/newuser}" th:method="post" th:object="${user}">
    <input type="hidden" th:field="*{id}"/>
    <label for="username">Username:</label>
    <input type="text" id="username" th:field="*{username}" required/>
    <div th:if="${#fields.hasErrors('username')}" th:errors="*{username}"></div>
    <br/>

    <label for="email">Email:</label>
    <input type="email" id="email" th:field="*{email}" required/>
    <div th:if="${#fields.hasErrors('email')}" th:errors="*{email}"></div>
    <br/>

    <button type="submit" th:text="${edit ? 'Update' : 'Register'}"></button>
</form>
<a th:href="@{/list}">Back to Users List</a>
</body>
</html>
