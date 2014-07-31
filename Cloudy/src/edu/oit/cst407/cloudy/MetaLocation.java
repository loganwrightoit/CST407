package edu.oit.cst407.cloudy;

import org.json.JSONException;
import org.json.JSONObject;

public class MetaLocation {
    
    private String city = "null";
    private String state = "null";
    private double lat;
    private double lng;
    private String weather = "Error loading location data.";
    private int temperature;
    
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
    
    @Override
    public String toString() {
        return String.format("%s, %s", city, state);
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
    
    public String getWeather() {
        return this.weather;
    }
    
    public void setTemp(int temp) {
        this.temperature = temp;
    }

    public int getTemp() {
        return temperature;
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
    
    public JSONObject toJSONObject(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("city", city);
            jsonObject.put("state", state);
            jsonObject.put("lat", lat);
            jsonObject.put("lng", lng);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}