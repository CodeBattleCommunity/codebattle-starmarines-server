<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:if test="${not empty gameInfo.gameObject}">
        <c:set var="isCreator" value="${pageContext.request.userPrincipal.principal.canControlGame(gameInfo.gameStatistics)}" />
        <div id="beforeStartTable">
            <div class="ribbon">
                <h4>Игра: <c:out value="${gameInfo.gameStatistics.title}" /></h4>
                <c:set value="#{gameInfo.gameObject.players.size()}" var="playersCount" />
                <span class="playersCount">Количество игроков: ${playersCount}</span>
            </div>
            <table class="playersTable">
                <thead>
                <th>
                    Имя бота
                </th>
                <th>
                    Пользователь
                </th>
                <th>
                    Бот подключен
                </th>
                <c:if test="${isCreator}">
                    <th>
                        Управление
                    </th>
                </c:if>
                </thead>
                <tbody>
                <c:forEach items="${gameInfo.gameObject.players}" var="player">
                    <tr>
                        <td>
                            ${player.userName}
                        </td>

                        <td>
                            ${player.email}
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${requestHandler.isConnected(gameInfo.gameObject, player.id)}">
                                    ДА
                                </c:when>
                                <c:otherwise>
                                    НЕТ
                                </c:otherwise>
                            </c:choose>
                        </td>
                    <c:if test="${isCreator}">
                        <td>
                            <c:if test="${pageContext.request.userPrincipal.principal.id ne player.id}">
                                <c:url var="deletePlayerLink" value="deletePlayer.html">
                                    <c:param name="playerId" value="${player.id}" />
                                    <c:param name="gameId" value="${gameInfo.gameObject.id}" />
                                </c:url>
                                <a href="${deletePlayerLink}">Удалить</a>
                            </c:if>
                        </td>
                    </c:if>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${isCreator && playersCount gt 1}">
                <div class="ribbon">
                    <c:url var="startUrl" value="/startGame.html">
                        <c:param name="gameId" value="${gameInfo.gameObject.id}" />
                    </c:url>
                    <a class="black_button" href="${startUrl}">Начать игру!</a>
                </div>
            </c:if>
        </div>
    </c:if>