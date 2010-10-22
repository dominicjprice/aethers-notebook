<%@ page isELIgnored="false" contentType="text/html" pageEncoding="windows-1252"%>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Telephony State map</title>

<link rel="stylesheet" type="text/css" href="/css/styles.css" />

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="/js/drawing.js"</script>
<script type="text/javascript" src="/js/utils.js"</script>

<script type="text/javascript">

  gm = google.maps

  var circlesArr = [];
  var cursorStr;

  function grabSelectedTypes() {
	var selectedTypes = [];
	var nts = document.forms[0].networkType;
	for (var i=0; i<nts.length; i++) {
		if (nts[i].checked) {
			selectedTypes[selectedTypes.length] = nts[i].value;
		}
	}

	return selectedTypes;  
  }
  
  function drawInit() {
	var selectedTypes = grabSelectedTypes();

	// start ajax requests
	$.ajax({
		url : '/a/map/drawTelephonyState/',
     	dataType : 'script',
     	data : 'selectedTypes=' + selectedTypes 
	});   	
  }
  
  function drawMore() {
  	var selectedTypes = grabSelectedTypes();
  	
    if (cursorStr != 'null') {
		$.ajax({
    		url : '/a/map/drawMoreTelephonyState/' + cursorStr,
     		dataType : 'script',
     		data : 'selectedTypes=' + selectedTypes 
     	}); 
   	}  
  }
  
  function redrawMap() {
	clearCircles();
	drawInit();
  }
  
</script>

</head>


<body>
	<div id='header'>
		<h1 align='center'>Telephony State Map</h1>
		<div>
			<form>
				Network type: <br />
				<input type="checkbox" name="networkType" value="NETWORK_TYPE_EDGE" checked="true" /> <b><font color="#00BC16">Edge</b></font>
				<input type="checkbox" name="networkType" value="NETWORK_TYPE_GPRS" checked="true" /> <b><font color="#0026FF">GPRS</b></font> 
				<input type="checkbox" name="networkType" value="NETWORK_TYPE_UMTS" checked="true" /> <b><font color="#FF0000">UMTS</b></font>
				<input type="checkbox" name="networkType" value="NETWORK_TYPE_UNKNOWN" checked="true" /> <b><font color="#FFD800">Unknown</b></font>
				<br />
				<br />
				<input type="button" value ="Redraw map" onclick="redrawMap()" />				
			</form>
		</div>
	</div>

	<div id='map-canvas'></div>
	
	<div id='footer' style='display:none;'></div>	
	
</body>

</html>