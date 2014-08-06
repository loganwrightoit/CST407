package edu.oit.cst407.cloudy;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class MainActivity extends ListActivity implements LocationListener, Observer {

    private LocationManager locationManager = null;
    private static LocationAdapter adapter = null;

    public final static String KEY_DETAIL = "detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CloudyUtil.anim_fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.anim_fade_in);
        CloudyUtil.anim_fade_out = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.anim_fade_out);

        CloudyUtil.INSTANCE.addObserver(this);

        /* Prepare adapter */

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String locations = prefs.getString("locations", "");
        adapter = new LocationAdapter(this, CloudyUtil.INSTANCE.getListFromJsonString(locations));
        setListAdapter(adapter);

        /* Start listening to obtain current location */

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CloudyUtil.INSTANCE.deleteObserver(this);
    }

    @Override
    /**
     * This method will be called when an item in the list is selected.
     * Subclasses should override. Subclasses can call
     * getListView().getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param l The ListView where the click happened
     * @param v The view that was clicked within the ListView
     * @param position The position of the view in the list
     * @param id The row id of the item that was clicked
     */
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_POSITION, Integer.toString(position));
        startActivityForResult(intent, 0);
    }

    public static LocationAdapter getAdapter() {
        return adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setIconified(true);
                searchView.clearFocus();
                (menu.findItem(R.id.action_search)).collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) { return false; }
        });

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
        editor.putString("locations", CloudyUtil.INSTANCE.getListAsJsonString("locations", adapter.objects));
        editor.apply();
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void update(Observable observable, Object data) {
        adapter.notifyDataSetChanged();
    }

}
