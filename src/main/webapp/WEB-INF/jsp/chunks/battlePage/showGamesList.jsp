<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
    <c:set value="${pageContext.request.userPrincipal.principal.canCreateAGame()}" var="canCreate" />
    <c:set value='${pageContext.request.userPrincipal.principal.hasAnyRole("ROLE_ADMIN")}' var="isAdmin" />
    <div class="opac-black-container tournament_list_desc" >
        На этой странице ты можешь увидеть список созданных другими пользователями турниров, посмотреть уже начатые или создать новое сражение.
        <c:choose>
        <c:when test="${battleCreationEnabled}" >
            <c:if test="${canCreate}">
                <c:choose>
                <c:when test="${empty game}">
                    <div class="new_battle_desc">
                        Сразу после создания новое сражение будет доступно на этой странице. Ты можешь позвать своих друзей подключиться к нему, или просто подождать, пока остальные игроки зайдут в этот турнир.
                    </div>
                    <a href="/createGame.html" id="create_battle" class="black_button">Создать новое сражение</a>
                </c:when>
                <c:otherwise>
                    <div >
                        <i>Но чтобы создать новое сражение, необходимо покинуть тренировочный уровень.</i>
                    </div>
                </c:otherwise>
                </c:choose>
            </c:if>
        </c:when>
        <c:otherwise>
            <div >
                <i><spring:message code="label.showGames.gameCreationDisabled"/></i>
            </div>
        </c:otherwise>
        </c:choose>
    </div>
    <div id="gamesListContainer"></div>
    <script>
        var updateGameList = function() {
            $.ajax({
                url: "gameListAsync.html",
                type: "get"
            }).success(function( data ) {
                jQuery("#gamesListContainer").html(data);
            });
        }
        updateGameList();
        setInterval(updateGameList, 5000);
    </script>