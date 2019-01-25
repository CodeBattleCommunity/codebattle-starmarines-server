<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<c:choose>
	<c:when test="${empty game}">
		<jsp:include page="startGame.jsp"></jsp:include>
	</c:when>
	<c:when test="${game.started eq true and game.type eq 'TRAINING_LEVEL' }">
		<jsp:include page="showGame.jsp"></jsp:include>
	</c:when>
	<c:otherwise>
		<jsp:include page="showError.jsp"></jsp:include>
	</c:otherwise>
</c:choose>