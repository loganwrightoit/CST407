package edu.oit.cst407.cloudy;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

public class LocationTask extends AsyncTask<String, Void, MetaLocation[]> {

    ILocationTask taskCaller;

    public LocationTask(ILocationTask taskCaller) {
        this.taskCaller = taskCaller;
    }

    @Override
    protected MetaLocation[] doInBackground(String... params) {
        JSONObject object = CloudyUtil.INSTANCE.getJson(params[0]);
        ArrayList<MetaLocation> list = new ArrayList<MetaLocation>();

        try {

            if (object.getString("status").equals("OK")) {

                JSONArray results = object.getJSONArray("results");
                for (int idx = 0; idx < results.length(); ++idx) {

                    String city = "";
                    String state = "";
                    String country = "";

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
                                state = component.getString("short_name");
                            }
                            if ("locality".equals(typeVal)) {
                                city = component.getString("short_name");
                            }
                            if ("country".equals(typeVal)) {
                                country = component.getString("short_name");
                            }

                        }

                    }

                    if (country.equals("US") || country.equals("United States")) {
                        list.add(new MetaLocation(city, state, lat, lng));
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Object[] objectArray = list.toArray();
        MetaLocation[] locations = Arrays.copyOf(objectArray, objectArray.length, MetaLocation[].class);

        return locations;
    }

    @Override
    protected void onPostExecute(MetaLocation[] locations) {
        taskCaller.onLocationTaskPostExecute(locations);
    }

}
