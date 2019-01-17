<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><tiles:insertAttribute name="title" ignore="true" /></title>
		<tiles:insertAttribute name="commonHeadImports"/>
		<tiles:insertAttribute name="currentHeadImports"/>
	</head>
	<body>
		<div class="wrap">
		    <div class="menu">
		    	<tiles:insertAttribute name="menu" />
		    </div>
			<div class="header">
				<tiles:insertAttribute name="header" />
			</div>
			<div class="content">
				<div class="userBar right-align">
			 	   <tiles:insertAttribute name="userBar" />
				</div>
			    <tiles:insertAttribute name="content" />
		 	</div>
		 	<div class="footer">
			    <tiles:insertAttribute name="footer" />
			</div>
		</div>
	</body>
</html>