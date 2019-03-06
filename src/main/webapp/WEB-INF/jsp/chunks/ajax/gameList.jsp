<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:set value='${pageContext.request.userPrincipal.principal.hasAnyRole("ROLE_ADMIN")}' var="isAdmin" />

<c:choose>
    <c:when test="${empty games}">
        <div class="not-available-msg">
            <c:url value="/training.html" var="trainingLink" />
            Список турниров пуст. Ты можешь <c:if test="${canCreate}">создать новый турнир или</c:if> протестировать своего бота на <a href='${trainingLink}'>тренировке</a>.
        </div>
    </c:when>
    <c:otherwise>
        <div id="games-list">
            <c:forEach items="${games.values()}" var="g">
                <div class="game opac-black-container">
                    <div class="top_line">
                        <div class="author">Создатель игры: <c:out value="${g.creator.userName}" /> (<c:out value="${g.creator.email}" />)</div>
                        <div class="players_counter">Подключилось игроков: <c:out value="${g.gameObject.players.size()}" /></div>
                    </div>
                    <div class="game_title"><c:out value="${g.gameStatistics.title}" /></div>
                    <div class="game_description"><c:out value="${g.gameStatistics.description}" /></div>
                    <div class="game_controls">
                        <div class="game_buttons">
                            <c:if test="${!isAdmin}">
                                <c:choose>
                                    <c:when test="${not empty gameInfo}">
                                        <div class="error">Ты не можешь подключиться к новой игре, пока участвуешь в другой игре или тренировочном уровне.</div>
                                    </c:when>
                                    <c:when test="${g.gameObject.isFull()}">
                                        <div class="error">Игра набрала максимальное количество участников. Вход невозможен.</div>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="joinToGame.html?gameId=<c:out value='${g.gameStatistics.gameId}'/>" class="black_button"><spring:message code="label.showGames.join"/></a>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                            <c:if test="${isAdmin}">
                                <c:if test="${!g.gameObject.finished}"><a href="gameControl.html?gameId=<c:out value='${g.gameStatistics.gameId}'/>" class="black_button">Управление игрой</a></c:if>
                                <a href="gameBroadcast.html?gameId=<c:out value='${g.gameStatistics.gameId}'/>" class="black_button">Просмотр</a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>