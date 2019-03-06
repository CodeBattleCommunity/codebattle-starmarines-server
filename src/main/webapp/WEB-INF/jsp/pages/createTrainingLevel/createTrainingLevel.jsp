<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div id="trainingLevelContainer">
	<h2><spring:message code="label.trainingLevel.header"/></h2>
	<form:form modelAttribute="createTrainingLevelForm" method="POST" >
		<div class="row">
			<spring:message code="text.trainingLevel.get-ready" />
		</div>
		<div class="row">
			<label class="trainingLevel"><spring:message code="label.trainingLevel.botsCount"/>:</label>
			<form:select path="botsCount" cssClass="trainingLevel">
				<c:forEach begin="1" end="${maxTrainingBots}" step = "1" varStatus="loop">
					<form:option value="${loop.index}">${loop.index} </form:option>
				</c:forEach>
			</form:select>
			<form:errors path="botsCount" cssClass="errors"/>
		</div>
		<div class="row">
			<label class="trainingLevel"><spring:message code="label.trainingLevel.type"/>:</label>
			<form:select path="type" cssClass="trainingLevel">
			<c:forEach items="${levelTypes}" var="type" varStatus="loop">
				<form:option value="${type}"><spring:message code="label.trainingLevel.type.${loop.index + 1}" /></form:option>
			</c:forEach>
			</form:select>
			<form:errors path="type" cssClass="errors"/>
		</div>
		<div class="row">
			<input class="button float-right" type="submit" value="<spring:message code="label.trainingLevel.submit"/>" />
			<form:input path="gameId" cssClass="pseudoHidden"/>
		</div>
	</form:form>
</div>