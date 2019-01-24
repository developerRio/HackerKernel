package com.originalstocks.demotestone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordText, registerText;

    public static final String EMAIL = "yash@gmail.com";
    public static final String PASSWORD = "123456";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.email_editText);
        passwordEditText = findViewById(R.id.password_editText);
        loginButton = findViewById(R.id.button_login);
        forgotPasswordText = findViewById(R.id.forgot_password_button);
        registerText = findViewById(R.id.register_button);


        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Register Clicked", Toast.LENGTH_SHORT).show();
            }
        });

       getUserData();
    }

    private void updateUI() {
        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }

    // Getting the user data...

    private void getUserData() {

        String url = "http://139.59.87.150/imakeprofile/public/api/company/login";
        url = url.replaceAll(" ", "%20");


        final StringRequest profileRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MainResponse", "onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("return").equals("true")) {

                        JSONObject userObject = obj.getJSONObject("user");

                        final String api_token = userObject.getString("api_token");
                        final String email = userObject.getString("email");

                        Log.i("TOKEN", "onResponse: " + api_token);
                        Log.i("EMAIL", "onResponse: " + email);


                        loginButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String mEmail = emailEditText.getText().toString().trim();
                                String mPassword = passwordEditText.getText().toString().trim();

                                if (!isValidEmail(mEmail)){
                                    Toast.makeText(MainActivity.this, "Please provide a valid email.", Toast.LENGTH_LONG).show();
                                }else if (!mEmail.equals(email)){
                                    Toast.makeText(MainActivity.this, "Entered Email is Incorrect.", Toast.LENGTH_LONG).show();
                                }else {
                                    // Provided login
                                    // *Password is supposed to check at the back-end,
                                    // via api_token because for each password there's
                                    // a different api_token is assigned. *
                                    if (mPassword.equals(PASSWORD)){
                                        updateUI();
                                        Toast.makeText(MainActivity.this, "Successfully Logged in...", Toast.LENGTH_SHORT).show();

                                        // Storing api token
                                        SharedPreferences.Editor readData = getSharedPreferences("apiToken", MODE_PRIVATE).edit();
                                        readData.putString("api", api_token);
                                        readData.apply();

                                    }else {
                                        Toast.makeText(MainActivity.this, "Entered Password is Incorrect.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });

                    }else {
                        Toast.makeText(MainActivity.this, "No data received", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Display error message whenever an error occurs
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("Error", "onErrorResponse: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", EMAIL);
                params.put("password", PASSWORD);
                return params;
            }
        };


        profileRequest.setRetryPolicy(
                new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(profileRequest);


    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences writeData = getSharedPreferences("apiToken", MODE_PRIVATE);
        String user = writeData.getString("api", null);
        if (user != null) {
            updateUI();
        } else {
            Toast.makeText(this, "No user available...", Toast.LENGTH_SHORT).show();

        }
    }
}
