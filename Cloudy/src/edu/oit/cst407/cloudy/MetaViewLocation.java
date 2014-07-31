package edu.oit.cst407.cloudy;

import android.view.View;

public class MetaViewLocation {

    private View view;
    private MetaLocation metaLocation;
    
    public MetaViewLocation(MetaLocation metaLocation, View view) {
        this.view = view;
        this.metaLocation = metaLocation;
    }
    
    public MetaLocation getMetaLocation() {
        return metaLocation;
    }
    
    public View getView() {
        return view;
    }
    
}
