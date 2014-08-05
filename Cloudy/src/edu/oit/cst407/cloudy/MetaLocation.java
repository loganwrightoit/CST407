package edu.oit.cst407.cloudy;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MetaLocation {

    private WeatherGovData weatherData = new WeatherGovData();
    private String city = "";
    private String state = "";
    private double lat;
    private double lng;
    private Date lastUpdate = null;

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
    
    public WeatherGovData getWeatherData() {
        return weatherData;
    }
    
    public void setWeatherData(WeatherGovData data) {
        weatherData = data;
    }

    /**
     * Checks whether the last forecast refresh date is outdated.
     * @return whether data is outdated
     */
    public boolean hasExpired() {
        Date currentDate = new Date();

        /*
         * If last update was performed less than 15 minutes ago,
         * do not attempt to update weather data.
         */
        if (lastUpdate != null) {
            long duration  = currentDate.getTime() - lastUpdate.getTime();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            if (minutes < 15) {
                return false;
            }
        }
        
        /*
         * If creation date is over an hour old, attempt to grab updated
         * weather data.
         */
        if (weatherData != null) {
            Date creationDate = weatherData.getCreationDate();
            if (creationDate != null) {
                long duration  = currentDate.getTime() - currentDate.getTime();
                long hours = TimeUnit.MILLISECONDS.toHours(duration);
                lastUpdate = currentDate;
                return hours > 1;
            }
        }

        return true;
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("city", city);
            jsonObject.put("state", state);
            jsonObject.put("lat", lat);
            jsonObject.put("lng", lng);
            jsonObject.put("weatherData", weatherData.toString());
            jsonObject.put("lastUpdate", DateFormat.getDateTimeInstance().format(lastUpdate));

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
            String weatherData = object.getString("weatherData");
            Date lastUpdate = DateFormat.getDateTimeInstance().parse(object.getString("lastUpdate"));
            
            Log.d("DEBUG", "fromJSONObject() lastUpdate = " + lastUpdate);

            MetaLocation metaLocation = new MetaLocation(city, state, lat, lng);
            metaLocation.weatherData.parse(weatherData);
            metaLocation.lastUpdate = lastUpdate;

            return metaLocation;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    

}
