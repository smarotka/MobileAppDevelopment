package com.example.vaibhav.login_inclass6;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import android.os.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
     Thread USER = null;
     ArrayList<MessageModel> MESSAGES = null;
    IMessageAdapter iMessageAdapter = null;
     public MessageAdapter(ArrayList<MessageModel> messageModels,Thread user)//,IMessageAdapter iMessageAdapter)
     {
         Log.d("demo","In MessageAdapter");
         this.MESSAGES = messageModels;
         this.USER = user;
        // this.iMessageAdapter = iMessageAdapter;
 }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("demo","In MessageAdapter onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_message_structure, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Log.d("demo","In MessageAdapter onBindViewHolder");
             MessageModel messageModel = holder.selected = MESSAGES.get(position);
            holder.message.setText(messageModel.message);
            holder.timeDifference.setText(messageModel.getTimeString());
            holder.messanger.setText(messageModel.user_fname+" "+messageModel.user_lname);
            if(USER.cacheUser_id.equals(messageModel.user_id))
            {
                holder.binImage.setVisibility(View.VISIBLE);
                holder.messanger.setText("Me");
            }
            else{
                holder.binImage.setVisibility(View.INVISIBLE);
            }

    }

    @Override
    public int getItemCount() {
        return MESSAGES.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder
    {
        TextView message;
        TextView messanger;
        TextView timeDifference;
        ImageView binImage;
        MessageModel selected =null;
        Handler handler = new Handler();
        public ViewHolder(View itemView) {
            super(itemView);

            final OkHttpClient CLIENT = new OkHttpClient();
            this.binImage = (ImageView)itemView.findViewById(R.id.bin);
            this.messanger = (TextView)itemView.findViewById(R.id.messanger_tv);
            this.message = (TextView)itemView.findViewById(R.id.messafe_tv);
            this.timeDifference = (TextView)itemView.findViewById(R.id.ago_timing_tv);
            
            binImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TestConnection.isConnected( v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE))) {
                        Request request = new Request.Builder()
                                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/message/delete/" + selected.id)
                                .addHeader("Authorization", "BEARER " + USER.cacheToken)
                                .build();

                        CLIENT.newCall(request).enqueue(new Callback() {
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
                                            MESSAGES.remove(selected);
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(v.getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
