<%@ page isELIgnored="false" contentType="text/html" pageEncoding="windows-1252"%>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="/css/styles.css" />
		<title>Aether's notebook</title>
	</head>
	
	<body align="center">
		${message}
		<div style="text-align:center;border:2px solid #4750FF;background-color:#E0E2FF;position:relative;margin-left:auto;margin-right:auto;margin-top:30px;padding-top:10px;">
			<h2>Data:</h2>
			<div>${stats}</div>
		</div>
	</body>
</html>