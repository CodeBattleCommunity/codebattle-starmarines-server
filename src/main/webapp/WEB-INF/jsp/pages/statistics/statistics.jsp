<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<h2>
	<spring:message code="label.statistics.header" />
</h2>
<c:choose>
	<c:when test="${not empty requestScope.statistics}">
		<table id="statistics">
			<thead>
				<tr>
					<th><spring:message code="label.statistics.gameId" /></th>
					<th><spring:message code="label.statistics.title" /></th>
					<th><spring:message code="label.statistics.type" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="gameStatistics" items="${requestScope.statistics}">
					<tr>
						<td>${gameStatistics.getGameId()}</td>
						<td><a
							href="gameStatistics.html?id=${gameStatistics.getGameId()}">${gameStatistics.getTitle()}</a></td>
						<td>${gameStatistics.getType()}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<spring:message code="label.statistics.noStatistics"/>
	</c:otherwise>
</c:choose>