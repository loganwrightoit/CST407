package edu.oit.cst407.cloudy;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import edu.oit.cst407.cloudy.R.id;

public class SearchResultsActivity extends ListActivity {

    private ProgressBar progressBar = null;    
    private ArrayAdapter<LocationResult> adapter = null;
    
    public class LocationResult {
        
        private String city = "null";
        private String state = "null";
        private double lat;
        private double lng;
        
        public LocationResult(String city, String state, double lat, double lng) {
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
        
    }
    
    private ArrayList<LocationResult> resultList = new ArrayList<LocationResult>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresults);
        
        progressBar = (ProgressBar) findViewById(id.loading_bar);
        
        adapter = new ArrayAdapter<LocationResult>(this, android.R.layout.simple_list_item_1, resultList);
        setListAdapter(adapter); 
        
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            new SearchTask().execute(query);
        }
    }
    
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        LocationResult locationResult = (LocationResult) listView.getItemAtPosition(position);      

        Location location = new Location(LocationManager.PASSIVE_PROVIDER);
        location.setLatitude(locationResult.getLatitude());
        location.setLongitude(locationResult.getLongitude());
        
        MainActivity.locationList.add(new WeatherLocation());
        MainActivity.adapter.notifyDataSetChanged();
        int idx = MainActivity.weatherList.getChildCount() - 1;
        
        new WeatherTask(MainActivity.weatherList.getChildAt(idx)).execute(location);
        
        // Notify mainactivity of added location here.
        Toast.makeText(getApplicationContext(), String.format("%s, %s added to locations", locationResult.getCity(), locationResult.getState()), Toast.LENGTH_SHORT).show();
        finish();
    }
    
    public class SearchTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            return JSONUtils.getJson(String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s", params[0]));
        }
        
        @Override
        protected void onPostExecute(JSONObject object) {
            progressBar.setVisibility(View.GONE);
            addSearchResults(object);
            
            // Notify adapter that list contains new items
            adapter.notifyDataSetChanged();
        }

    }
    
    private void addSearchResults(JSONObject object)
    {
        try {

            if (object.getString("status").equals("OK")) {
                
                JSONArray results = object.getJSONArray("results");
                for (int idx = 0; idx < results.length(); ++idx) {
                    
                    String city = "";
                    String state = "";

                    JSONObject current = results.getJSONObject(idx);
                    JSONObject location = current.getJSONObject("geometry").getJSONObject("location");
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");

                    JSONArray address = current.getJSONArray("address_components");
                    for (int idx1 = 0; idx1 < address.length(); ++idx1) {
                        
                        JSONObject component = address.getJSONObject(idx1);
                        
                        JSONArray type = component.getJSONArray("types");
                        for (int idx2 = 0; idx2 < type.length(); ++idx2) {
                            
                            String typeVal = type.getString(idx2);
                            if ("administrative_area_level_1".equals(typeVal)) {
                                state = component.getString("long_name");
                            }
                            if ("locality".equals(typeVal)) {
                                city = component.getString("long_name");
                            }
                            
                        }
                        
                    }

                    resultList.add(new LocationResult(city, state, lat, lng));
                    
                }
            } else {
                
                Toast.makeText(getApplicationContext(), "No matches found.", Toast.LENGTH_SHORT).show();
                finish();
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*

   "results" : [
      {
         "address_components" : [
            {
               "long_name" : "Mistymorning Avenue Southeast",
               "short_name" : "Mistymorning Ave SE",
               "types" : [ "route" ]
            },
            {
               "long_name" : "South Gateway",
               "short_name" : "South Gateway",
               "types" : [ "neighborhood", "political" ]
            },
            {
               "long_name" : "Salem",
               "short_name" : "Salem",
               "types" : [ "locality", "political" ]
            },
            {
               "long_name" : "Marion County",
               "short_name" : "Marion County",
               "types" : [ "administrative_area_level_2", "political" ]
            },
            {
               "long_name" : "Oregon",
               "short_name" : "OR",
               "types" : [ "administrative_area_level_1", "political" ]
            },
            {
               "long_name" : "United States",
               "short_name" : "US",
               "types" : [ "country", "political" ]
            },
            {
               "long_name" : "97306",
               "short_name" : "97306",
               "types" : [ "postal_code" ]
            }
         ],
         "formatted_address" : "2091 Mistymorning Avenue Southeast, Salem, OR 97306, USA",
         "geometry" : {
            "location" : {
               "lat" : 44.867809,
               "lng" : -123.015813
            },
            "location_type" : "ROOFTOP",
            "viewport" : {
               "northeast" : {
                  "lat" : 44.8691579802915,
                  "lng" : -123.0144640197085
               },
               "southwest" : {
                  "lat" : 44.86646001970851,
                  "lng" : -123.0171619802915
               }
            }
         },
         "types" : [ "street_address" ]
      }
   ],
   "status" : "OK"

     */
    
}
