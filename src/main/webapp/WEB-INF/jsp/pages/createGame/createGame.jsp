<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div id="createGameContainer">
	<h2><spring:message code="label.createGame.header"/></h2>
	<form:form modelAttribute="createGameForm" action="createGame.html" method="POST"
		onsubmit="return validateData();">
		<div class="row">
			<label class="createGame"><spring:message code="label.createGame.title"/>:</label>
			<form:input maxlength="200" cssClass="createGame" path="title" />
			<form:errors path="title" cssClass="errors" />
		</div>
		<div class="row">
			<label class="createGame"><spring:message code="label.createGame.description"/>:</label>
			<form:input cssClass="createGame" path="description" />
			<form:errors path="description" cssClass="errors" />
		</div>
		<div class="row">
			<input class="button float-right" type="submit" value="<spring:message code="label.createGame.createGame"/>" /> <input
				class="button float-right" type="reset" value="<spring:message code="label.createGame.reset"/>" onclick="clearData()" />
		</div>
	</form:form>
</div>