package edu.oit.cst407.cloudy;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationAdapter extends ArrayAdapter<MetaLocation> {

    public ArrayList<MetaLocation> objects;
    private LayoutInflater inflater;

    public LocationAdapter(Context context, ArrayList<MetaLocation> objects) {
        super(context, 0, objects);
        this.objects = objects;
        inflater = LayoutInflater.from(getContext());
    }

    public void addItem(MetaLocation metaLocation) {
        objects.add(metaLocation);
        MainActivity.saveState(LocationAdapter.this.getContext());
        notifyDataSetChanged();
    }
    
    @Override
    public void remove(MetaLocation metaLocation) {
        super.remove(metaLocation);
        MainActivity.saveState(LocationAdapter.this.getContext());
        Toast.makeText(getContext(), String.format("Removed %s, %s from list.", metaLocation.getCity(), metaLocation.getState()), Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
    }

    public ArrayList<MetaLocation> getList() {
        return objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public MetaLocation getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CurrentConditionsViewHolder holder = null;
        final MetaLocation metaLocation = objects.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_location, parent, false);
            
            holder = new CurrentConditionsViewHolder(convertView);
            holder.location_text = (TextView) convertView.findViewById(R.id.location_text);
            holder.temperature_text = (TextView) convertView.findViewById(R.id.temperature_text);
            holder.weather_text = (TextView) convertView.findViewById(R.id.weather_text);
            holder.current_location = (ImageView) convertView.findViewById(R.id.current_location_image);
            holder.refresh_button = (ImageButton) convertView.findViewById(R.id.refresh_button);
            holder.delete_button = (ImageButton) convertView.findViewById(R.id.delete_button);
            
            convertView.setTag(holder);
        } else {
            holder = (CurrentConditionsViewHolder) convertView.getTag();
        }
        
        holder.location_text.setText(String.format("%s, %s", metaLocation.getCity(), metaLocation.getState()));
        holder.temperature_text.setText(String.format("%s°", metaLocation.getWeatherData().getCurrentTemperature()));
        holder.weather_text.setText(metaLocation.getWeatherData().getCurrentWeather());

        /*
         * If a location is refreshing then the refreshing container
         * is all that should be visible.  This also keeps DataSet updates
         * from clearing the refreshing state of other locations upon
         * a completed forecast task.
         */
        if (!CloudyUtil.INSTANCE.hasTask(metaLocation)) {

            if (holder.refresh_container.getVisibility() == View.VISIBLE){ 
                holder.refresh_container.startAnimation(CloudyUtil.anim_fade_out);
                holder.refresh_container.setVisibility(View.INVISIBLE);
            }
            
            /*
             * Refresh the forecast if MetaLocation date has expired
             * and a task for this location isn't currently running.
             * This time is approximated at one hour for weather.gov.
             */
            if (metaLocation.hasExpired()) {
                
                CloudyUtil.INSTANCE.getForecast(holder, metaLocation);
                
            } else {

                /* Add pinpoint icon next to location if it's the current location. */

                MetaLocation currentLocation = CurrentLocation.currentLocation;
                if (currentLocation != null && currentLocation.equals(metaLocation)) {
                    holder.current_location.setVisibility(View.VISIBLE);
                } else {
                    holder.current_location.setVisibility(View.INVISIBLE);
                }

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
                        remove(metaLocation);
                    }
                });

            }
        }

        return convertView;
    }

}
