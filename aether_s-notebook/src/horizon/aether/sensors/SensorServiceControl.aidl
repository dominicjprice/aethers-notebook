package horizon.aether.sensors;

import horizon.aether.sensors.LoggingServiceDescriptor;

interface SensorServiceControl 
{
	List<LoggingServiceDescriptor> getLoggers();
	
	void stopLogger(in LoggingServiceDescriptor descriptor);
	
	void startLogger(in LoggingServiceDescriptor descriptor);
	
	boolean getLoggerStatus(in LoggingServiceDescriptor descriptor);
}