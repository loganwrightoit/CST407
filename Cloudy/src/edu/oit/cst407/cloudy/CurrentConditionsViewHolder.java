package edu.oit.cst407.cloudy;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CurrentConditionsViewHolder {

    final View refresh_container;
    final View content_container;
    TextView refresh_text;
    TextView location_text;
    TextView temperature_text;
    TextView weather_text;
    ImageButton refresh_button;
    ImageButton delete_button;
    ImageView current_location;
    
    public CurrentConditionsViewHolder(View view) {
        refresh_container = view.findViewById(R.id.refresh_container);
        content_container = view.findViewById(R.id.content_container);
    }

}
