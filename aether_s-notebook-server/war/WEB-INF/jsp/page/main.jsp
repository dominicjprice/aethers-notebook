<%@ page isELIgnored="false" contentType="text/html" pageEncoding="windows-1252"%>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
	<head>
		<link rel="stylesheet" type="text/css" href="/css/styles.css" />
		
		<title>Aether's notebook</title>
		
	</head>
	
	<body align="center">
		<div style="text-align:center;border:2px solid #4750FF;background-color:#E0E2FF;position:relative;margin-left:auto;margin-right:auto;margin-top:30px;padding-top:10px;">
			<h2>List of available maps</h2>
			<h3>
				<div><a href="/a/map/dataConnectionState">Data connection state</a> </div>
				<div><a href="/a/map/serviceState">Service state</a> </div>
				<div><a href="/a/map/signalStrength">Signal strength</a> </div>
				<div><a href="/a/map/telephonyState">Telephony state</a> </div>
			</h3>			
			<h2>Upload log files:</h2>
			<div>
				<form action='/a/crowd/' method='POST'>
					<div>Compressed log file: <input type='file' name='uploadedfile'></div>
					<input type='submit' value='Go'>
				</form>
				<form>
					<div>Uncompressed log file: <input type='file' name='file-uncompressed'></div>
					<input type='submit' value='Go'>
				</form>
			</div>
		</div>
	</body>
</html>