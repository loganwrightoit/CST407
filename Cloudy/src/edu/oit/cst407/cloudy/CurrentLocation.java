package edu.oit.cst407.cloudy;

import java.util.ArrayList;

import android.view.View;
import android.widget.ArrayAdapter;

public class CurrentLocation implements ILocationTask {

    public void find(double lat, double lng) {
        String query = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s", lat, lng);
        new LocationTask(this).execute(query);
    }

    @Override
    public void onLocationTaskPostExecute(MetaLocation[] location) {
        if (location.length > 0) {
            ArrayList<MetaLocation> primaryList = MainActivity.getList();
            ArrayAdapter<MetaLocation> adapter = MainActivity.getAdapter();
            MetaLocation metaLocation = location[0];

            // Add current location to list if it doesn't already exist
            if (!primaryList.contains(metaLocation)) {
                primaryList.add(metaLocation);
                adapter.notifyDataSetChanged();
            } else {
            	// Set listview for location as "Current" with a different color
            	View view = CloudyUtil.getMetaLocationView(metaLocation);
            	
            }
        }
    }

}
