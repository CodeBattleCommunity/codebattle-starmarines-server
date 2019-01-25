<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script>var gameId = ${game.id}</script>


<div class = "gameStatusMessage">
	<span id="notStarted"><spring:message code="label.currentGame.notStarted" /></span>
	<span id="finished">
		<spring:message code="label.currentGame.finished" />
	</span>
    <span id ="winner"> <spring:message code="label.currentGame.winner" /></span>
    <span id="winnerName"></span>
</div>
<div id = "container"></div>
<c:choose>
	<c:when test="${not empty game.players}">
		<h2>Участники игры (<c:out value="${game.players.size()}" />)</h2>
		<ul>
		<c:forEach items="${game.players}" var="player">
			<c:url var="deletePlayerLink" value="deletePlayer.html?gameId=${game.id}&playerId=${player.id}" />
			<li><c:out value="${player.login}"/> <a href="${deletePlayerLink}">Удалить из игры</a></li>
		</c:forEach>
		</ul>
	</c:when>
	<c:otherwise>
		<h2>В этой игре пока нет участников.</h2>
	</c:otherwise>
</c:choose>

<br />
<c:choose>
	<c:when test="${game.players.size() ge 2}">
		<c:if test="${game.started ne true}">
			<h3><a href="startGame.html?gameId=${game.id}"><spring:message code="label.showGames.start"/></a></h3>
		</c:if>
	</c:when>
	<c:otherwise>
		<p>Начать игру можно только после подключения двух и более игроков.</p>
	</c:otherwise>
</c:choose>
<br />
<h3><a href="deleteGame.html?gameId=${game.id}">Удалить игру</a></h3>
