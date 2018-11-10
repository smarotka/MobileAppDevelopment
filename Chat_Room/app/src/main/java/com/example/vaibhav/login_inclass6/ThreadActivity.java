package com.example.vaibhav.login_inclass6;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ThreadActivity extends AppCompatActivity implements  IThreadRecycleAdapter{

    private RecyclerView recyclerView;
    public  static RecyclerView.Adapter adapter;
    private  RecyclerView.LayoutManager layoutManager;
    LoginDetails loginUser;
    ListView listView;
    ThreadAdapter tAdapter;
    TextView nameTxt;
    TextView newThread;
    ImageView logout, addThread;
    ArrayList<Thread> threadList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        listView = null;//findViewById(R.id.threadList);
        nameTxt = findViewById(R.id.username);

        if(getIntent().getSerializableExtra(IntentKeyAndCodes.SIGNUP_TO_THREAD_KEY)!=null) {

            loginUser = (LoginDetails) getIntent().getSerializableExtra(IntentKeyAndCodes.SIGNUP_TO_THREAD_KEY);
        }else{
            return;
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_id);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        nameTxt.setText(loginUser.firstName + " " + loginUser.lastName);
        logout = findViewById(R.id.logoutbtn);
        addThread = findViewById(R.id.addbtn);
        newThread = findViewById(R.id.editTextThread);
        if (TestConnection.isConnected(getSystemService(Context.CONNECTIVITY_SERVICE))) {
            new GetThread().execute(loginUser.token);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();
        }

        addThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newThread.getText().toString().isEmpty() || newThread.getText().toString().trim().equals("")) {
                    Toast.makeText(ThreadActivity.this, "Enter New Thread Name", Toast.LENGTH_SHORT).show();
                } else {
                    if (TestConnection.isConnected(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                        new CreateThread().createNewThread(newThread.getText().toString(), loginUser.token);
                        Toast.makeText(ThreadActivity.this, "New Thread " + newThread.getText().toString() + " created", Toast.LENGTH_SHORT).show();
                        newThread.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadList = null;
                loginUser = null;
                Toast.makeText(getApplicationContext(),"Logged Out successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ThreadActivity.this,MainActivity.class);
                startActivity(intent);
                System.exit(0);
                finish();
            }
        });

    }


    public void GoToMessage(Thread thread){
        if(TestConnection.isConnected(getSystemService(CONNECTIVITY_SERVICE)))
        {
            Intent intent = new Intent(IntentKeyAndCodes.MESSAGR_ACIVITY);
            intent.putExtra(IntentKeyAndCodes.THREAD_TO_MESSAGR_KEY,thread);
            startActivity(intent);
        }
        else{
            ToastMessage.showToast(0,getApplicationContext());
        }

    }
    class GetThread extends AsyncTask<String, Void, ArrayList<Thread>> {

        @Override
        protected ArrayList<Thread> doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            try {
                Request request = new Request.Builder()
                        .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/thread")
                        .addHeader("Authorization", "BEARER " + strings[0])
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                String responseThreads = response.body().string();
                JSONObject root = new JSONObject(responseThreads);
                JSONArray jsonArrayThread = root.getJSONArray("threads");

                for (int i = 0; i < jsonArrayThread.length(); i++) {
                    JSONObject srcObj = jsonArrayThread.getJSONObject(i);
                    Thread tObj = new Thread(srcObj.getString("title"), srcObj.getString("id"), srcObj.getString("user_id"), srcObj.getString("user_fname"), srcObj.getString("user_lname"));
                    threadList.add(tObj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return threadList;
        }

        @Override
        protected void onPostExecute(ArrayList<Thread> thread) {
            displayThreadList(thread);
        }
    }

    public void displayThreadList(ArrayList<Thread> threads) {
        if (threads.size() == 0 || threads.isEmpty() || threads == null) {
            Toast.makeText(ThreadActivity.this, "Thread List is empty", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new ThreadRecycleAdapter(threads,loginUser,this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    class CreateThread{
        public void createNewThread(String title, String token){
            OkHttpClient client = new OkHttpClient();
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("title", title)
                        .build();
                Request request = new Request.Builder()
                        .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/thread/add")
                        .addHeader("Authorization", "BEARER " + token)
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //ArrayList<Thread> threadNewList = new ArrayList<>();
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) {
                                throw new IOException("Unexpected code " + response);
                            }

                            new GetThread().execute(loginUser.token);
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
