package edu.oit.cst407.cloudy;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
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
        setContentView(R.layout.activity_search_results);

        progressBar = (ProgressBar) findViewById(id.refresh_bar);
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
            new LocationTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        }
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        LocationAdapter adapter = MainActivity.getAdapter();
        MetaLocation metaLocation = (MetaLocation) listView.getItemAtPosition(position);

        if (adapter.getList().contains(metaLocation)) {
            Toast.makeText(getApplicationContext(), String.format("%s, %s is already in list.", metaLocation.getCity(), metaLocation.getState()), Toast.LENGTH_SHORT).show();
            view.setEnabled(false);
        } else {
            Toast.makeText(getApplicationContext(), String.format("Added %s, %s to list.", metaLocation.getCity(), metaLocation.getState()), Toast.LENGTH_SHORT).show();
            adapter.addItem(metaLocation);

            // Close search activity
            finish();
        }
    }

    @Override
    public void onLocationTaskPostExecute(MetaLocation[] locations) {
        progressBar.setVisibility(View.INVISIBLE);

        if (locations.length > 0) {

            // Add locations to list, disable if already exists
            for (MetaLocation location : locations) {
                list.add(location);
            }

            // Notify adapter that list contains new items
            adapter.notifyDataSetChanged();

        } else {
            Toast.makeText(getApplicationContext(), "No matches found.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
