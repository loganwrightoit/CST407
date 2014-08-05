package edu.oit.cst407.cloudy;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends ListActivity {

    public final static String KEY_POSITION = "POSITION";
    
    private LocationDetailAdapter adapter = null;
    private MetaLocation metaLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        handleIntent(getIntent());

        TextView detail_location_text = (TextView) findViewById(R.id.detail_location_text);
        TextView detail_temperature_text = (TextView) findViewById(R.id.detail_temperature_text);
        TextView detail_weather_current_text = (TextView) findViewById(R.id.detail_weather_current_text);
        
        detail_location_text.setText(String.format("%s, %s", metaLocation.getCity(), metaLocation.getState()));
        detail_temperature_text.setText(String.format("%s°", metaLocation.getWeatherData().getCurrentTemperature()));
        detail_weather_current_text.setText(metaLocation.getWeatherData().getCurrentWeather());

        ArrayList<MetaLocation> list = new ArrayList<MetaLocation>();
        for (int count = 0; count < 13; ++count) {
            list.add(metaLocation);
        }
        adapter = new LocationDetailAdapter(this, list);
        setListAdapter(adapter);
    }

    private void handleIntent(Intent intent) {
        int position = Integer.parseInt(intent.getStringExtra(KEY_POSITION));
        LocationAdapter adapter = MainActivity.getAdapter();
        metaLocation = adapter.getItem(position);
    }

}
