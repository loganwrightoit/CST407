package edu.oit.cst407.cloudy;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

public class MainActivity extends ListActivity implements LocationListener {

    private LocationManager locationManager = null;
    private static LocationAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Prepare adapter */

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String locations = prefs.getString("locations", "");
        adapter = new LocationAdapter(this, CloudyUtil.getListFromJsonString(locations));
        setListAdapter(adapter);
        
        /* Start listening to obtain current location */

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    public static LocationAdapter getAdapter() {
        return adapter;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getAccuracy() >= 0.68F) {
            locationManager.removeUpdates(this);
            // Add current location to list if not there already
            new CurrentLocation().find(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState(this);
    }
    
    /**
     * Save the contents of the location list.
     * @param context
     */
    public static void saveState(Context context) {
        Activity activity = (Activity) context;
        SharedPreferences prefs = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("locations", CloudyUtil.getListAsJsonString(adapter.objects));
        editor.apply();
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

}
