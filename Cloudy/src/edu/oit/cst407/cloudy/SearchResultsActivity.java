package edu.oit.cst407.cloudy;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import edu.oit.cst407.cloudy.R.id;

public class SearchResultsActivity extends ListActivity implements ILocationTask {

    private ProgressBar progressBar = null;    
    private ArrayAdapter<MetaLocation> adapter = null;
    private ArrayList<MetaLocation> list = new ArrayList<MetaLocation>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresults);
        
        progressBar = (ProgressBar) findViewById(id.loading_bar);
        adapter = new ArrayAdapter<MetaLocation>(this, android.R.layout.simple_list_item_1, list);
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
            String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s", query.replace(" ", "+"));
            new LocationTask(this).execute(url);
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        MetaLocation metaLocation = (MetaLocation) listView.getItemAtPosition(position);
        MainActivity.getList().add(metaLocation);
        MainActivity.getAdapter().notifyDataSetChanged();
        
        // Close search activity
        finish(); 
    }

	@Override
	public void onLocationTaskPreExecute() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLocationTaskPostExecute(ArrayList<MetaLocation> inList) {
		progressBar.setVisibility(View.INVISIBLE);

		if (!inList.isEmpty()) {

    		// Add locations to list, disable if already exists
    		for (MetaLocation location : inList) {
    		    list.add(0, location);
    		}
            
            // Notify adapter that list contains new items
            adapter.notifyDataSetChanged();
            
		} else {
		    Toast.makeText(getApplicationContext(), "No matches found.", Toast.LENGTH_SHORT).show();
            finish();
		}
	}

}
