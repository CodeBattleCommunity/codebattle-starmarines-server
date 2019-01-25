<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<h2><spring:message code="label.statistics.current.header"/></h2>

<h3><spring:message code="label.statistics.current.game.title"/>: ${requestScope.gameStatistics.title}</h3>

	<div class="row">
		<label class="statistics"><spring:message code="label.statistics.game.description"/>:</label>
		<span>${requestScope.gameStatistics.description}</span>
	</div>
	<div class="row">
		<label class="statistics"><spring:message code="label.statistics.game.type"/>:</label>
		<span>${requestScope.gameStatistics.type}</span>
	</div>
	<div class="row">
			<label class="statistics"><spring:message code="label.statistics.game.state"/>:</label>
			<span>${requestScope.gameStatistics.state}</span>
		</div>
	<c:if test="${requestScope.gameStatistics.state eq 'FINISHED'}">
		<div class="row">
			<label class="statistics"><spring:message code="label.statistics.game.winner"/>:</label>
			<span>${requestScope.gameWinner.userName}</span>
		</div>
		<div class="row">
			<label class="statistics"><spring:message code="label.statistics.game.turns.number"/>:</label>
			<span>${requestScope.gameStatistics.numberOfTurns}</span>
		</div>
		<div class="row">
			<label class="statistics"><spring:message code="label.statistics.game.log"/>:</label>
			<span>${requestScope.gameStatistics.logPath}</span>
		</div>
		
	</c:if>