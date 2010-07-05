package horizon.aether.sensors;

import org.json.JSONException;
import org.json.JSONStringer;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service implements LocationListener {
    
    private static Location location;
    private static LocationManager locationManager;
       
    public String getLocation() {
        if (location == null)
            return "EARTH";
        else
            return convertLocationToString(location);
    }
    
    public void startLocationUpdates()
    {
        for (String prov : locationManager.getAllProviders())
            locationManager.requestLocationUpdates(prov, 1, 1, this);
    }
    
    public void stopLocationUpdates()
    {
        // respect user's privacy
        locationManager.removeUpdates(this);
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        startLocationUpdates();
        
        // initialize location
        location = locationManager.getLastKnownLocation(locationManager.getAllProviders().get(0));
    }
    
    @Override
    public void onLocationChanged(Location loc)
    {
        // save the location
        location = loc;        
    }
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}
    
    private static String convertLocationToString(Location location) {
        JSONStringer data = new JSONStringer();
        try
        {
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
        catch(JSONException e)
        {
            throw new RuntimeException(e);
        }
        
        return data.toString();
    }
}
