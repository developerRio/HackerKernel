package com.originalstocks.demotestone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Profile> profileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        profileList = new ArrayList<>();

        //get data
        loadData();
    }

    private void loadData() {

        String url = "http://hackerkernel.com/demo/android-api/api.php";

        StringRequest profileRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    JSONArray imagesArray = obj.getJSONArray("images");
                    for (int i = 0; i < imagesArray.length(); i++) {
                        Profile mProfile = new Profile(i);
                        JSONObject dataObj = imagesArray.getJSONObject(i);

                        mProfile.setStatus(dataObj.getString("status"));
                        mProfile.setFile(dataObj.getString("file"));

                        JSONArray facesArray = dataObj.getJSONArray("faces");
                        for (int j = 0; j < facesArray.length(); j++) {
                            JSONObject facesObj = facesArray.getJSONObject(j);
                            mProfile.setPitch(facesObj.getString("pitch"));

                            JSONObject attributesObj = facesArray.getJSONObject(j).getJSONObject("attributes");
                            mProfile.setLips(attributesObj.getString("lips"));

                            JSONObject genderObj = attributesObj.getJSONObject("gender");
                            mProfile.setMaleConfidence(genderObj.getString("maleConfidence"));

                            profileList.add(mProfile);
                            Log.i("Profile", "onResponse: " + profileList);
                        }
                    }

                    ProfileAdapter profileAdapter = new ProfileAdapter(ProfileActivity.this, profileList);
                    mRecyclerView.setAdapter(profileAdapter);


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
            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(profileRequest);


    }
}
