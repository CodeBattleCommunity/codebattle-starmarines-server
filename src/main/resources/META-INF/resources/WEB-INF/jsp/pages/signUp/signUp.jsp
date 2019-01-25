<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div id="signUpContainer">
	<h2><spring:message code="label.signUp.header"/></h2>
	<form:form modelAttribute="signUpForm" action="signUp.html" method="POST"
		onsubmit="return validateData();">
		<div class="row">
			<label class="signUp"><spring:message code="label.signUp.userName"/>:</label>
			<form:input maxlength="30" cssClass="signUp" path="name" />
			<form:errors path="name" cssClass="errors" />
		</div>
		<div class="row">
			<label class="signUp"><spring:message code="label.signUp.login"/>:</label>
			<form:input maxlength="30" cssClass="signUp" path="login" />
			<form:errors path="login" cssClass="errors" />
		</div>
		<div class="row">
			<label class="signUp"><spring:message code="label.signUp.password"/>:</label>
			<form:password maxlength="30" cssClass="signUp" path="password" />
			<form:errors path="password" cssClass="errors" />
		</div>
		<div class="row">
			<label class="signUp"><spring:message code="label.signUp.repassword"/>:</label>
			<form:password maxlength="30" cssClass="signUp" path="repassword" />
			<form:errors path="repassword" cssClass="errors" />
		</div>
		<div class="row">
			<input class="button float-right" type="submit" value="<spring:message code="label.signUp.signUp"/>" /> <input
				class="button float-right" type="reset" value="<spring:message code="label.signUp.reset"/>" onclick="clearData()" />
		</div>
	</form:form>
</div>