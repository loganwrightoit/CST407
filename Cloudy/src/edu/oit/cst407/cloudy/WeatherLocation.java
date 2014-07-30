package edu.oit.cst407.cloudy;

import android.location.Location;

public class WeatherLocation {

    private String city = "";
    private String state = "";
    private String weather = "";
    private int temperature;
    
    Location location;
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public Location getLocation() {
        return location;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    public String getCity() {
        return this.city;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getState() {
        return this.state;
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

}
