<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div id="loginContainer">
	<h2><spring:message code="label.login.header"/></h2>
	<form:form modelAttribute="loginForm" action = "login.html" method="POST" accept-charset="UTF-8"	onsubmit="return validateLogin();">
		<div class="row">
			<label class="login"><spring:message code="label.login.login"/></label>
			<form:input maxlength="30" cssClass="field login" id="usrName" path="userName" />
			<form:errors path="userName" cssClass="errors" />
		</div>
		<div class="row">
			<label class="login"><spring:message code="label.login.password"/></label>
			<form:password maxlength="30" cssClass="field login" id="passwd" path="password" />
			<form:errors path="password" cssClass="errors" />
		</div>
		<div class="row">
			<a href="signUp.html"><spring:message code="label.login.signUp"/></a>
			<input type="submit" class="float-right" value = "<spring:message code="label.login.signIn"/>"/>
		</div>
	</form:form>
</div>