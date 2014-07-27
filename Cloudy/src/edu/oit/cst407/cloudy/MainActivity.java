package edu.oit.cst407.cloudy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity implements LocationListener {

    public static Location lastLocation = null;
    public static ListView weatherList = null;
    LocationManager locationManager;
    private List<WeatherLocation> locationList = new ArrayList<WeatherLocation>();
    public static WeatherAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // MOCK DATA
        WeatherLocation mockLocation = new WeatherLocation();
        locationList.add(mockLocation);
        // END MOCK DATA
        
        weatherList = (ListView) findViewById(R.id.weather_list);
        adapter = new WeatherAdapter(this, locationList);
        weatherList.setAdapter(adapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);
        WeatherTask weatherTask = new WeatherTask(weatherList.getChildAt(0));
        weatherTask.execute(location);
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

}
