package com.example.android.quetico;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by NICK GS on 5/3/2017.
 */

public class QueticoAdapter extends ArrayAdapter<Quetico> {


    private static StringBuilder timeBuilder = new StringBuilder();
    public static final String LOG_TAG = QueticoAdapter.class.getName();
    private SimpleDateFormat sdf;

    public QueticoAdapter(Activity context, List<Quetico> l) {

        super(context, 0, l);


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            v = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        }
        try {
            Quetico newsObj = getItem(position);

            TextView titleTextView = (TextView) v.findViewById(R.id.title);
            String title = newsObj.getTitle();
            if (!title.equals("null")) {
                titleTextView.setText(title);
            } else {
                titleTextView.setVisibility(View.GONE);
            }


            TextView authorTextView = (TextView) v.findViewById(R.id.author);
            String author = newsObj.getAuthor();
            if (author.equals("null") || author.contains("http"))
                authorTextView.setVisibility(View.GONE);
            else {
                if (author.contains("\n")) {
                    String[] arr = author.split(" ");
                    author = arr[0];

                }

                authorTextView.setText(author);
            }


            TextView descriptionTextView = (TextView) v.findViewById(R.id.description);
            String description = newsObj.getDescription();
            if (!description.equals("null")) {
                descriptionTextView.setText(description);
            } else {
                descriptionTextView.setVisibility(View.GONE);
            }


            TextView timeTextView = (TextView) v.findViewById(R.id.time);
            String t = newsObj.getTime();
            if (!t.equals("null")) {
                if (t.charAt(19) == '.') {


                    sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                } else if (t.charAt(19) == '+') {


                    sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+'SS:SS");
                } else {
                    sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

                }

                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = sdf.parse(t);
                SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd, yyyy");
                String formattedDate = sdf1.format(date);

                SimpleDateFormat sdf2 = new SimpleDateFormat("h:mm a ");
                String formattedTime = sdf2.format(date);

                timeBuilder.append(formattedDate);
                timeBuilder.append(", ");
                timeBuilder.append(formattedTime);
                String finalTimeToDisplay = timeBuilder.toString();

                timeTextView.setText(finalTimeToDisplay);

                timeBuilder.delete(0, timeBuilder.length());
            } else {
                timeTextView.setVisibility(View.GONE); // if value received is "null"
            }


        } catch (Exception e) {
            Log.e(LOG_TAG, "Parse Exception handled gracefully");

        }

        return v;
    }
}
