<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<c:url var="linkProfile" value="/profile.html" />
	<c:url var="linkProfileTitle" value="label.menu.profile" />
	<c:url var="linkTraining" value="/training.html" />
	<c:url var="linkTrainingTitle" value="label.menu.training" />
	<c:url var="linkBattle" value="/battle.html" />
	<c:url var="linkBattleTitle" value="label.menu.battle" />
	<c:url var="linkDocumentation" value="/documentation.html" />
	<c:url var="linkDocumentationTitle" value="label.menu.documentation" />
	<c:url var="linkStatistics" value="/gamesStatistics.html"/>
	<c:url var="linkStatisticsTitle" value="label.menu.statistics" />
	<c:url var="logoutUrl" value="/logout.html" />

	<c:set var="currentPageAddress" value="${requestScope.get('javax.servlet.forward.servlet_path')}" />
	
<div class="menu-wrap">
    <div class="menu">
            <a class="item ${fn:endsWith(linkProfile, currentPageAddress) ? 'active' : ''}" href="${linkProfile}"><spring:message code="${linkProfileTitle}" /></a>
            <a class="item ${fn:endsWith(linkTraining, currentPageAddress) ? 'active' : ''}" href="${linkTraining}"><spring:message code="${linkTrainingTitle}" /></a>
            <a class="item ${fn:endsWith(linkBattle, currentPageAddress) ? 'active' : ''}" href="${linkBattle}"><spring:message code="${linkBattleTitle}" /></a>
	    <a class="item ${fn:endsWith(linkStatistics, currentPageAddress) ? 'active' : ''}" href="${linkStatistics}"><spring:message code="${linkStatisticsTitle}" /></a>
	<span id="countdown"> Турнир начнется через: </span>
    <div id="doclink">
        <a class="item icon-abys ${fn:endsWith(linkDocumentation, currentPageAddress) ? 'active' : ''}" href="${linkDocumentation}"><spring:message code="${linkDocumentationTitle}" /></a>
        <a class="item" href="/logout.html"><spring:message code="label.menu.logout"/></a>
        </div>
    </div>

</div>