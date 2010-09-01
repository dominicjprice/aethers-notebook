<!-- Initializes the map --->
function initialize() {
  var centerLatLng = new gm.LatLng(52.9510063, -1.18332495);
  map = new gm.Map(document.getElementById('map-canvas'), {
				zoom: 15,
				center: centerLatLng,
      			mapTypeId: gm.MapTypeId.ROADMAP
  });
    
  drawInit();	
}