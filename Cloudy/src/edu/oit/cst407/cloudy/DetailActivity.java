package edu.oit.cst407.cloudy;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailActivity extends ListActivity implements Observer {

    public final static String KEY_POSITION = "POSITION";

    private LocationDetailAdapter adapter = null;
    private MetaLocation metaLocation = null;
    private CurrentConditionsViewHolder holder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        CloudyUtil.INSTANCE.addObserver(this);

        handleIntent(getIntent());

        holder = new CurrentConditionsViewHolder(getWindow().getDecorView());
        holder.location_text = (TextView) findViewById(R.id.location_text);
        holder.temperature_text = (TextView) findViewById(R.id.temperature_text);
        holder.weather_text = (TextView) findViewById(R.id.weather_text);
        holder.refresh_button = (ImageButton) findViewById(R.id.refresh_button);
        holder.delete_button = (ImageButton) findViewById(R.id.delete_button);

        updateView();

        final CurrentConditionsViewHolder tempHolder = holder;
        holder.refresh_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CloudyUtil.INSTANCE.getForecast(tempHolder, metaLocation);
            }
        });

        holder.delete_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getAdapter().remove(metaLocation);
                finish();
            }
        });

        ArrayList<MetaLocation> list = new ArrayList<MetaLocation>();
        for (int count = 0; count < 13; ++count) {
            list.add(metaLocation);
        }
        adapter = new LocationDetailAdapter(this, list);
        setListAdapter(adapter);
    }

    private void updateView() {
        if (holder.refresh_container.getVisibility() == View.VISIBLE){
            holder.refresh_container.startAnimation(CloudyUtil.anim_fade_out);
            holder.refresh_container.setVisibility(View.INVISIBLE);
        }
        holder.location_text.setText(String.format("%s, %s", metaLocation.getCity(), metaLocation.getState()));
        holder.temperature_text.setText(String.format("%s°", metaLocation.getWeatherData().getCurrentTemperature()));
        holder.weather_text.setText(metaLocation.getWeatherData().getCurrentWeather());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CloudyUtil.INSTANCE.deleteObserver(this);
    }

    private void handleIntent(Intent intent) {
        int position = Integer.parseInt(intent.getStringExtra(KEY_POSITION));
        LocationAdapter adapter = MainActivity.getAdapter();
        metaLocation = adapter.getItem(position);
    }

    @Override
    public void update(Observable observable, Object data) {
        updateView();
        adapter.notifyDataSetChanged();
    }

}
