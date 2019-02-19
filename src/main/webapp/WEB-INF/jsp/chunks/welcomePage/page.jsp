<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>


<div class="left-bar opac-black-container">
	<h2>Не так чтобы очень скоро буквально в соседней галактике...</h2>

	<p><b>2389 год.</b>
		Гигантская корпорация «ABYSS», созданная в начале 22 века путём слияния Apple, BMW, Yandex, Sony и Shell,
		выпустила на рынок вооружений суперновинку — боевого меха «Destro».
		После того, как стальные воины были взяты на вооружение США, Россией, Паназиатской коалицией и Евросоюзом, у
		меньших стран просто не осталось выбора.
		У многих он исчез вместе с независимостью...
	</p>

	<p>
		Искусно введённые в заблуждение правительства слишком поздно осознали, что «ABYSS» всем им продала одну и ту же
		разработку в одно и то же время.
		Рынок оружия рухнул в одночасье, а «ABYSS» превратилась в организацию, способную диктовать свои условия всему
		миру.
	</p>

	<p>
		Технический прогресс не мог только одного - изменить человеческую сущность. Раздираемые противоречиями
		государства продолжали бороться между собой.
	</p>

	<p>
		Ослабевание контроля вооружений со стороны ООН привело к началу войны между Китаем и Японией из-за спорного
		участка Тихого океана.
		Ситуацией не преминули воспользоваться Американские Штаты, давно состоящие в союзе с Японией...
		Через год война стала Мировой.
	</p>

	<div class="right-align"><a href="info.html">Читать далее...</a></div>
</div>
<div class="right-bar opac-black-container">

	<h2 class="yellow-header">Приглашаем принять участие в турнире боевых ботов «Hardcoded StarMarines», который пройдёт в
		рамках CodeBattle for Students.
	</h2>
	<br/>

	<div class="login-ribbon">
		<c:choose>
			<c:when test="${pageContext.request.userPrincipal.principal.hasAnyRole('ROLE_USER')}">
				<c:url var="docLink" value="/documentation.html"/>
				<a href="${docLink}" id="login-pivot" class="icon-dot">Войти</a>
			</c:when>
			<c:otherwise>
                <c:if test="${registrationIsOpen}">
				    <a href="signUp.html" class="icon-triangle">Зарегистрироваться и подготовиться к игре</a>
                </c:if>
				<a href="#login-pivot" id="login-pivot" class="icon-dot" onclick="toggleLoginForm()">Войти</a>
			</c:otherwise>
		</c:choose>
		<form:form modelAttribute="loginForm" action="login.html" method="POST" accept-charset="UTF-8" id="login-form"
		           class="abys-branded">
			<div class="form-row">
				<label><spring:message code="label.login.login"/></label>
				<form:input maxlength="30" id="usrName" path="userName" name="usrName"/>
				<form:errors path="userName" cssClass="errors"/>
			</div>
			<div class="form-row">
				<label><spring:message code="label.login.password"/></label>
				<form:password maxlength="30" id="passwd" path="password" name="passwd"/>
				<c:if test="${param.error != null}">
					<span class="errors">Введен неверный логин или пароль</span>
				</c:if>
				<form:errors path="password" cssClass="errors"/>
			</div>
			<div class="form-row">
				<input type="submit" class="submit" value="<spring:message code="label.login.signIn"/>"/>
			</div>
		</form:form>
	</div>
</div>