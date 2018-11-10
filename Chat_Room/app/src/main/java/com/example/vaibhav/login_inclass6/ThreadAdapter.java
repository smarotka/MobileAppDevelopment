package com.example.vaibhav.login_inclass6;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ThreadAdapter extends ArrayAdapter<Thread> {
    Context context;
    ArrayList<Thread> threadList;
    LoginDetails userDetails;
    Thread t = new Thread();

    public ThreadAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Thread> objects, LoginDetails userDetails) {
        super(context, resource, objects);
        this.context = context;
        this.threadList = objects;
        this.userDetails = userDetails;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        t = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.thread_structure, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTxt = convertView.findViewById(R.id.titleTxt);
            viewHolder.delete = convertView.findViewById(R.id.deleteThread);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (t.userId.equalsIgnoreCase(userDetails.user_id)) {
            viewHolder.delete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.delete.setVisibility(View.INVISIBLE);
        }
        viewHolder.titleTxt.setText(t.title.toString());
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("User",userDetails);
                    json.put("thread",t);
                    //new DeleteThread().execute(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView titleTxt;
        ImageView delete;

    }
}
