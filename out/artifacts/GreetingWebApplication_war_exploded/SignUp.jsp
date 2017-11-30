<%--
  The JSP showing the user sign-up screen
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Sign Up</title>
    <style>
        div.signup_form {
            background-color:snow;
            padding: 10px;
            border:3px green solid;
            border-radius: 20px;
            width: 40%;
        }
        div.error_message {
            color: red;
            font: 15px bold;
            width: 40%;
        }
    </style>
</head>
<body>
    <c:if test="${sessionScope.user != null}">
        <jsp:forward page="/Start"/>
    </c:if>
    <c:if test="${requestScope.signup_error != null}">
        <div class="error_message">
            <c:out value="${requestScope.signup_error}"/>
        </div>
    </c:if>
    <br/>
    <div class="signup_form">
        <h3>Пожалуйста укажите имя и пароль, чтобы создать нового пользователя.</h3>
        <form action="/Start" name="signUp" method="POST">
            <label for="newUserName">Имя Пользователя:</label>
            <input type="text" id="newUserName" name="newUserName" />
            <br/>
            <label for="newPassword">Пароль:</label>
            <input type="text" id="newPassword" name="newPassword" />
            <br/>
            <label for="newPasswordConfirmation">Подтверждение Пароля:</label>
            <input type="text" id="newPasswordConfirmation" name="newPasswordConfirmation" />
            <br/>
            <button name="signUp" type="submit" value="signUp">Зарегистрироваться</button>
        </form>
    </div>
</body>
</html>
