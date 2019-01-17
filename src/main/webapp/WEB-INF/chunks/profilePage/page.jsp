<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<form:form modelAttribute="profileForm" action="profile.html"
		method="POST" onsubmit="return validateData()" id="profile-form" cssClass="abys-branded bot-decorated">
		<h3><spring:message code="label.profile.profile"/></h3>
		<div class="form-row">
			<label><spring:message code="label.profile.userName"/>:</label>
			<form:input maxlength="30" path="userName" />
			<form:errors path="userName" cssClass="errors" />
		</div>
		<div class="form-row">
			<label><spring:message code="label.profile.email"/>:</label>
			<form:input maxlength="200" path="email" />@epam.com
			<form:errors path="email" cssClass="errors" />
		</div>
		<%--<div class="form-row">--%>
		<%--<label><spring:message code="label.profile.phone"/>:</label>--%>
			<%--<form:input maxlength="20" path="phone" />--%>
			<%--<span>Номер телефона нужен для обратной связи</span>--%>
			<%--<form:errors path="phone" cssClass="errors" />--%>
		<%--</div>--%>
		<div class="form-row">
			<label><spring:message code="label.profile.oldPassword"/>:</label>
			<form:password maxlength="30" path="oldPassword" />
			<form:errors path="oldPassword" cssClass="errors" />
		</div>
		<div class="form-row">
			<label><spring:message code="label.profile.newPassword"/>:</label>
			<form:password maxlength="30" path="newPassword" />
			<form:errors path="newPassword" cssClass="errors" />
		</div>
		<div class="form-row">
			<c:if test="${not empty requestScope.errorToken}">
				<span class = "errors"><spring:message code="${requestScope.errorToken}"></spring:message></span>
			</c:if>
			<label><spring:message code="label.profile.token"/>:</label><b class="red-bold">${requestScope.token}</b>
		</div>
		<div class="form-row">
			<a href = "generateToken.html"><spring:message code="label.profile.generateToken"/></a>
		</div>
		<div class="form-row right-align">
			<input type="submit" value="<spring:message code="label.profile.save"/>">
		</div>
		<div class="form-row">
			<c:if test="${not empty pageMessage}" >
				<spring:message code="${pageMessage}"></spring:message>
			</c:if>
		</div>
	</form:form>
	<div id="right-bar" class="opac-black-container">
		<h3>Статистика сыгранных сражений:</h3>
		<br />
		Сыграно сражений: ${tournamentsNumber}
		<br />
		Выиграно сражений: ${tournamentsWin}
	</div>
