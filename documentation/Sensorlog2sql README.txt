** PLEASE SEE THE QUIRKS SECTION BEFORE USING THIS APP WITH YOUR DATA!! **

Sensorlog2sql


This is a java app that reads geotagger log files and publishes them to an sql database.

Cuerrently the configuration is hardcoded, so you will have to run from within Eclipse, etc.


*******************
*  CONFIGURATION  *
*******************

Set up mysql
**************
Look for these lines to provide database connection details:

properties.setProperty("javax.jdo.option.ConnectionURL","jdbc:mysql://localhost:3306/aether");
properties.setProperty("javax.jdo.option.ConnectionUserName","aether");
properties.setProperty("javax.jdo.option.ConnectionPassword","WU5sVPnf8Jcf2sza");


The app automatically creates tables, etc as required.


Specify your source file:
**************************

Look for and change as required:
String sourceFileName = "sensorservice.log";

Sepceify compression standard:
*******************************
CompressionType compressionType = CompressionType.NONE;    	


********************************************

Operation:

This does take some time, and has little feedback.  
Expect to wait 10 minutes or more if you have ~10,000 entries in your logs.

I reccomend you dump the sql database once it's been run as it's much quicker to import again later!

***********************************
> > > > > > Quirks < < < < < < < < 
***********************************

The SignalStrengthEntry class was hacked for the Bridging the Rural Divide fieldwork to provide additional data
this was then changed to SignalStrengthOnLocationChange - this app has currently been modified to import all 
SignalStrength entries as 'OnLocationChange entries, and also mirror it to SignalStrength - this needs to be changed 
and tested with the modified Andriod app to make both types will work as intended.

^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
************************************************************

Mark Paxton
mcp@cs.nott.ac.uk