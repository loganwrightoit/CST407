package edu.oit.cst407.cloudy;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

public class WeatherAdapter extends ArrayAdapter<WeatherLocation> {

    public WeatherAdapter(Context context, List<WeatherLocation> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null ) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_weatherlocation, parent, false);
        }
        
        final View view = convertView;
                
        ImageButton refreshButton = (ImageButton) convertView.findViewById(R.id.refresh_button);
        
        refreshButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherTask task = new WeatherTask(view);
                task.execute(MainActivity.lastLocation);
            }
        });
        
        return convertView;
    }

}