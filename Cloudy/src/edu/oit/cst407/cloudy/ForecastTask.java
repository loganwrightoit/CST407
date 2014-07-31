package edu.oit.cst407.cloudy;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.view.View;

public class ForecastTask extends AsyncTask<MetaViewLocation, Void, MetaViewLocation[]> {

	private IForecastTask taskCaller;
	private View view;
	    
    public ForecastTask(View view, IForecastTask taskCaller) {
        this.taskCaller = taskCaller;
        this.view = view;
    }
        
    @Override
    protected void onPreExecute() {
        taskCaller.onForecastTaskPreExecute(view);
    }

    @Override
    protected MetaViewLocation[] doInBackground(MetaViewLocation... params) {
        for (MetaViewLocation location : params) {
            MetaLocation metaLocation = location.getMetaLocation();
            String url = String.format("http://forecast.weather.gov/MapClick.php?lat=%s&lon=%s&FcstType=json", metaLocation.getLatitude(), metaLocation.getLongitude());
            JSONObject object = CloudyUtil.getJson(url);

            try {
                JSONObject current = object.getJSONObject("currentobservation");
                metaLocation.setWeather(current.getString("Weather"));
                metaLocation.setTemp(current.getInt("Temp"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return params;
    }
    
    @Override
    protected void onPostExecute(MetaViewLocation[] locations) {
        taskCaller.onForecastTaskPostExecute(locations);
    }

}
