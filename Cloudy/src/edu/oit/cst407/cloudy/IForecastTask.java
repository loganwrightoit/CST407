package edu.oit.cst407.cloudy;

import android.view.View;

public interface IForecastTask {

    public void onForecastTaskPreExecute(View view);
    
    public void onForecastTaskPostExecute(View view, MetaLocation metaLocation);
    
}
