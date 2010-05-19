package horizon.aether.sensors;

interface SensorServiceLogger
{
    void log(in long timestamp, in String identifier, in String dataBlob);
}