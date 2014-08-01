package edu.oit.cst407.cloudy;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

public class MetaLocation {
    
	private Date date = null;
	
    private String city = "null";
    private String state = "null";
    private double lat;
    private double lng;
    private String weather = "";
    private int temp;

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

    public String getWeather() {
        return this.weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
        
    public int getTemp() {
        return temp;
    }
    
    public void setTemp(int temp) {
        this.temp = temp;
    }

    public void setDate(Date date) {
    	this.date = date;
    }
    
    /**
     * Checks whether the last forecast refresh date is outdated.
     * @return whether data is outdated
     */
	public boolean hasExpired() {
		if (date != null) {
			Date currentDate = new Date();
			long duration  = currentDate.getTime() - date.getTime();
			long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);			
			return minutes > 15;
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
    
    public JSONObject toJSONObject(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("city", city);
            jsonObject.put("state", state);
            jsonObject.put("lat", lat);
            jsonObject.put("lng", lng);
            jsonObject.put("weather", weather);
            jsonObject.put("temp", temp);

            if (date != null) {
	            DateFormat dateFormat = DateFormat.getDateInstance();
	            jsonObject.put("date", dateFormat.format(date));
            }
            
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}