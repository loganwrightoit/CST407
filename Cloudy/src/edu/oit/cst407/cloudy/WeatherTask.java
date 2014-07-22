package edu.oit.cst407.cloudy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;

public class WeatherTask extends AsyncTask<Location, Void, WeatherLocation> {

    public static JSONObject getJson(String url) {
        InputStream is = null;
        String result = "";
        JSONObject jsonObject = null;

        // Grab content of JSON
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch(Exception e) {
            return null;
        }

        // Read JSON into string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch(Exception e) {
            return null;
        }

        // Convert string to JSONObject
        try {
            jsonObject = new JSONObject(result);
        } catch(JSONException e) {
            return null;
        }

        return jsonObject;

    }

    @Override
    protected WeatherLocation doInBackground(Location... params) {
        MainActivity.textView.setText("WeatherTask is processing a location.");
        String url = String.format("http://forecast.weather.gov/MapClick.php?lat=%s&lon=%s&FcstType=json", params[0].getLatitude(), params[0].getLongitude());
        JSONObject object = getJson(url);

        WeatherLocation weatherLocation = new WeatherLocation();

        try {
            JSONObject current = object.getJSONObject("currentobservation");
            weatherLocation.setTemp(current.getInt("Temp"));
        } catch (JSONException e) {}

        return weatherLocation;
    }

    @Override
    protected void onPostExecute(WeatherLocation result) {
        MainActivity.textView.setText("Outside temperature: " + result.getTemp());
    }

}
