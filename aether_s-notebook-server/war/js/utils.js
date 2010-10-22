<!-- Initializes the map --->
//Use the jquery $() function to set up script to run on loadComplete of the page...
$(function() {
  var centerLatLng = new gm.LatLng(52.503794, -4.048757);
  map = new gm.Map(document.getElementById('map-canvas'), {
				zoom: 14,
				center: centerLatLng,
      			mapTypeId: gm.MapTypeId.ROADMAP
  });

  // Resize the map to fit window whenever the size is changed
  $(window).resize(onWindowResized);
  onWindowResized();
  drawInit();	
});

$(window).load(function() {
	onWindowResized();
});


function onWindowResized()
{
	var headerFooterHeight = 0;
	if($('#header').is(":visible"))
	{
		headerFooterHeight = headerFooterHeight + $('#header').outerHeight(true);
	}

	if($('#footer').is(":visible"))
	{
		headerFooterHeight = headerFooterHeight + $('#footer').outerHeight(true);
	}
	
	//Correct for borders and margins that aren't counted when setting with .height()
	// by looking at the difference between document height and window height to fine
	// the discrepancy
	var bordersHeight = $('#map-canvas').outerHeight(true) - $('#map-canvas').height();
	$('#map-canvas').height($(window).height() - headerFooterHeight - bordersHeight);
	
	//Since floats are used in the footer, they're not picked up by the height of #footer
	// so to a final correction:
	var footerOverflow = $(document).height() - $(window).height();
	var newMapHeight = $('#map-canvas').height() - footerOverflow;
	
	$('#map-canvas').height(newMapHeight);
	
  // Make the map width 100%...
  var bordersWidth = $('#map-canvas').outerWidth(true) - $('#map-canvas').width();
  $('#map-canvas').width($(window).width());
  //now correct again as it still doesn't get it right!
  $('#map-canvas').width($('#map-canvas').width() - ($(document).width() - $(window).width()));
}

