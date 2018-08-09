package com.example.android.quetico;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NICK GS on 5/3/2017.
 */

public class QueticoLoader extends AsyncTaskLoader<List<Quetico>> {

    private static final String LOG_TAG = QueticoLoader.class.getSimpleName();
    private String mUrl;


    public QueticoLoader(Context context, String url) {
        super(context);
        mUrl = url;

    }

    @Override
    protected void onStartLoading() {
        Log.e(LOG_TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    public List<Quetico> loadInBackground() {
        if (mUrl == null)
            return null;

        else {

            ArrayList<Quetico> data = QueryUtils.fetchNewsData(mUrl);
            return data;
        }


    }
}
