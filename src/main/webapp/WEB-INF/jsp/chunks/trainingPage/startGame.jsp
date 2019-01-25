<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<form:form modelAttribute="createTrainingLevelForm" method="POST" cssClass="abys-branded bot-decorated" id="training-form">
		<div class="form-row">
			<label><spring:message code="label.trainingLevel.botsCount"/>:</label>
			<form:select path="botsCount">
				<c:forEach begin="1" end="${maxTrainingBots}" step = "1" varStatus="loop">
					<form:option value="${loop.index}">${loop.index} </form:option>
				</c:forEach>
			</form:select>
			<form:errors path="botsCount" cssClass="errors"/>
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
		<div class="form-row">
			<input type="submit" class="submit" value="<spring:message code="label.trainingLevel.submit"/>" />
		</div>
	</form:form>
	<div id="disclaimer" class="opac-black-container">Тестируй своего бота в сражении с туповатыми роботами сайта. Ты можешь выбрать несколько вариантов расположения планет и поведения роботов сервера. Но помни, что на турнире тебе будут противостоять хитроумные боты, разработанные твоими противниками! Будь хитрее, коварнее, умнее!</div>
	<div id="right-bar" class="opac-black-container">
		<ul>
			<li class="icon-1">Используй этот код для инициализации бота: <span class="token">${token}</span></li>
			<li class="icon-2">Запусти своего бота</li>
			<li class="icon-3">Нажми на кнопку "Начать игру"</li>
		</ul>
	</div>