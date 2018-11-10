package com.example.vaibhav.newsfeeds;

import java.io.Serializable;
import java.util.ArrayList;

public class Source implements Serializable {
    public  String source_id;
    public  String source_Name;
    public  ArrayList<News> NewsFeeds;

    @Override
    public String toString() {
        return source_Name;
    }
}
