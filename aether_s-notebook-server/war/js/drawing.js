<!-- Clears all circles on map -->
function clearCircles() {
  for (var i=0; i<this.circlesArr.length; i++) {
	this.circlesArr[i].setMap(null);
  }
}

<!-- Draws a circle with center (x, y) on the given map -->
function drawCircle(map, x, y, accuracy, colour) {
  var centerLatLng = new gm.LatLng(x, y);
  circleOptions = { 
	center: centerLatLng, 
	clickable: false, 
	fillColor: colour,
	fillOpacity: 0.50, 
	map: map, 
	radius: accuracy, 
	strokeColor: colour, 
	strokeOpacity: 1,
	strokeWeight: 1 
  }; 

  circle = new gm.Circle(circleOptions); 		
  this.circlesArr[this.circlesArr.length] = circle;
}