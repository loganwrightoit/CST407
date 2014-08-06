package edu.oit.cst407.cloudy;

import java.util.ArrayList;
import java.util.Observable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;

public class CloudyUtil extends Observable implements IForecastTask {

    public final static CloudyUtil INSTANCE = new CloudyUtil();
    private ArrayList<MetaLocation> taskList = new ArrayList<MetaLocation>();
    public static Animation anim_fade_in;
    public static Animation anim_fade_out;

    private CloudyUtil() {}

    public boolean hasTask(MetaLocation metaLocation) {
        return taskList.contains(metaLocation);
    }
    
    /**
     * Creates forecast inquiry for location(s) and toggles refresh state
     * on ViewHolder item.
     * @param location
     */
    public void getForecast(CurrentConditionsViewHolder viewHolder, MetaLocation location) {
        if (!taskList.contains(location)) {
            viewHolder.refresh_container.setVisibility(View.VISIBLE);
            viewHolder.refresh_container.bringToFront();
            viewHolder.refresh_container.startAnimation(anim_fade_in);
            taskList.add(location);
            new ForecastTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, location);
        }
    }
    
    @Override
    public void onForecastTaskPostExecute(MetaLocation[] location) {
        for (MetaLocation metaLocation : location) {
            taskList.remove(metaLocation);
        }
        setChanged();
        notifyObservers();
    }
    
    public JSONObject getJson(String url) {
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

    public String getListAsJsonString(String key, ArrayList<MetaLocation> list) {
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

    public ArrayList<MetaLocation> getListFromJsonString(String string) {
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
