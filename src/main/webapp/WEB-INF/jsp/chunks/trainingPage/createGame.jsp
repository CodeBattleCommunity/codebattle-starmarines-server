<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<form action="" class="abys-branded bot-decorated" id="training-form">
	<div class="form-row">
	<h2>Тестовая игра</h2>
	<p>Чтобы создать тренировочный уровень, нажми на кнопку
	</div>
	<div class="form-row center-align">
		<input type="submit" value="<spring:message code="label.trainingLevel.submit"/>" />
	</div>
</form>
<div id="disclaimer" class="black-plated">Тестируй своего бота в сражении с туповатыми роботами сайта. Ты можешь выбрать несколько вариантов расположения планет и поведения роботов сервера. Но помни, что на турнире тебе будут противостоять хитроумные боты, разработанные твоими противниками! Будь хитрее, коварнее, умнее!</div>
<div id="right-bar" class="black-plated">
	<ul>
		<li class="icon-1">Создай тренировочный уровень</li>
		<li class="icon-1">Выбери противников</li>
		<li class="icon-1">Используй этот код для инициализации своего бота: <span class="token">${token}<span></li>
		<li class="icon-2">Запусти своего бота</li>
		<li class="icon-3">Начни игру</li>
	</ul>
</div>