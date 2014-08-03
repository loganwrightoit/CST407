package edu.oit.cst407.cloudy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MetaLocation {

    private Date weatherDate = null;
    
    private String city = "";
    private String state = "";
    private double lat;
    private double lng;
    private String weatherData = "";

    public MetaLocation(String city, String state, double lat, double lng) {
        this.city = city;
        this.state = state;
        this.lat = lat;
        this.lng = lng;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lng;
    }
    
    public Date getWeatherDate() {
        return weatherDate;
    }
    
    public void setWeatherDate(Date date) {
        this.weatherDate = date;
    }
    
    public String getWeatherData() {
        return weatherData;
    }
    
    public void setWeatherData(String weatherData) {
        this.weatherData = weatherData;

        if (weatherDate == null) {
            JSONObject object= new JSONObject();
            try {
                if (object.has("date")) {
                    DateFormat dateFormat = DateFormat.getDateInstance();
                    weatherDate = dateFormat.parse(object.getString("date"));
                }
            } catch (Exception e) {}
        }
    }

    /**
     * Checks whether the last forecast refresh date is outdated.
     * @return whether data is outdated
     */
    public boolean hasExpired() {
        if (weatherDate != null) {
            Date currentDate = new Date();
            long duration  = currentDate.getTime() - weatherDate.getTime();
            long hours = TimeUnit.MILLISECONDS.toHours(duration);
            return hours >= 1;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return String.format("%s, %s", city, state);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof MetaLocation)) {
            return false;
        }
        MetaLocation lhs = (MetaLocation) object;
        return toString().equals(lhs.toString());
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("city", city);
            jsonObject.put("state", state);
            jsonObject.put("lat", lat);
            jsonObject.put("lng", lng);
            
            if (weatherDate != null) {
                DateFormat dateFormat = DateFormat.getDateInstance();
                jsonObject.put("date", dateFormat.format(weatherDate));
            }
            
            jsonObject.put("weatherData", weatherData);

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static MetaLocation fromJSONObject(JSONObject object) {
        try {
            String city = object.getString("city");
            String state = object.getString("state");
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            
            Date date = null;
            if (object.has("date")) {
                DateFormat dateFormat = DateFormat.getDateInstance();
                date = dateFormat.parse(object.getString("date"));
            }
            
            String weatherData = object.getString("weatherData");
            
            MetaLocation metaLocation = new MetaLocation(city, state, lat, lng);
            metaLocation.setWeatherData(weatherData);
            metaLocation.setWeatherDate(date);
            
            return metaLocation;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Date getCreationDate() throws JSONException, ParseException {
        JSONObject object = new JSONObject(weatherData); 
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US);
        Date date = dateFormat.parse(object.getString("creationDate"));
        return date;
    }
    
    public int getCurrentTemperature() throws JSONException {
        JSONObject object = new JSONObject(weatherData); 
        JSONObject current = object.getJSONObject("currentobservation");
        return current.getInt("Temp");
    }
    
    public String getCurrentWeather() throws JSONException {
        JSONObject object = new JSONObject(weatherData); 
        JSONObject current = object.getJSONObject("currentobservation");
        return current.getString("Weather");
    }
    
    public String[] getWeatherPeriods() throws JSONException {
        JSONObject object = new JSONObject(weatherData); 
        JSONObject time = object.getJSONObject("time");
        JSONArray array_startPeriodName = time.getJSONArray("startPeriodName");
                        
        ArrayList<String> extStartPeriodName = new ArrayList<String>();

        for (int idx = 0; idx < array_startPeriodName.length(); ++idx) {
            extStartPeriodName.add(array_startPeriodName.getString(idx));
        }

        Object[] objectArray = extStartPeriodName.toArray();

        return Arrays.copyOf(objectArray, objectArray.length, String[].class);
    }
    
    public String[] getWeatherPeriodConditions() throws JSONException {
        JSONObject object = new JSONObject(weatherData);
        JSONObject data = object.getJSONObject("data");
        JSONArray array_text = data.getJSONArray("text");
                       
        ArrayList<String> extText = new ArrayList<String>();

        for (int idx = 0; idx < array_text.length(); ++idx) {
            extText.add(array_text.getString(idx));
        }

        Object[] objectArray = extText.toArray();         
        
        return Arrays.copyOf(objectArray, objectArray.length, String[].class);
    }

}
