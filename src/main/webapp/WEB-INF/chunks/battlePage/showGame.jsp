<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:set var="isCreator" value="${pageContext.request.userPrincipal.principal.canControlGame(gameInfo.gameStatistics)}" />
<a id="leaveGame" href="leaveGame.html?gameId=${game.id}"><spring:message code="label.currentGame.leaveGame"/></a>
<script>
    var gameId = ${game.id};
    try {
        startGameListener();
    } catch (a) {

    }
</script>
<div class = "gameStatusMessage">
	<span id="notStarted"><spring:message code="label.currentGame.notStarted" /></span>
	<span id="finished">
		<spring:message code="label.currentGame.finished" />
	</span>
    <span id ="winner"> <spring:message code="label.currentGame.winner" /></span>
    <span id="winnerName"></span>
</div>
    <div id = "container">
	    <canvas id="canv" width="512" height="512" style="position: absolute; z-index: 1; clear:both; top: 0px;"></canvas>
    </div>
	<div id = "viewport"></div>
<div id="beforeStartTableContainer"></div>
