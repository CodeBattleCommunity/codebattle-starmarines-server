<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<form:form action="admin/default" method="POST" >
    <input class="button float-right" type="submit" value="<spring:message code="label.admin.settings.restore"/>" />
</form:form>

<br/>

<form:form modelAttribute="settings" action="admin" method="POST" >
    <c:forEach var="e" items="${settings.opts}">
        <div class="row">
            <label class="signUp"><c:out value="${e.key}"  />:</label>
            <form:input maxlength="30" cssClass="float-right" path="opts['${e.key}']"/>
            <br/>
            <c:set var="descKey" value="${e.key}" />
            <i><c:out value="${settings.descriptions[descKey]}" /></i>
        </div>
        <br/>
    </c:forEach>
    <input class="button float-right" type="submit" value="<spring:message code="label.admin.settings.apply"/>" />
</form:form>
