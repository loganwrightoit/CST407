package edu.oit.cst407.cloudy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends Activity {

    public final static String KEY_POSITION = "POSITION";
    
    private MetaLocation metaLocation = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        handleIntent(getIntent());

        TextView detail_location_text = (TextView) findViewById(R.id.detail_location_text);
        
        detail_location_text.setText(String.format("%s, %s", metaLocation.getCity(), metaLocation.getState()));
    }
    
    private void handleIntent(Intent intent) {
        int position = Integer.parseInt(intent.getStringExtra(KEY_POSITION));
        LocationAdapter adapter = MainActivity.getAdapter();
        metaLocation = adapter.getItem(position);
    }
    
}
