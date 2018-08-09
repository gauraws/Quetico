package com.example.android.quetico;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by NICK GS on 5/3/2017.
 */

public class QueryUtils {


    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static ArrayList<Quetico> fetchNewsData(String requestUrl) {

        ArrayList<Quetico> list = null;
        try {
            URL url = createUrl(requestUrl);
            if (url == null) {
                return null;
            } else {
                String jsonResponse = null;
                jsonResponse = makeHttpRequest(url);

                list = extractNewsListFromJSON(jsonResponse);
                Log.e(LOG_TAG, "data fetched");
                //Thread.sleep(3000);

            }


        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem fetching the earthquake data", e);
        }

        return list;
    }


    private static URL createUrl(String requestUrl) {
        URL url = null;

        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem forming the url", e);
        }


        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                Log.e(LOG_TAG, "Successful HTTP cycle. The http response code is: " + urlConnection.getResponseCode());
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Unsuccessful HTTP cycle. The http response code is: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the http request", e);

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }


        return jsonResponse;
    }


    private static String readFromInputStream(InputStream is) throws IOException {

        StringBuilder sb = new StringBuilder();
        try {

            InputStreamReader r = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(r);
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException while trying to read input from stream", e);
        }

        return sb.toString();
    }

    public static ArrayList<Quetico> extractNewsListFromJSON(String jsonResponse) {

        ArrayList<Quetico> newsList = null;
        if (jsonResponse.length() == 0)
            return newsList;

        try {
            Log.e(LOG_TAG, "The response is: " + jsonResponse);
            newsList = new ArrayList<>();
            JSONObject response = new JSONObject(jsonResponse);
            JSONArray articles = response.getJSONArray("articles");
            Quetico newsObj;
            for (int i = 0; i < articles.length(); i++) {
                JSONObject articleJsonObject = articles.getJSONObject(i);

                String title = articleJsonObject.getString("title");
                String author = articleJsonObject.getString("author");
                String time = articleJsonObject.getString("publishedAt");
                String description = articleJsonObject.getString("description");
                String url = articleJsonObject.getString("url");
                String image = articleJsonObject.getString("urlToImage");

                newsObj = new Quetico(title, author, time, description, url, image);

                newsList.add(newsObj);

            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        // Return the list of earthquakes
        return newsList;

    }
}
