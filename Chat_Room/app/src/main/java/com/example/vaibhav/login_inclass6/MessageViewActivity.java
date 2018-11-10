package com.example.vaibhav.login_inclass6;
//Assignment 6 :inclass class 6 : Group No R15
//Dikshali Bhikaji Margaj, Srishte Marotkar, Abineshwar Angamuthu Matheswaran
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MessageViewActivity extends AppCompatActivity implements IMessageAdapter {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    Thread RECEIVED_THREAD = null;
    OkHttpClient CLIENT = new OkHttpClient();
    AlertDialog.Builder builder = null;
    AlertDialog dialog = null;
    ArrayList<MessageModel> MESSAGES = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_message_view);
            if (TestConnection.isConnected(getSystemService(CONNECTIVITY_SERVICE))) {
                builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                builder.setTitle("Loading Chats...").setView(inflater.inflate(R.layout.activity_progress_loader, null)).setCancelable(false);
                dialog = builder.create();


                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_message);
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);

                if (getIntent() != null) {
                    if (getIntent().getSerializableExtra(IntentKeyAndCodes.THREAD_TO_MESSAGR_KEY) != null) {
                        RECEIVED_THREAD = (Thread) getIntent().getSerializableExtra(IntentKeyAndCodes.THREAD_TO_MESSAGR_KEY);
                        ((TextView) findViewById(R.id.user_name)).setText(RECEIVED_THREAD.title);
                        dialog.show();
                        GetMessages(RECEIVED_THREAD.cacheToken, RECEIVED_THREAD.id);
                    }
                }

                ((ImageView) findViewById(R.id.home_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                ((ImageView) findViewById(R.id.send_message)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TestConnection.isConnected(getSystemService(CONNECTIVITY_SERVICE))) {
                            EditText messageBox = ((EditText) findViewById(R.id.message_box));
                            if (!(messageBox == null || messageBox.toString().equals("") || messageBox.toString().trim().equals(""))) {
                                addMessageToChatBox(messageBox.getText().toString());
                                messageBox.setText("");
                            }
                        } else {
                            ToastMessage.showToast(0, getApplicationContext(), null);
                        }
                    }
                });
            } else {
                ToastMessage.showToast(0, getApplicationContext());
            }
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
            showToast(0, "Technical Issue, Please check logs.");
        }
    }

    public void addMessageToChatBox(String message) {
        RequestBody formBody = new FormBody.Builder()
                .add("message", message)
                .add("thread_id", RECEIVED_THREAD.id)
                .build();
        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/message/add")
                .post(formBody)
                .addHeader("Authorization", "BEARER " + RECEIVED_THREAD.cacheToken)
                .build();

        CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    MessageViewActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ResponseBody responseBody = response.body();
                            try {
                                JSONObject object = null;
                                try {
                                    object = new JSONObject(responseBody.string());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ToastMessage.showToast(0, getApplicationContext(), object.getString("message"));
                            } catch (IOException e) {
                                ToastMessage.showToast(2, getApplicationContext());
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }finally {
                                dialog.dismiss();
                            }

                        }
                    });
                } else {
                    MessageViewActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ResponseBody responseBody = response.body();
                            try {
                                JSONObject object = new JSONObject(responseBody.string());
                                if (object.getString("status").equalsIgnoreCase("ok")) {
                                    GetMessages(RECEIVED_THREAD.cacheToken, RECEIVED_THREAD.id);
                                }
                            } catch (IOException e) {
                                ToastMessage.showToast(2, getApplicationContext());
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception ex) {

                                Log.e("error", ex.getStackTrace().toString());
                            }finally {
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    public void GetMessages(String token, String id) {
        final Handler handler = new Handler();
        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/messages/" + id)
                .addHeader("Authorization", "BEARER " + token)
                .build();

        CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String result = responseBody;
                                JSONObject object = new JSONObject(result);

                                if (object.getString("status").toString().equalsIgnoreCase("ok")) {
                                    GetTheJsonToClass(object);
                                    GoToMessageAdapter();
                                }
                                Log.d("demo", object.get("status").toString());
                            } catch (Exception e) {
                                Log.e("error", e.getMessage());
                            } finally {
                                dialog.dismiss();
                            }
                        }
                    });
                } else {
                    final String responseBody = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String result = responseBody;
                                JSONObject jsonObject = new Gson().fromJson(result, JSONObject.class);
                                String jsonArray = jsonObject.get("message").toString();
                                Log.d("demo", jsonArray);
                                showToast(0, jsonArray);
                            } catch (Exception e) {
                                Log.e("error", e.getMessage());
                            } finally {
                                dialog.dismiss();
                            }
                        }
                    });
                }

            }
        });
    }

    public void GoToMessageAdapter() {
        if (TestConnection.isConnected(getSystemService(CONNECTIVITY_SERVICE))) {
            adapter = new MessageAdapter(MESSAGES, RECEIVED_THREAD);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        } else {
            showToast(0);
        }
    }

    public void GetTheJsonToClass(JSONObject object) {
        Gson gson = new Gson();
        MESSAGES = new ArrayList<>();
        JSONArray jsonArrayThread = null;
        try {
            jsonArrayThread = object.getJSONArray("messages");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonArrayThread.length() > 0) {
            for (int i = 0; i < jsonArrayThread.length(); i++) {
                try {
                    MESSAGES.add(gson.fromJson(jsonArrayThread.getJSONObject(i).toString(), MessageModel.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(MESSAGES, new MessageModel());
        }
    }

    public void showToast(int id, String... message) {
        ToastMessage.showToast(id, getApplicationContext(), message);
    }

    public void setAlert(boolean set) {
        if (set)
            dialog.show();
        else
            dialog.dismiss();

    }
}
