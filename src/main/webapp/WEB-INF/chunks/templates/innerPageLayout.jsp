<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<tiles:insertAttribute name="commonHeadImports"/>
<tiles:insertAttribute name="currentHeadImports"/>
<title><tiles:insertAttribute name="title"/></title>
</head>
<body>
	<tiles:insertAttribute name="header"/>
	<div class="menu-ribbon">
		<tiles:insertAttribute name="menu" />
	</div>
	<div class="wrapper">
        <c:if test="${not empty errorMessage}">
            <div class="errorMessage"><c:out value="${errorMessage}" /></div>
        </c:if>
		<tiles:insertAttribute name="content"/>
		<tiles:insertAttribute name="footer"/>
	</div>
</body>
</html>