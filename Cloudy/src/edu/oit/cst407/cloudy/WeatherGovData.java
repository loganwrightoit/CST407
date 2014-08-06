package edu.oit.cst407.cloudy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherGovData {

    public String jsonString;
    public Date creationDate;
    public String[] periods;
    public String[] periodConditions;
    public String currentWeather;
    public int currentTemperature;

    public WeatherGovData() {}

    public void parse(String weatherData) {
        jsonString = weatherData;

        try {
            JSONObject object = new JSONObject(weatherData);

            /* Grab forecast creation date */

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US);
            creationDate = dateFormat.parse(object.getString("creationDate"));

            /* Grab current conditions */

            JSONObject current = object.getJSONObject("currentobservation");
            currentTemperature = current.getInt("Temp");
            currentWeather = current.getString("Weather");

            /* Grab future forecast periods */

            JSONObject time = object.getJSONObject("time");
            JSONArray array_startPeriodName = time.getJSONArray("startPeriodName");
            ArrayList<String> extStartPeriodName = new ArrayList<String>();
            for (int idx = 0; idx < array_startPeriodName.length(); ++idx) {
                extStartPeriodName.add(array_startPeriodName.getString(idx));
            }
            Object[] tempArray = extStartPeriodName.toArray();
            periods = Arrays.copyOf(tempArray, tempArray.length, String[].class);

            /* Grab future forecast */

            JSONObject data = object.getJSONObject("data");
            JSONArray array_text = data.getJSONArray("text");
            ArrayList<String> extText = new ArrayList<String>();
            for (int idx = 0; idx < array_text.length(); ++idx) {
                extText.add(array_text.getString(idx));
            }
            Object[] tempArray1 = extText.toArray();
            periodConditions = Arrays.copyOf(tempArray1, tempArray1.length, String[].class);

        } catch (Exception e) {}

    }

    @Override
    public String toString() {
        return jsonString;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String[] getForecastPeriods() {
        return periods;
    }

    public String[] getForecastPeriodConditions() {
        return periodConditions;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public int getCurrentTemperature() {
        return currentTemperature;
    }

}
