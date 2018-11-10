package com.example.vaibhav.newsfeeds;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, int resource, List<News> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News news = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageViewObject = (ImageView) convertView.findViewById(R.id.urlToImage);
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.textViewAuthor = (TextView) convertView.findViewById(R.id.author);
            viewHolder.textViewDate = (TextView) convertView.findViewById(R.id.newsDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!news.urlToImage.equals("null") && !news.urlToImage.equals(""))
            Picasso.get().load(news.urlToImage).into(viewHolder.imageViewObject);


        if (!news.title.equals("null") && !news.title.equals("")) {
            viewHolder.textViewTitle.setText(news.title);
        } else {
            viewHolder.textViewTitle.setText("Not Available");
        }

        if (!news.author.equals("null") && !news.author.equals("")) {
            viewHolder.textViewAuthor.setText(news.author);
        } else {
            viewHolder.textViewAuthor.setText("Not Available");
        }

        if (!news.publishedAt.equals("null") && !news.publishedAt.equals("")) {
            try {
                viewHolder.textViewDate.setText(new SimpleDateFormat("yyyy-mm-dd").format((new SimpleDateFormat("yyyy-mm-dd").parse(news.publishedAt))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            viewHolder.textViewDate.setText("Not Available");
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageViewObject;
        TextView textViewTitle;
        TextView textViewAuthor;
        TextView textViewDate;
    }
}
