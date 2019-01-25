<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<c:choose>
	<c:when test="${empty game or game.type eq 'TRAINING_LEVEL' or pageContext.request.userPrincipal.principal.hasAnyRole('ROLE_ADMIN')}">
		<jsp:include page="showGamesList.jsp" />
	</c:when>
	<c:otherwise>
		<jsp:include page="showGame.jsp" />
	</c:otherwise>
</c:choose>