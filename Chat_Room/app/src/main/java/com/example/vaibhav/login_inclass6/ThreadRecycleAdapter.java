package com.example.vaibhav.login_inclass6;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ThreadRecycleAdapter extends RecyclerView.Adapter<ThreadRecycleAdapter.ViewHolder> {
    public ArrayList<Thread> threadList = null;
    LoginDetails loginDetails = null;
    IThreadRecycleAdapter iThreadRecycleAdapter = null;
    public ThreadRecycleAdapter(){}
public ThreadRecycleAdapter(ArrayList<Thread> list, LoginDetails loginDetails,IThreadRecycleAdapter iThreadRecycleAdapter)
    {
        this.iThreadRecycleAdapter = iThreadRecycleAdapter;
        this.threadList = list;
        this.loginDetails = loginDetails;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thread_structure, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
}

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Thread thread = threadList.get(position);
        holder.titleTxt.setText(thread.title);
        holder.thread = thread;
        if (loginDetails.user_id.equalsIgnoreCase(thread.userId)) {
            holder.delete.setVisibility(View.VISIBLE);
            thread.token = loginDetails.token;
        } else {
            holder.delete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }


    public  class ViewHolder  extends RecyclerView.ViewHolder{
        TextView titleTxt;
        ImageView delete;
        OkHttpClient client = new OkHttpClient();
        Thread thread = null;
        Handler handler = new Handler();

        public ViewHolder(View view)
        {

            super(view);
            OkHttpClient CLIENT = new OkHttpClient();
            this.thread = thread;
            this.titleTxt = view.findViewById(R.id.titleTxt);
            this.delete = view.findViewById(R.id.deleteThread);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TestConnection.isConnected( v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)))
                    {
                        thread.cacheToken = loginDetails.token;
                        thread.cacheUser_id = loginDetails.user_id;
                        iThreadRecycleAdapter.GoToMessage(thread);
                    }
                    else{
                       ToastMessage.showToast(0, v.getContext(),null);
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TestConnection.isConnected( v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE))) {
                        Request request = new Request.Builder()
                                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/thread/delete/" + thread.id)
                            .addHeader("Authorization", "BEARER " + thread.token)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {

                                if (!response.isSuccessful()) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                        }
                                    });
                                } else {
                                    final ResponseBody responseBody = response.body();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            threadList.remove(thread);
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(v.getContext(), "No Internet", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
}


