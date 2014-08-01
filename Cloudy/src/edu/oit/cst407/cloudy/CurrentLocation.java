package edu.oit.cst407.cloudy;

import java.util.ArrayList;

import android.widget.ArrayAdapter;

public class CurrentLocation implements ILocationTask {

	public static MetaLocation currentLocation;
	
    public void find(double lat, double lng) {
        String query = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s", lat, lng);
        new LocationTask(this).execute(query);
    }

    @Override
    public void onLocationTaskPostExecute(MetaLocation[] location) {
        if (location.length > 0) {
            ArrayList<MetaLocation> primaryList = MainActivity.getList();
            ArrayAdapter<MetaLocation> adapter = MainActivity.getAdapter();
            MetaLocation metaLocation = currentLocation = location[0];

            // Add current location to list if it doesn't already exist
            if (!primaryList.contains(metaLocation)) {
                primaryList.add(metaLocation);
            }
            
            // We want the list to refresh since current location will change attributes
            adapter.notifyDataSetChanged();
        }
    }

}
