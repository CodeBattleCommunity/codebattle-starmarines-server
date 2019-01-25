<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h2><spring:message code="label.showGames.header"/></h2>

<c:if test="${not empty requestScope.errorShowGames }">
	<span class="errors"><spring:message code="${requestScope.errorShowGames}"/></span>
</c:if>
<c:choose>
	<c:when test="${empty requestScope.games.entrySet()}">
		<spring:message code="label.showGames.noGamesAvailable"/>
	</c:when>
	<c:otherwise>
		<table id="gamesList">
			<thead>
				<tr>
					<th><spring:message code="label.showGames.gameName"/></th>
					<th><spring:message code="label.showGames.players"/></th>
					<th><spring:message code="label.showGames.action"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="game" items="${requestScope.games.entrySet()}">
					<tr>
						<td><a href="gameStatistics.html?id=${game.getValue().id}">${game.getValue().title}</a></td>
						<td>${game.getValue().getNumberOfPlayers()}</td>
						<td><c:if
								test="${pageContext.request.userPrincipal.principal.hasAnyRole('ROLE_USER')}">
								<c:choose>
									<c:when
										test="${ empty game.getValue().getUserById(pageContext.request.userPrincipal.principal.id)}">
										<a href="joinToGame.html?gameid=${game.getKey()}"><spring:message code="label.showGames.join"/></a>
									</c:when>
									<c:otherwise>
										<a href="leaveGame.html?gameid=${game.getKey()}"><spring:message code="label.showGames.leave"/></a>
									</c:otherwise>
								</c:choose>
							</c:if> <c:if test="${pageContext.request.userPrincipal.principal.hasAnyRole('ROLE_ADMIN')}">
								<a href="startGame.html?gameid=${game.getKey()}"><spring:message code="label.showGames.start"/></a>
							</c:if></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:otherwise>
</c:choose>

<div>
	<c:if test="${pageContext.request.userPrincipal.principal.hasAnyRole('ROLE_ADMIN')}">
		<a href="createGame.html"><spring:message code="label.showGames.createGame"/></a>
	</c:if>
	<br />
	<c:if test="${pageContext.request.userPrincipal.principal.hasAnyRole('ROLE_USER')}">
		<a href="trainingLevel.html"><spring:message code='label.showGames.trainingLevel'/></a>
	</c:if>
</div>