package edu.oit.cst407.cloudy;

import java.util.ArrayList;

public interface ILocationTask {

    public void onLocationTaskPreExecute();
    
    public void onLocationTaskPostExecute(ArrayList<MetaLocation> list);
    
}
