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
    
    public LocationAdapter(Context context, ArrayList<MetaLocation> objects) {
        super(context, 0, objects);
        this.objects = objects;
        inflater = LayoutInflater.from(getContext());
    }
    
    public void addItem(MetaLocation metaLocation) {
        objects.add(metaLocation);
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
        ViewHolderItem holder = null;
        final MetaLocation metaLocation = objects.get(position);
        
        Log.d("DEBUG", String.format("Position %s holds %s, %s", position, metaLocation.getCity(), metaLocation.getState()));

        if (convertView == null) {
            holder = new ViewHolderItem();
            convertView = inflater.inflate(R.layout.list_item_weatherlocation, parent, false);

            holder.loading_container = (RelativeLayout) convertView.findViewById(R.id.loading_container);
            holder.loading_text = (TextView) convertView.findViewById(R.id.loading_text);
            holder.weather_container = (RelativeLayout) convertView.findViewById(R.id.weather_container);
            holder.location_text = (TextView) convertView.findViewById(R.id.location_text);
            holder.temperature_text = (TextView) convertView.findViewById(R.id.temperature_text);
            holder.weather_text = (TextView) convertView.findViewById(R.id.weather_text);
            holder.currentLocation_image = (ImageView) convertView.findViewById(R.id.current_location_image);
            holder.refreshButton = (ImageButton) convertView.findViewById(R.id.refresh_button);
            holder.deleteButton = (ImageButton) convertView.findViewById(R.id.delete_button);

            // Refresh forecast if necessary
            if (metaLocation.hasExpired()) {
                getForecast(holder, metaLocation);
            }
            
            final ViewHolderItem tempHolder = holder;
            
            holder.refreshButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    getForecast(tempHolder, metaLocation);
                }
            });

            holder.deleteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.saveState(LocationAdapter.this.getContext());
                    remove(metaLocation);
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "Location removed", Toast.LENGTH_SHORT).show();
                }
            });
            
            convertView.setTag(holder);             
            Log.d("DEBUG", String.format("Inflating metalocation %s, %s at position %s", metaLocation.getCity(), metaLocation.getState(), position));
        } else {
            holder = (ViewHolderItem) convertView.getTag();
        }

        holder.loading_container.setVisibility(View.INVISIBLE);
        holder.weather_container.setVisibility(View.VISIBLE);
        holder.location_text.setText(String.format("%s, %s", metaLocation.getCity(), metaLocation.getState()));
        holder.temperature_text.setText(String.format("%s°", metaLocation.getTemp()));
        holder.weather_text.setText(metaLocation.getWeather());
        
        MetaLocation currentLocation = CurrentLocation.currentLocation;
        if (currentLocation != null && currentLocation.equals(metaLocation)) {
            holder.currentLocation_image.setVisibility(View.VISIBLE);
        } else {
            holder.currentLocation_image.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
        
    /**
     * Creates forecast inquiry for location(s) and toggles refresh state
     * on ListView item.
     * @param location
     */
    public void getForecast(ViewHolderItem holder, MetaLocation location) {
        holder.weather_container.setVisibility(View.INVISIBLE);
        holder.loading_container.setVisibility(View.VISIBLE);
        holder.loading_text.setText(R.string.refreshing_location);
        new ForecastTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, location);
    }
    
    @Override
    public void onForecastTaskPostExecute(MetaLocation[] locations) {
        notifyDataSetChanged();
    }

    static class ViewHolderItem {
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