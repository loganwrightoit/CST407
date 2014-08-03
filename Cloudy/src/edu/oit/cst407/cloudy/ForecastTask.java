package edu.oit.cst407.cloudy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.os.AsyncTask;

public class ForecastTask extends AsyncTask<MetaLocation, Void, MetaLocation[]> {

	private IForecastTask taskCaller;
	    
    public ForecastTask(IForecastTask taskCaller) {
        this.taskCaller = taskCaller;
    }

    @Override
    protected MetaLocation[] doInBackground(MetaLocation... params) {
        for (MetaLocation location : params) {
        	MetaLocation metaLocation = location;
            String url = String.format("http://forecast.weather.gov/MapClick.php?lat=%s&lon=%s&FcstType=json", metaLocation.getLatitude(), metaLocation.getLongitude());
            JSONObject object = CloudyUtil.getJson(url);

            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US);
                
                Date date = dateFormat.parse(object.getString("creationDate"));
                metaLocation.setCreationDate(date);

                JSONObject current = object.getJSONObject("currentobservation");
                metaLocation.setCurrentWeather(current.getString("Weather"));
                metaLocation.setTemp(current.getInt("Temp"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return params;
    }
    
    @Override
    protected void onPostExecute(MetaLocation[] locations) {
        taskCaller.onForecastTaskPostExecute(locations);
    }

}
