package edu.oit.cst407.cloudy;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CurrentLocation implements ILocationTask, IForecastTask {

    public void find(double lat, double lng) {
        String query = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s", lat, lng);
        new LocationTask(this).execute(query);
    }
    
    @Override
    public void onForecastTaskPreExecute(View view) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onForecastTaskPostExecute(View view, MetaLocation metaLocation) {
        
        
        
    }

    @Override
    public void onLocationTaskPreExecute() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onLocationTaskPostExecute(ArrayList<MetaLocation> list) {
        if (!list.isEmpty()) {
            ArrayList<MetaLocation> primaryList = MainActivity.getList();
            ArrayAdapter<MetaLocation> adapter = MainActivity.getAdapter();
            ListView view = MainActivity.getListView();
    
            // Check if mainactivity list contains location
            
            if (!primaryList.contains(list.get(0))) {
                
                // Add location to list
                primaryList.add(list.get(0));
                adapter.notifyDataSetChanged();
                
                // Fetch forecast for location
                new ForecastTask(this, view.getChildAt(primaryList.size() - 1)).execute(list.get(0));
                
            }
        }
    }

}
