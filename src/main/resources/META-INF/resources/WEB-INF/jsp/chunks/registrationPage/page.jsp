<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<c:choose>
    <c:when test="${registrationIsOpen}">
        <form:form modelAttribute="signUpForm" action="signUp.html" method="POST" id="registration-form" class="abys-branded" onsubmit="return validateData();">
            <div class="form-row">
                <label><spring:message code="label.signUp.userName"/>:</label>
                <form:input maxlength="30" path="name" />
                <form:errors path="name" cssClass="errors" />
            </div>
            <div class="form-row">
                <label style="white-space: nowrap;"><spring:message htmlEscape="false" code="label.signUp.email"/>:</label>
                <form:input maxlength="200" path="email" />@epam.com
                <form:errors path="email" cssClass="errors" />
            </div>
            <%--<div class="form-row">--%>
                <%--<label><spring:message code="label.signUp.phone"/>:</label>--%>
                <%--<form:input maxlength="20" path="phone" />--%>
                <%--&lt;%&ndash;<span>Номер телефона нужен для обратной связи</span>&ndash;%&gt;--%>
                <%--<form:errors path="phone" cssClass="errors" />--%>
            <%--</div>--%>
            <div class="form-row">
                <label><spring:message code="label.signUp.password"/>:</label>
                <form:password maxlength="30" path="password" />
                <form:errors path="password" cssClass="errors" />
            </div>
            <div class="form-row">
                    <label><spring:message code="label.signUp.repassword"/>:</label>
                    <form:password maxlength="30" path="repassword" />
                    <form:errors path="repassword" cssClass="errors" />
            </div>
            <div style="display:none">
                    <form:checkbox path="agreed" />
                    <spring:message code="label.signUp.agreed"/>
                    <form:errors path="agreed" cssClass="errors" />
            </div>
            <div class="form-row"><input type="submit" value="Регистрация" class="submit" /></div>
        </form:form>
        <div class="right-bar opac-black-container">
            Регистрация необходима для участия в турнире и подготовки к нему.
            <ul>
                <li class="icon-1">Придумай уникальное имя бота. Используй его для&nbsp;входа на&nbsp;сайт&nbsp;игры</li>
                <li class="icon-2">Ознакомься с&nbsp;API игрового&nbsp;сервера</li>
                <li class="icon-3">Напиши своего бота на&nbsp;любом удобном тебе языке&nbsp;программирования</li>
                <li class="icon-4">Тестируй своего бота на&nbsp;этом&nbsp;сайте</li>
                <li class="icon-5">В день турнира заходи на&nbsp;сайт для&nbsp;участия в&nbsp;сражении</li>
            </ul>
        </div>
    </c:when>
    <c:otherwise>
        <div class="opac-black-container outerMessage">
            <h2>Регистрация закрыта</h2>
            <c:url value="/" var="homeLink" />
            <a href="${homeLink}" class="black_button">Вернуться на главную страницу</a>
        </div>
    </c:otherwise>
</c:choose>