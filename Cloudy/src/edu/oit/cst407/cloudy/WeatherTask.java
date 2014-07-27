package edu.oit.cst407.cloudy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WeatherTask extends AsyncTask<Location, Void, WeatherLocation> {

    private ProgressBar loading_bar = null;
    private TextView location_text = null;
    private TextView temperature_text = null;
    private TextView weather_text = null;
    
    public WeatherTask(View view) {
        loading_bar = (ProgressBar) view.findViewById(R.id.loading_bar);
        location_text = (TextView) view.findViewById(R.id.location_text);
        temperature_text = (TextView) view.findViewById(R.id.temperature_text);
        weather_text = (TextView) view.findViewById(R.id.weather_text);
    }
    
    private JSONObject getJson(String url) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            return new JSONObject(EntityUtils.toString(entity));
        } catch(Exception e) {
            return null;
        }
    }
    
    @Override
    protected void onPreExecute() {
        loading_bar.setVisibility(View.VISIBLE);
        temperature_text.setText("Grabbing details for location...");
    }

    @Override
    protected WeatherLocation doInBackground(Location... params) {        
        String url = String.format("http://forecast.weather.gov/MapClick.php?lat=%s&lon=%s&FcstType=json", params[0].getLatitude(), params[0].getLongitude());
        JSONObject object = getJson(url);

        WeatherLocation weatherLocation = new WeatherLocation();

        try {
            JSONObject current = object.getJSONObject("currentobservation");
            weatherLocation.setTemp(current.getInt("Temp"));
            
            String city = current.getString("name");
            int trimIdx = city.indexOf(",");
            
            weatherLocation.setCity(city.substring(0, trimIdx));
            weatherLocation.setState(current.getString("state"));
            weatherLocation.setWeather(current.getString("Weather"));
        } catch (JSONException e) {}

        return weatherLocation;
    }

    @Override
    protected void onPostExecute(WeatherLocation weatherLocation) {
        
        loading_bar.setVisibility(View.INVISIBLE);
        
        if (weatherLocation != null) {
            location_text.setText(String.format("%s, %s", weatherLocation.getCity(), weatherLocation.getState()));
            temperature_text.setText(String.format("%s°", weatherLocation.getTemp()));
            weather_text.setText(weatherLocation.getWeather());
        } else {
            location_text.setText("Error loading location.");
        }
    }

}
