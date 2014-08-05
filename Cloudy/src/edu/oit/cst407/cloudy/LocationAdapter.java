package edu.oit.cst407.cloudy;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LocationAdapter extends ArrayAdapter<MetaLocation> implements IForecastTask {

    public ArrayList<MetaLocation> objects;
    private LayoutInflater inflater;
    private ArrayList<MetaLocation> taskList = new ArrayList<MetaLocation>();

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
        ViewHolder holder = null;
        final MetaLocation metaLocation = objects.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_location, parent, false);

            holder.loading_container = (RelativeLayout) convertView.findViewById(R.id.loading_container);
            holder.loading_text = (TextView) convertView.findViewById(R.id.loading_text);
            holder.weather_container = (RelativeLayout) convertView.findViewById(R.id.weather_container);
            holder.location_text = (TextView) convertView.findViewById(R.id.detail_location_text);
            holder.temperature_text = (TextView) convertView.findViewById(R.id.temperature_text);
            holder.weather_text = (TextView) convertView.findViewById(R.id.weather_text);
            holder.currentLocation_image = (ImageView) convertView.findViewById(R.id.current_location_image);
            holder.refreshButton = (ImageButton) convertView.findViewById(R.id.refresh_button);
            holder.deleteButton = (ImageButton) convertView.findViewById(R.id.delete_button);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*
         * If a location is refreshing then the refreshing container
         * is all that should be visible.  This also keeps DataSet updates
         * from clearing the refreshing state of other locations upon
         * a completed forecast task.
         */
        if (taskList.contains(metaLocation)) {

            holder.loading_container.setVisibility(View.VISIBLE);
            holder.weather_container.setVisibility(View.INVISIBLE);

        } else {

            /*
             * Refresh the forecast if MetaLocation date has expired
             * and a task for this location isn't currently running.
             * This time is approximated at one hour for weather.gov.
             */
            if (metaLocation.hasExpired()) {
                
                getForecast(holder, metaLocation);
                
            } else {

                holder.loading_container.setVisibility(View.INVISIBLE);
                holder.weather_container.setVisibility(View.VISIBLE);
                holder.location_text.setText(String.format("%s, %s", metaLocation.getCity(), metaLocation.getState()));
                holder.temperature_text.setText(String.format("%s°", metaLocation.getWeatherData().getCurrentTemperature()));
                holder.weather_text.setText(metaLocation.getWeatherData().getCurrentWeather());

                /* Add pinpoint icon next to location if it's the current location. */

                MetaLocation currentLocation = CurrentLocation.currentLocation;
                if (currentLocation != null && currentLocation.equals(metaLocation)) {
                    holder.currentLocation_image.setVisibility(View.VISIBLE);
                } else {
                    holder.currentLocation_image.setVisibility(View.INVISIBLE);
                }

                final ViewHolder tempHolder = holder;
                holder.refreshButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getForecast(tempHolder, metaLocation);
                    }
                });

                holder.deleteButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(metaLocation);
                        MainActivity.saveState(LocationAdapter.this.getContext());
                        notifyDataSetChanged();
                        Toast.makeText(getContext(), String.format("Removed %s, %s from list.", metaLocation.getCity(), metaLocation.getState()), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

        return convertView;
    }

    /**
     * Creates forecast inquiry for location(s) and toggles refresh state
     * on ListView item.
     * @param location
     */
    public void getForecast(ViewHolder holder, MetaLocation location) {
        if (!taskList.contains(location)) {
            Log.d("DEBUG", "Grabbing forecast for view.");
            holder.weather_container.setVisibility(View.INVISIBLE);
            holder.loading_container.setVisibility(View.VISIBLE);
            holder.loading_text.setText(R.string.main_refreshing_location);
            taskList.add(location);
            new ForecastTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, location);
        }
    }

    @Override
    public void onForecastTaskPostExecute(MetaLocation[] location) {
        for (MetaLocation metaLocation : location) {
            taskList.remove(metaLocation);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        RelativeLayout loading_container;
        TextView loading_text;
        RelativeLayout weather_container;
        TextView location_text;
        TextView temperature_text;
        TextView weather_text;
        ImageView currentLocation_image;
        ImageButton refreshButton;
        ImageButton deleteButton;
    }

}
