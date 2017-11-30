<%--
  The JSP showing the user sign-in screen
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Sign In</title>
    <style>
        div.signin_form {
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
        div.info_message {
            color: blue;
            font: 15px bold;
            width: 40%;
        }
    </style>
</head>
<body>
    <c:if test="${sessionScope.user != null}">
        <jsp:forward page="/Start"/>
    </c:if>
    <c:if test="${requestScope.signinInfo != null}">
        <div class="info_message">
            <c:out value="${requestScope.signinInfo}"/>
        </div>
    </c:if>
    <c:if test="${requestScope.signinError != null}">
        <div class="error_message">
            <c:out value="${requestScope.signinError}"/>
        </div>
    </c:if>
    <div class = "signin_form">
        <h3>Пожалуйста укажите Ваши учётные данные.</h3>
        <form action="/Start" name="userName" method="POST">
            <label for="userName">Имя Пользователя:</label>
            <input type="text" id="userName" name="userName" />
            <br/>
            <label for="password">Пароль:</label>
            <input type="text" id="password" name="password" />
            <br/>
            <button name="login" type="submit" value="login">Войти</button>
        </form>
        <br/><hr/><br/>
        <form>
            <button name="signUp" type="submit" value="signUp">Регистрация</button>
        </form>
    </div>
</body>
</html>
