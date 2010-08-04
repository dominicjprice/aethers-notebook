package horizon.aether.gaeserver.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An entry pack contains two lists. A list of logging entries
 * and a list of the corresponding blobs.
 */
public class EntryPack {
    
    private List<Entry> entries;
    
    private List<IBlob> blobs;
    
    /**
     * Constructor.
     * @param entries
     * @param blobs
     */
    public EntryPack(List<Entry> entries, List<IBlob> blobs) {
        this.entries = entries;
        this.blobs = blobs;
    }
    
    /**
     * Default constructor.
     */
    public EntryPack() {
        this.entries = new ArrayList<Entry>();
        this.blobs = new ArrayList<IBlob>();
    }
    
    /**
     * Add an entry-blob pair to the lists.
     * @param entry
     * @param blob
     */
    public void add(Entry entry, IBlob blob) {
        entries.add(entry);
        blobs.add(blob);
    }
    
    /**
     * Gets the entries.
     * @return The entries.
     */
    public List<Entry> getEntries() {
        return this.entries;
    }
    
    /**
     * Gets the blobs.
     * @return The blobs.
     */
    public List<IBlob> getBlobs() {
        return this.blobs;
    }
}
