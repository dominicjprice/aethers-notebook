<%@ page isELIgnored="false" contentType="text/html" pageEncoding="windows-1252"%>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Service State map</title>

<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

<link rel="stylesheet" type="text/css" href="/css/styles.css" />

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="/js/drawing.js"</script>
<script type="text/javascript" src="/js/utils.js"</script>

<script type="text/javascript">

  gm = google.maps

  var circlesArr = [];
  var cursorStr;
  
  function grabOperator() {
	return document.forms[0].txtOperator.value;
  }
  
  function grabRoaming() {
	var list = document.forms[0].lstRoaming;
	var roaming = "1";
	for (i=0; i<list.length; i++) {
		if (list[i].selected) {
			roaming = list[i].value;
		}
	}
	
	return roaming;
  }
  
  function drawInit() {
	var roaming = grabRoaming();
	var operator = grabOperator();

	// start ajax requests
	$.ajax({
		url : '/a/map/drawServiceState/',
     	dataType : 'script',
     	data : 'roaming=' + roaming + '&operator=' + operator 
	});   	
  }
  
  function drawMore() {
	var roaming = grabRoaming();
	var operator = grabOperator();

    if (cursorStr != 'null') {
		$.ajax({
    		url : '/a/map/drawMoreServiceState/' + cursorStr,
     		dataType : 'script',
     		data : 'roaming=' + roaming + '&operator=' + operator 
     	}); 
   	}  
  }
  
  function redrawMap() {
	clearCircles();
	drawInit();
  }
  
</script>

</head>


<body onload="initialize();">
	<table align="center" width="1000">
		<tr align="center"><td>
			<h1> Service State Map </h1>
		</tr></td>
		
		<tr><td>
			<form>
				Operator: 
				<input name="txtOperator" /> <br/>
				
				Roaming: 
				<select name="lstRoaming" id="lstRoaming">
					<option value="1">Doesn't matter</option>
					<option value="2">Must be on</option>
					<option value="3">Must be off</option>
				</select>
				
				<br /><br />
				<input type="button" value ="Redraw map" onclick="redrawMap()" />				
			</form>
		</td></tr>
		
		<tr><td>
			<br /> <br />
			<div align="center" id="map-canvas" style="width:1100px; height:600px"></div>
		</td></tr>
		
	</table>
	
</body>

</html>