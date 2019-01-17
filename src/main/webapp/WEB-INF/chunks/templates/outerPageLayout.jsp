<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
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
	<div class="wrapper">
		<tiles:insertAttribute name="tournamentCountdown"/>
		<tiles:insertAttribute name="header"/>
		<tiles:insertAttribute name="content"/>
		<tiles:insertAttribute name="footer"/>
	</div>
</body>
</html>