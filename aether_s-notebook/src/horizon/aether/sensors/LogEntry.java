package horizon.aether.sensors;

public class LogEntry {

    private long timestamp;
    private String identifier;
    private String location;
    private String dataBlob;
    
    public long getTimestamp() { return this.timestamp; }
    public String identifier() { return this.identifier; }
    public String location() { return this.location; }
    public String dataBlob() { return this.dataBlob; }
    
    public LogEntry(long timestamp, String identifier, String location, String dataBlob) {
        this.timestamp = timestamp;
        this.identifier = identifier;
        this.location = location;
        this.dataBlob = dataBlob;
    }
    
    @Override
    public String toString() {
        return this.timestamp + ":" + 
               this.identifier + ":" +
               this.location + ":" + 
               this.dataBlob;
    }    
}
