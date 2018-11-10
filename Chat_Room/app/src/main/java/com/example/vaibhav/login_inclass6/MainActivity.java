
package com.example.vaibhav.login_inclass6;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder builder = null;
    AlertDialog dialog = null;
    EditText email;
    EditText password;
    Button login;
    Button signup;
    String eml;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Chat Room");
        email = (EditText) findViewById(R.id.login_editText);
        password = (EditText) findViewById(R.id.password_editText);
        login = (Button) findViewById(R.id.login_button);

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setTitle("Logging........").setView(inflater.inflate(R.layout.activity_progress_loader, null)).setCancelable(false);
        dialog = builder.create();

        ((Button) findViewById(R.id.signup_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TestConnection.isConnected(getSystemService(Context.CONNECTIVITY_SERVICE))) {
                    email.setText("");
                    password.setText("");
                    Intent intent = new Intent(IntentKeyAndCodes.SIGNUP_ACTIVITY);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TestConnection.isConnected(getSystemService(Context.CONNECTIVITY_SERVICE))) {


                    boolean isValid = true;

                    dialog.show();

                    eml = email.getText().toString();
                    Log.d("Demo", "email" + eml);
                    pass = password.getText().toString();

                    email.setText("");
                    password.setText("");

                    if (eml == null || eml.equals("") || eml.trim().equals("")) {
                        email.setError("Enter Email");
                        isValid = false;
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(eml).matches()) {
                        email.setError("Enter Valid Email ");
                        isValid = false;
                    } else if (pass == null || pass.equals("") || pass.trim().equals("")) {
                        password.setError("Enter Password");
                        isValid = false;
                    } else if (pass == null || pass.equals("") || pass.trim().equals("")) {
                        password.setError("Enter Password");
                        isValid = false;
                    }
                    if (isValid) {
                        OkHttpClient client = new OkHttpClient();

                        RequestBody formBody = new FormBody.Builder()
                                .add("email", eml)
                                .add("password", pass)
                                .build();
                        Request request = new Request.Builder()
                                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login")
                                .post(formBody)
                                .build();


                        client.newCall(request).enqueue(new Callback() {

                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();

                            }


                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                //Log.d("login","success"+response.body().string());
                                if (response.isSuccessful()) {

                                    Log.d("bbb", "s");
                                    JSONObject obj = null;
                                    // String json = response.body().string();
                                    try {
                                        String js = response.body().string();
                                        obj = new JSONObject(js);
                                        String status = obj.getString("status");

                                        if (status.equals("ok")) {
                                            LoginDetails ld = new LoginDetails(obj.getString("user_fname"), obj.getString("user_lname"), obj.getString("user_email"), null);
                                            ld.token = obj.getString("token");
                                            ld.user_id = obj.getString("user_id");

                                            Intent intent = new Intent(IntentKeyAndCodes.THREAD_ACTIVITY);
                                            intent.putExtra(IntentKeyAndCodes.SIGNUP_TO_THREAD_KEY, ld);
                                            startActivity(intent);
                                            dialog.dismiss();
                                            finish();
                                            // Log.d("demo", "status\n" + status + "\n" + token + "\n" + fname + "\n" + lname + "\n" + id + "\n" + email);
                                        }


                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }

                                } else {
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String js = response.body().string();
                                                JSONObject obj = null;
                                                String status = null;
                                                try {
                                                    obj = new JSONObject(js);
                                                    status = obj.getString("message");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();

                }
            }
        });


    }
}
