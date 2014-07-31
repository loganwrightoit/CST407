package edu.oit.cst407.cloudy;

import java.util.ArrayList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener, IForecastTask {

    private static ListView listView = null;
    private LocationManager locationManager = null;
    private static ArrayList<MetaLocation> list = new ArrayList<MetaLocation>();
    private static LocationAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restoreListState();
        
        listView = (ListView) findViewById(R.id.weather_list);
        adapter = new LocationAdapter(this, list);
        listView.setAdapter(adapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    public static ArrayList<MetaLocation> getList() {
        return list;
    }
    
    public static LocationAdapter getAdapter() {
        return adapter;
    }
    
    public static ListView getListView() {
        return listView;
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
            //new CurrentLocation().find(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveListState();
    }
    
    public void saveListState() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("locations", CloudyUtil.getListAsJsonString(list));
        editor.apply();
    }
    
    public void restoreListState() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE); 
        String locations = prefs.getString("locations", null);
        if (locations != null) {
            list = CloudyUtil.getListFromJsonString(locations);
        }
    }
    
    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	public void onForecastTaskPreExecute(View view) {
        RelativeLayout loading_container = (RelativeLayout) view.findViewById(R.id.loading_container);
        RelativeLayout weather_container = (RelativeLayout) view.findViewById(R.id.weather_container);    
        weather_container.setVisibility(View.INVISIBLE);
        loading_container.setVisibility(View.VISIBLE);
	}
    
	@Override
	public void onForecastTaskPostExecute(View view, MetaLocation metaLocation) {
        RelativeLayout loading_container = (RelativeLayout) view.findViewById(R.id.loading_container);
        loading_container.setVisibility(View.INVISIBLE);
        
        RelativeLayout weather_container = (RelativeLayout) view.findViewById(R.id.weather_container);
        weather_container.setVisibility(View.VISIBLE);
        
        TextView location_text = (TextView) view.findViewById(R.id.location_text);
        TextView temperature_text = (TextView) view.findViewById(R.id.temperature_text);
        TextView weather_text = (TextView) view.findViewById(R.id.weather_text);

        location_text.setText(String.format("%s, %s", metaLocation.getCity(), metaLocation.getState()));
        temperature_text.setText(String.format("%s°", metaLocation.getTemp()));
        weather_text.setText(metaLocation.getWeather());
	}
	
	private class LocationAdapter extends ArrayAdapter<MetaLocation> {

	    public LocationAdapter(Context context, ArrayList<MetaLocation> objects) {
	        super(context, 0, objects);
	    }

	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	        
	        if( convertView == null ) {
	            LayoutInflater inflater = LayoutInflater.from(getContext());
	            convertView = inflater.inflate(R.layout.list_item_weatherlocation, parent, false);
	        }
	        
	        final MetaLocation metaLocation = (MetaLocation) listView.getItemAtPosition(position);
	        
	        TextView location_text = (TextView) convertView.findViewById(R.id.location_text);
	        TextView temperature_text = (TextView) convertView.findViewById(R.id.temperature_text);
	        TextView weather_text = (TextView) convertView.findViewById(R.id.weather_text);
	        location_text.setText(String.format("%s, %s", metaLocation.getCity(), metaLocation.getState()));
	        temperature_text.setText(String.format("%s°", metaLocation.getTemp()));
	        weather_text.setText(metaLocation.getWeather());

	        ImageButton refreshButton = (ImageButton) convertView.findViewById(R.id.refresh_button);
	        
	        refreshButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                new ForecastTask(MainActivity.this, v).execute(metaLocation);
	            }
	        });
	        
	        return convertView;
	    }

	}

}
