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
            } else {
                /*
                 * If location already exists on list, we need to refresh
                 * the list so that the pinpoint icon is placed next to your
                 * current location.  This just guarantees it happens quickly.
                 */
                adapter.notifyDataSetChanged();
            }
        }
    }

}
