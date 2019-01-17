<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<h2><spring:message code="label.currentGame.header"/></h2>
<c:choose>
	<c:when test="${not empty requestScope.game}">
		<a href="leaveGame.html?gameid=${requestScope.game.getId()}"><spring:message code="label.currentGame.leaveGame"/></a>
		<spring:message code="label.currentGame.gameId" />: <label id = "gameId">${requestScope.game.getId()}</label>
	</c:when>
	<c:otherwise>
		<spring:message code="label.currentGame.noGame"/>
	</c:otherwise>
</c:choose>
<div id = "container"></div>
<div class = "gameStatusMessage">
	<span id="notStarted"><spring:message code="label.currentGame.notStarted" /></span>
	<span id="finished">
		<spring:message code="label.currentGame.finished" />
		<c:if test="${requestScope.game.type ne 'TRAINING_LEVEL'}">
			<spring:message code="label.currentGame.seeStatistics" />
			<a href="${pageContext.request.contextPath}/gameStatistics.html?id=${requestScope.game.getId()}"><spring:message code="label.currentGame.statisticsPage" /></a>
		</c:if>
	</span>
</div>