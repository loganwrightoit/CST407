package edu.oit.cst407.cloudy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.SearchView;

public class MainActivity extends Activity implements LocationListener {

    public static ListView weatherList = null;
    LocationManager locationManager;
    public static List<WeatherLocation> locationList = new ArrayList<WeatherLocation>();
    public static WeatherAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Default, or "current" location at index 0
        WeatherLocation mockLocation = new WeatherLocation();
        locationList.add(mockLocation);
        
        weatherList = (ListView) findViewById(R.id.weather_list);
        adapter = new WeatherAdapter(this, locationList);
        weatherList.setAdapter(adapter);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
               (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
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
