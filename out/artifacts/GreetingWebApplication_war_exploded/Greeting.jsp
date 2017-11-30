<%--
  The JSP showing the user sign-up screen
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Sign Up</title>
    <style>
        div.greeting {
            background-color:snow;
            padding: 10px;
            border:3px green solid;
            border-radius: 20px;
            width: 40%;
        }
    </style>
</head>
<body>
    <c:if test="${sessionScope.user == null}">
        <jsp:forward page="/Start"/>
    </c:if>
    <br/>
    <div class="greeting">
        <div id="text_with_client_date"></div>
        <br/>
        <div id="last_login_ip">
            <h5>В прошлый раз Вы к нам заходили с адреса:  <c:out value="${requestScope.priorIpToDisplay}"/>.</h5>
        </div>
        <br/>
        <div id="views_count">
            <h5>Наши пользователи просматривали эту страницу уже <c:out value="${requestScope.viewsCount}"/> раз.</h5>
        </div>
        <br/>
        <form action="/Start" name="logout" method="POST">
            <button name="logout" type="submit" value="logout">Выход</button>
        </form>
    </div>
    <script type="text/javascript">
        var dateElement = document.getElementById("text_with_client_date");
        var clientDate = new Date();
        var day = clientDate.getDate();
        var month = clientDate.getMonth() + 1;
        var monthString = "";
        switch(month){
            case 1: monthString = "января"; break;
            case 2: monthString = "февраля"; break;
            case 3: monthString = "марта"; break;
            case 4: monthString = "апреля"; break;
            case 5: monthString = "мая"; break;
            case 6: monthString = "июня"; break;
            case 7: monthString = "июля"; break;
            case 8: monthString = "августа"; break;
            case 9: monthString = "сентября"; break;
            case 10: monthString = "октября"; break;
            case 11: monthString = "ноября"; break;
            case 12: monthString = "декабря"; break;
        }
        var year = clientDate.getFullYear();
        var hour = clientDate.getHours();
        var minute = clientDate.getMinutes();
        var greeting = "";
        if(hour < 6 || hour > 22){ greeting = "Доброй ночи!"; }
        else if(hour < 10) { greeting = "Доброе утро!"; }
        else if(hour < 18) { greeting = "Добрый день!"; }
        else { greeting = "Добрый вечер!"; }
        dateElement.innerHTML = "<h3>" + greeting + " Вы нас посетили " + day + ". " + monthString + " " + year + "-го года в " + hour + " часов " + minute + " минут по времени на Вашем компьютере.</h3>";
    </script>
</body>
</html>
