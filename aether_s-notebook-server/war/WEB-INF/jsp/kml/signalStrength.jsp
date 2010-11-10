<%@ page isELIgnored="false" contentType="application/vnd.google-earth.kml+xml kml" pageEncoding="utf8"%><%--
--%><%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%--
--%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%--
--%><?xml version="1.0" standalone="yes"?>
<kml xmlns="http://earth.google.com/kml/2.2">
	<Document>
		<Snippet><![CDATA[<p>Created by Horizon.Mark Paxton</p>]]></Snippet>
		<Style id="gv_waypoint_normal">
				<BalloonStyle>
				<text><![CDATA[<p>Description</p>${title}]]></text>
			</BalloonStyle>
			<IconStyle>
				<Icon>
					<href>http://maps.google.ca/mapfiles/kml/pal4/icon56.png</href>
				</Icon>
				<color>FFFFFFFF</color>
				<hotSpot x="0.5" xunits="fraction" y="0.5" yunits="fraction" />
			</IconStyle>
			<LabelStyle>
				<color>FFFFFFFF</color>
				<scale>1</scale>
			</LabelStyle>
		</Style>
		 <Style id="gv_waypoint_highlight">
			<BalloonStyle>
			  <text><![CDATA[<p align="left" style="white-space:nowrap;"><font size="+1"><b>$[name]</b></font></p> <p align="left">$[description]</p>]]></text>
			</BalloonStyle>
			<IconStyle>
			  <Icon>
				 <href>http://maps.google.ca/mapfiles/kml/pal4/icon56.png</href>
			  </Icon>
			  <color>FFFFFFFF</color>
			  <hotSpot x="0.5" xunits="fraction" y="0.5" yunits="fraction" />
			  <scale>1.2</scale>
			</IconStyle>
			<LabelStyle>
			  <color>FFFFFFFF</color>
			  <scale>1</scale>
			</LabelStyle>
		 </Style>
		 <Style id="gv_trackpoint_normal">
			<BalloonStyle>
			  <text><![CDATA[<p align="left" style="white-space:nowrap;"><font size="+1"><b>$[name]</b></font></p> <p align="left">$[description]</p>]]></text>
			</BalloonStyle>
			<IconStyle>
			  <Icon>
				 <href>http://maps.google.ca/mapfiles/kml/pal2/icon26.png</href>
			  </Icon>
			  <scale>0.3</scale>
			</IconStyle>
			<LabelStyle>
			  <scale>0</scale>
			</LabelStyle>
		 </Style>
		 <Style id="gv_trackpoint_highlight">
			<BalloonStyle>
			  <text><![CDATA[<p align="left" style="white-space:nowrap;"><font size="+1"><b>$[name]</b></font></p> <p align="left">$[description]</p>]]></text>
			</BalloonStyle>
			<IconStyle>
			  <Icon>
				 <href>http://maps.google.ca/mapfiles/kml/pal2/icon26.png</href>
			  </Icon>
			  <scale>0.4</scale>
			</IconStyle>
			<LabelStyle>
			  <scale>1</scale>
			</LabelStyle>
		 </Style>
		 <StyleMap id="gv_waypoint">
			<Pair>
			  <key>normal</key>
			  <styleUrl>#gv_waypoint_normal</styleUrl>
			</Pair>
			<Pair>
			  <key>highlight</key>
			  <styleUrl>#gv_waypoint_highlight</styleUrl>
			</Pair>
		</StyleMap>
		<StyleMap id="gv_trackpoint">
			<Pair>
				<key>normal</key>
				<styleUrl>#gv_trackpoint_normal</styleUrl>
			</Pair>
			<Pair>
				<key>highlight</key>
				<styleUrl>#gv_trackpoint_highlight</styleUrl>
			</Pair>
		 </StyleMap>
		<name><![CDATA[Geotagger app data]]></name>
		<open>1</open>
		<visibility>1</visibility>
		<Placemark>
			<name><![CDATA[<p>Route</p>]]></name>
			<description><![CDATA[<p>Rough route based on joining GPS readings</p>]]></description>
			<MultiGeometry>
				<LineString>
					<altitudeMode>clampToGround</altitudeMode>
					<coordinates></coordinates>
					<tessellate>1</tessellate>
				</LineString>
			</MultiGeometry>
			<Snippet></Snippet>
			<Style>
				<LineStyle>
					<color>ff00008b</color>
					<width>4</width>
				</LineStyle>
			</Style>
		</Placemark>
		<Folder id="track 1 points">
			<name>GPS Points</name>		
<c:forEach items="${entries}" var="entry">
	<jsp:useBean id="dateValue" class="java.util.Date" />
	<jsp:setProperty name="dateValue" property="time" value="${entry.timestamp}" />
	<fmt:formatDate var="entrydate" value="${dateValue}"  pattern="yyyy-MM-dd" scope="request"/>
	<fmt:formatDate var="entrytime" value="${dateValue}"  pattern="HH:mm:ss" scope="request"/>
			<Placemark>
				<Point>
					<altitudeMode>clampToGround</altitudeMode>
					<coordinates>${entry.location.longitude}, ${entry.location.latitude}</coordinates>
					<extrude>1</extrude>
				</Point>
				<name>${entrydate}T${entrytime}</name>
				<description><![CDATA[<b>Signal Strength Reading</b><br/>
				<i>Network Type:</i> ${entry.networkType}<br/>
				<i>Signal Strength:</i> ${entry.signalStrength}&#176;<br/>
				<i>Latitude:</i> ${entry.location.latitude}&#176;<br/>
				<i>Longitude:</i> ${entry.location.longitude}&#176;<br/>
				<i>Elevation:</i> ${entry.location.altitude}m<br/>
				<i>Speed:</i> ${entry.location.speed}km/h<br/>
				<i>Time:</i> ${entrydate}T${entrytime} ]]></description>
				<TimeStamp>
					<when>${entrydate}T${entrytime}+01:00</when>
				</TimeStamp>
				<styleUrl>#gv_trackpoint</styleUrl>
				<Snippet></Snippet>
				<Style>
					<IconStyle>
						<color>FF0073E6</color>
				 	</IconStyle>
				 	<LabelStyle>
						<color>FF0073E6</color>
				 	</LabelStyle>
				 	<LineStyle>
						<color>990073E6</color>
						<width>2</width>
				 	</LineStyle>
				</Style>
			</Placemark>
</c:forEach>
		</Folder>
	</Document>
</kml>