package edu.oit.cst407.cloudy;

import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeatherTask extends AsyncTask<Location, Void, WeatherLocation> {

    private RelativeLayout loading_container = null;
    private RelativeLayout weather_container = null;    
    private TextView location_text = null;
    private TextView temperature_text = null;
    private TextView weather_text = null;
    
    public WeatherTask(View view) {
        loading_container = (RelativeLayout) view.findViewById(R.id.loading_container);
        weather_container = (RelativeLayout) view.findViewById(R.id.weather_container);
        location_text = (TextView) view.findViewById(R.id.location_text);
        temperature_text = (TextView) view.findViewById(R.id.temperature_text);
        weather_text = (TextView) view.findViewById(R.id.weather_text);
    }
        
    @Override
    protected void onPreExecute() {
        loading_container.setVisibility(View.VISIBLE);
        weather_container.setVisibility(View.GONE);
    }

    @Override
    protected WeatherLocation doInBackground(Location... params) {        
        String url = String.format("http://forecast.weather.gov/MapClick.php?lat=%s&lon=%s&FcstType=json", params[0].getLatitude(), params[0].getLongitude());
        JSONObject object = JSONUtils.getJson(url);

        WeatherLocation weatherLocation = new WeatherLocation();

        try {
            JSONObject current = object.getJSONObject("currentobservation");
            weatherLocation.setTemp(current.getInt("Temp"));
            
            String city = current.getString("name");
            
            if (city.contains(",")) {
                weatherLocation.setCity(city.substring(0, city.indexOf(",")));
            } else {
                weatherLocation.setCity(city);
            }            
            
            weatherLocation.setState(current.getString("state"));
            weatherLocation.setWeather(current.getString("Weather"));
        } catch (Exception e) {}

        return weatherLocation;
    }
    
    @Override
    protected void onPostExecute(WeatherLocation weatherLocation) {
        
        loading_container.setVisibility(View.GONE);
        weather_container.setVisibility(View.VISIBLE);
        
        if (weatherLocation != null) {
            location_text.setText(String.format("%s, %s", weatherLocation.getCity(), weatherLocation.getState()));
            temperature_text.setText(String.format("%s°", weatherLocation.getTemp()));
            weather_text.setText(weatherLocation.getWeather());
        } else {
            location_text.setText("Error loading location.");
        }
    }

}
