<%@ page isELIgnored="false" contentType="text/html" pageEncoding="windows-1252"%>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Data Connection State Map</title>
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
  
  
  function drawInit() {
	// start ajax requests
	$.ajax({
		url : '/a/map/drawDataConnectionState/',
     	dataType : 'script'
	});     	
  }
         
  function drawMore() {
    if (cursorStr != 'null') {
		$.ajax({
    		url : '/a/map/drawMoreDataConnectionState/' + cursorStr,
     		dataType : 'script'
     	}); 
   	}
  }       


</script>

</head>

<body onload="initialize();">

	<table align="center" width="1000">
	
		<tr align="center"><td>
			<h1>Data Connection State Map</h1>
		</tr></td>
				
		<tr><td>
			<br />
			<div align="center" id="map-canvas" style="width:1100px; height:600px"></div>
		</td></tr>
		
	</table>
	
</body>

</html>