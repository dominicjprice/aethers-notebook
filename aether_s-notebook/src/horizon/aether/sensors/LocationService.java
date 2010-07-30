package horizon.aether.sensors;

import horizon.android.logging.Logger;

import org.json.JSONException;
import org.json.JSONStringer;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Keeps track of the current location and provides methods for
 * other services to access this information. 
 */
public class LocationService 
extends Service 
implements LocationListener {
    
    private static Location location;
    private static LocationManager locationManager;
    
    private static final Logger logger = Logger.getLogger(LocationService.class);
    
    /**
     * Returns the current location as a JSON string.
     * @return the location as a JSON string.
     */
    public static String getLocation() {
        if (location == null)
            return null;
        else
            return convertLocationToString(location);
    }
    
    /**
     * Registers to location updates of all the providers.
     */
    public void startLocationUpdates() {
        for (String prov : locationManager.getAllProviders())
            locationManager.requestLocationUpdates(prov, 1, 1, this);
    }
    
    /**
     * Stops receiving location updates.
     */
    public void stopLocationUpdates() {
        locationManager.removeUpdates(this);
    }
    
    /**
     * Called by the system when clients want to bind to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Called by the system when the service is created. The location is 
     * initialised and the service registers to location updates. 
     */
    @Override
    public void onCreate() {
        super.onCreate();
        logger.verbose("LocationService.onCreate()");
        
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        startLocationUpdates();
        
        // initialise location
        location = locationManager.getLastKnownLocation(locationManager.getAllProviders().get(0));
    }
    
    /**
     * Called by the system when the service is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.verbose("LocationService.onDestroy()");
    }
    
    /**
     * Called by the system when the location has changed.
     */
    @Override
    public void onLocationChanged(Location loc) {
        location = loc;        
    }
    
    /**
     * Called by the system when the provider changes.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    /**
     * Called by the system when a provider is disabled by the user.
     */
    @Override
    public void onProviderDisabled(String provider) {}

    /**
     * Called by the system when a provider is enabled by the user.
     */
    @Override
    public void onProviderEnabled(String provider) {}
    
    /**
     * Converts a location object to a JSON string.
     * @param location
     * @return the location as a JSON string.
     */
    private static String convertLocationToString(Location location) {
        JSONStringer data = new JSONStringer();
        try {
            data.object();
            data.key("accuracy");
            if(location.hasAccuracy())
                data.value(location.getAccuracy());
            else
                data.value(null);
            
            data.key("altitude");
            if(location.hasAltitude())
                data.value(location.getAltitude());
            else
                data.value(null);
            
            data.key("bearing");
            if(location.hasBearing())
                data.value(location.getBearing());
            else
                data.value(null);
            
            data.key("latitude");
            data.value(location.getLatitude());
            
            data.key("longitude");
            data.value(location.getLongitude());
            
            data.key("provider");
            data.value(location.getProvider());
            
            data.key("speed");
            if(location.hasSpeed())
                data.value(location.getSpeed());
            else
                data.value(null);
            
            Bundle extras = location.getExtras();
            data.key("extras");
            if(extras != null)
                data.value(extras.toString());
            else
                data.value(null);
                    
            data.endObject();
        }
        catch(JSONException e) {
            throw new RuntimeException(e);
        }
        
        return data.toString();
    }
}
