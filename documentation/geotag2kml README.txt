geotag2kml

This is an app to expoert data from a mysql database to a KML file.

It is currently configured to show SignalStrengthOnLocationChange entries 
from Geotagger (as uploaded by sensorlog2sql) in colours depending on 
signal and netowrk type.


*******************
*  CONFIGURATION  *
*******************

Set up mysql
**************
Look for these lines to provide database connection details:

properties.setProperty("javax.jdo.option.ConnectionURL","jdbc:mysql://localhost:3306/aether");
properties.setProperty("javax.jdo.option.ConnectionUserName","aether");
properties.setProperty("javax.jdo.option.ConnectionPassword","WU5sVPnf8Jcf2sza");




********************************************

Operation:

The output file will be saved as map.kml, overwriting any existing file.

*** THE EXISTING map.kml FILE WILL BE OVERWRITTEN***
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Ideally this would be made into a jar to run on the commandline, etc, but it hasn't been done yet.

Mark Paxton
mcp@cs.nott.ac.uk