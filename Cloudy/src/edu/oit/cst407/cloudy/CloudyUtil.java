package edu.oit.cst407.cloudy;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CloudyUtil {

    public static JSONObject getJson(String url) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            return new JSONObject(EntityUtils.toString(entity));
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getListAsJsonString(String key, ArrayList<MetaLocation> list) {
        JSONObject object = new JSONObject();

        try {
            JSONArray jsonArray = new JSONArray();
            for (int idx = 0; idx < list.size(); ++idx) {
                jsonArray.put(idx, list.get(idx).toJSONObject());
            }
            object.put(key, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }

    public static ArrayList<MetaLocation> getListFromJsonString(String string) {
        ArrayList<MetaLocation> list = new ArrayList<MetaLocation>();

        try {
            JSONObject object = new JSONObject(string);            
            JSONArray locations = object.getJSONArray("locations");

            for (int idx = 0; idx < locations.length(); ++idx) {
                JSONObject location = locations.getJSONObject(idx);
                list.add(MetaLocation.fromJSONObject(location));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
}
