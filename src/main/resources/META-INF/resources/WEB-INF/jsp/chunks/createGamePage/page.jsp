<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<form:form modelAttribute="createGameForm" method="POST" cssClass="abys-branded bot-decorated" id="starting-form">
		<div class="form-row">
			<h2>Создание турнира</h2>
		</div>
		<div class="form-row">
			<label><spring:message code="label.createGame.title"/>:</label>
			<form:input maxlength="200" path="title" />
			<form:errors path="title" cssClass="errors" />
		</div>
		<div class="form-row">
			<label class="createGame"><spring:message code="label.createGame.description"/>:</label>
			<form:textarea path="description"/>
			<form:errors path="description" cssClass="errors" />
		</div>
		<div class="form-row">
			<label><spring:message code="label.trainingLevel.type"/>:</label>
			<form:select path="type">
			<c:forEach items="${levelTypes}" var="type" varStatus="loop">
				<form:option value="${type}"><spring:message code="label.trainingLevel.type.${loop.index + 1}" /></form:option>
			</c:forEach>
			</form:select>
			<form:errors path="type" cssClass="errors"/>
		</div>
		<div class="form-row center-align">
			<input type="submit" value="Создать" />
		</div>
	</form:form>