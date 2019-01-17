<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<h2><spring:message code="label.profile.header"/></h2>

	<form:form modelAttribute="profileForm" action="profile.html"
		method="POST" onsubmit="return validateData()">
		<h3><spring:message code="label.profile.yourId"/>: ${pageContext.request.userPrincipal.principal.id}</h3>
		<div class="row">
			<label class="profile"><spring:message code="label.profile.userName"/>:</label>
			<form:input maxlength="30" cssClass="field common" path="userName" />
			<input class="button float-right" type="submit" value="<spring:message code="label.profile.save"/>"></input>
			<form:errors path="userName" cssClass="errors" />
		</div>
		<div class="row">
			<c:if test="${not empty requestScope.errorToken}">
				<span class = "errors"><spring:message code="${requestScope.errorToken}"></spring:message></span>
			</c:if>
			<label class="profile"><spring:message code="label.profile.token"/>:</label> ${requestScope.token}
		</div>
		<a href = "generateToken.html"><spring:message code="label.profile.generateToken"/></a>
		<c:if test="${pageContext.request.userPrincipal.principal.hasAnyRole('ROLE_USER')}">
			<h3><spring:message code="label.profile.gameStatistics"/></h3>
			<div class="row">
				<label class="profile"><spring:message code="label.profile.playedGames"/>:</label> ${requestScope.playedGames}
			</div>
			<div class="row">
				<label class="profile"><spring:message code="label.profile.wonGames"/>:</label> ${requestScope.wonGames}
			</div>
		</c:if>
	</form:form>