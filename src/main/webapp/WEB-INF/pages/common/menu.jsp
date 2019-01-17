<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<ul>
	<li class="item"><a href="profile.html"><spring:message code="label.menu.profile"/></a></li>
	<li class="item"><a href="showGames.html"><spring:message code="label.menu.viewGames"/></a></li>
	<c:if test="${pageContext.request.userPrincipal.principal.hasAnyRole('ROLE_USER')}">
		<li class="item"><a href="currentGame.html"><spring:message code="label.menu.viewCurrentGame"/></a></li>
	</c:if>
	<li class="item"><a href="statistics.html"><spring:message code="label.menu.statistics"/></a></li>
	<li class="item"><a href="information.html"><spring:message code="label.menu.information"/></a></li>
	<li class="item"><a href="history.html"><spring:message code="label.menu.history"/></a></li>
</ul>