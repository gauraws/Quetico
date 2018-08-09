package com.example.android.quetico;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class QueticoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Quetico>> {

    public static final String LOG_TAG = QueticoActivity.class.getName();
    private static final String NEWS_API_REQUEST_URL = "https://newsapi.org/v1/articles";
    public static final int NEWS_LOADER_ID = 1;
    private static QueticoAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_quetico);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (!isConnected) {
            ProgressBar pb1 = (ProgressBar) findViewById(R.id.progress_indicator);
            pb1.setVisibility(View.GONE);
            TextView emptyView = (TextView) findViewById(R.id.emptyView);
            emptyView.setText("No internet connection.");

        } else {
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(NEWS_LOADER_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.preferences) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if (id == R.id.about) {
            Intent settingsIntent = new Intent(this, AboutActivity.class);
            startActivity(settingsIntent);
            return true;

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public Loader<List<Quetico>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortByStoredValue = sharedPrefs.getString("sort_by", "top");

        String sourceNameStoredValue = sharedPrefs.getString("source", "ars-technica");
        Uri uri = Uri.parse(NEWS_API_REQUEST_URL);
        Uri.Builder builder = uri.buildUpon();
        builder.appendQueryParameter("source", sourceNameStoredValue);
        builder.appendQueryParameter("sortBy", sortByStoredValue);
        builder.appendQueryParameter("apiKey", "08baa1bbbfb14f04a248621c9e9520ea");

        return new QueticoLoader(this, builder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Quetico>> loader, List<Quetico> data) {

        ProgressBar pb = (ProgressBar) findViewById(R.id.progress_indicator);
        pb.setVisibility(View.GONE);

        TextView emptyView = (TextView) findViewById(R.id.emptyView);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setEmptyView(emptyView);
        emptyView.setText("No news at this point. Stay tuned!");

        if (data != null) {

            mAdapter = new QueticoAdapter(this, data);
            listView.setAdapter(mAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Quetico newsObj = mAdapter.getItem(position);
                    Uri uri = Uri.parse(newsObj.getUrl());
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    //Intent i = new Intent(QueticoActivity.this,NewsDetailsActivity.class);

                    startActivity(i);

                }
            });


        }


    }


    @Override
    public void onLoaderReset(Loader<List<Quetico>> loader) {

        if (mAdapter != null) {

            mAdapter.clear();
        }


    }
}


