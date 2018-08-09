package com.example.android.quetico;

/**
 * Created by NICK GS on 5/3/2017.
 */

public class Quetico {


    private String title;
    private String author;
    private String time;
    private String description;
    public  String url;
    private String image;

    public Quetico(String t, String a, String tm, String d, String u, String img) {
        this.title = t;
        this.author = a;
        this.time = tm;
        this.description = d;
        this.url = u;
        this.image = img;
    }


    public String getTitle() {
        return title;

    }

    public String getAuthor() {
        return author;

    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

}
