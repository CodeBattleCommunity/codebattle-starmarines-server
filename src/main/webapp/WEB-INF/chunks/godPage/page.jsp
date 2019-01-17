<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<form:form modelAttribute="multipleLoginForm">
	<fieldset>
		<c:out value="${pageMessage}"/><br>
		<form:input path="logins"/>
		<form:input class="pseudoHidden" path="idGame"/>
		<input type="submit" value="Заджойнить всех" />
		<br />
		<a href="/battle.html">&lt;&lt; вернуться к битвам</a>
	</fieldset>
</form:form>