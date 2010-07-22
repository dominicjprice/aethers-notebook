package horizon.aether.sensors;

import org.json.JSONException;
import org.json.JSONStringer;

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
        JSONStringer data = new JSONStringer();
        try {
            data.object();
            data.key("timestamp");
            data.value(timestamp);
            data.key("identifier");
            data.value(identifier);
            data.key("location");
            data.value(location);
            data.key("dataBlob");
            data.value(dataBlob);
            data.endObject();
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        
        return data.toString();
    }
}
