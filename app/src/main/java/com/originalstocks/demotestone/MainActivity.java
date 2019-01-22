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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordText, registerText;

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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform login...
                getUserData();
                updateUI();
            }
        });

    }

    private void updateUI() {
        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }

    // Getting the user data...

    private void getUserData() {

        String url = "http://139.59.87.150/imakeprofile/public/api/company/login";

        StringRequest profileRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject obj = new JSONObject(response);
                    if (obj.optString("return").equals("true")) {

                        JSONObject userObject = obj.getJSONObject("user");
                        String api_token = userObject.getString("api_token");
                       // Log.i("TOKEN", "onResponse: " + api_token);

                        // Storing api token
                        SharedPreferences.Editor readData = getSharedPreferences("apiToken", MODE_PRIVATE).edit();
                        readData.putString("api", api_token);
                        readData.apply();

                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Display error message whenever an error occurs
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(profileRequest);


    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences writeData = getSharedPreferences("apiToken", MODE_PRIVATE);
        String user = writeData.getString("api", null);

        if (user != null){
            updateUI();
        }else {
            Toast.makeText(this, "Tap on login to proceed...", Toast.LENGTH_SHORT).show();

        }
    }
}
