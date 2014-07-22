package edu.oit.cst407.cloudy;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener {

    public static TextView textView;
	LocationManager locationManager;
	Location location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textView = (TextView) findViewById(R.id.textView1);
		textView.setText("Determining temperature for your location...");

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
	}

	@Override
	public void onLocationChanged(Location location) {
	    
	    if (location != null) {
	        textView.setText("Processing a new location...");
    	    this.location = location;
    	    locationManager.removeUpdates(this);
    
    	    WeatherTask weatherTask = new WeatherTask();
    	    weatherTask.execute(location);
	    } else {
	        textView.setText("Location retrieved was null, continuing...");
	    }
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

}
