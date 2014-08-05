package edu.oit.cst407.cloudy;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LocationDetailAdapter extends ArrayAdapter<MetaLocation> {

    private LayoutInflater inflater;
    private String[] period;
    private String[] periodWeather;

    public LocationDetailAdapter(Context context, ArrayList<MetaLocation> list) {
        super(context, 0, list);
        MetaLocation metaLocation = list.get(0);

        period = metaLocation.getWeatherData().getForecastPeriods();
        periodWeather = metaLocation.getWeatherData().getForecastPeriodConditions();

        inflater = LayoutInflater.from(getContext());
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_location_detail, parent, false);

            holder.period_text = (TextView) convertView.findViewById(R.id.period_text);
            holder.period_conditions_text = (TextView) convertView.findViewById(R.id.period_conditions_text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.period_text.setText(period[position]);
        holder.period_conditions_text.setText(periodWeather[position]);

        return convertView;
    }

    static class ViewHolder {
        TextView detail_weather_current_text;
        TextView period_text;
        TextView period_conditions_text;
    }

}
