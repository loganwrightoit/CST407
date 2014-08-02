package edu.oit.cst407.cloudy;

import android.os.AsyncTask;

public class CurrentLocation implements ILocationTask {

	public static MetaLocation currentLocation;
	
    public void find(double lat, double lng) {
        String query = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s", lat, lng);
        new LocationTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, query);
    }

    @Override
    public void onLocationTaskPostExecute(MetaLocation[] location) {
        if (location.length > 0) {
            LocationAdapter adapter = MainActivity.getAdapter();
            MetaLocation metaLocation = currentLocation = location[0];

            // Add current location to list if it doesn't already exist
            if (!adapter.getList().contains(metaLocation)) {
                adapter.add(metaLocation);
            }
            
            // We want the list to refresh since current location will change attributes
            adapter.notifyDataSetChanged();
        }
    }

}
