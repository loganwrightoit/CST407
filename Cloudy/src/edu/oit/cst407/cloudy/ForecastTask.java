package edu.oit.cst407.cloudy;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ForecastTask extends AsyncTask<MetaLocation, Void, MetaLocation> {

	IForecastTask taskCaller;
	View view;
	    
    public ForecastTask(IForecastTask taskCaller, View view) {
        this.taskCaller = taskCaller;
        this.view = view;
    }
        
    @Override
    protected void onPreExecute() {
        taskCaller.onForecastTaskPreExecute(view);
    }

    @Override
    protected MetaLocation doInBackground(MetaLocation... params) {
        if (params != null) {
            String url = String.format("http://forecast.weather.gov/MapClick.php?lat=%s&lon=%s&FcstType=json", params[0].getLatitude(), params[0].getLongitude());
            JSONObject object = CloudyUtil.getJson(url);
    
            MetaLocation metaLocation = params[0];
    
            try {
                JSONObject current = object.getJSONObject("currentobservation");
                metaLocation.setWeather(current.getString("Weather"));
                metaLocation.setTemp(current.getInt("Temp"));
            } catch (Exception e) {
                Toast.makeText(view.getContext(), String.format("Could not update location %s, %s.", metaLocation.getCity(), metaLocation.getState()), Toast.LENGTH_SHORT).show();
            }
            
            return metaLocation;
        } else {
            Log.d("DEBUG", "doInBackground passed null params in ForecastTask.");
        }

        return null;
    }
    
    @Override
    protected void onPostExecute(MetaLocation metaLocation) {
        taskCaller.onForecastTaskPostExecute(view, metaLocation);
    }

}
