package horizon.aether.sensors;

import horizon.aether.sensors.LoggingListenerService;
   
interface SensorServiceLogger
{
    void log(in long timestamp, in String identifier, in String dataBlob);
    
    void registerListener(in LoggingListenerService listener);    
}