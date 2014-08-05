package edu.oit.cst407.cloudy;

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
            metaLocation.getWeatherData().parse(object.toString());
        }

        return params;
    }

    @Override
    protected void onPostExecute(MetaLocation[] locations) {
        taskCaller.onForecastTaskPostExecute(locations);
    }

}
