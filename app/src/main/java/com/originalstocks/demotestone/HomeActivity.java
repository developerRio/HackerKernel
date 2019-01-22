package com.originalstocks.demotestone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
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

public class HomeActivity extends AppCompatActivity {

    final List<String> mCountries = new ArrayList<>();
    final List<String> mStates = new ArrayList<>();
    final List<String> mCities = new ArrayList<>();
    final ArrayList<Country> countryNewList = new ArrayList<>();
    final ArrayList<State> stateNewList = new ArrayList<>();
    final ArrayList<City> cityNewList = new ArrayList<>();
    private Spinner countrySpinner;
    private Spinner stateSpinner;
    private Spinner citiesSpinner;
    private ProgressDialog pDialog;
    private String api_token;
    private String api_tokens = "H2Y6wI6itQycNLPwoRI5BwrCBAodEFY7G21m3d9ueQW2zAWQGh8jYTQwAMcH43IVCgJKmrO8eVJmnzJNrc2guMYOZzOGlNHDFZ9k";
    private String country_url = null;

    private String country_urls = "http://139.59.87.150/imakeprofile/public/api/countries?api_token=H2Y6wI6itQycNLPwoRI5BwrCBAodEFY7G21m3d9ueQW2zAWQGh8jYTQwAMcH43IVCgJKmrO8eVJmnzJNrc2guMYOZzOGlNHDFZ9k";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        countrySpinner = findViewById(R.id.country_spinner);
        stateSpinner = findViewById(R.id.state_spinner);
        citiesSpinner = findViewById(R.id.cities_spinner);
        displayLoader();
        loadStateCityDetails();

        stateSpinner.setVisibility(View.GONE);
        citiesSpinner.setVisibility(View.GONE);

        SharedPreferences writeData = getSharedPreferences("apiToken", MODE_PRIVATE);
        if (writeData.contains("api")) {
            api_token = writeData.getString("api", api_tokens);

        }
        country_url = "http://139.59.87.150/imakeprofile/public/api/countries?" + api_token;

    }



    private void loadStateCityDetails() {


        StringRequest stringCountryRequest = new StringRequest(Request.Method.GET, country_urls, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.optString("return").equals("true")) {

                        JSONArray stateArray = obj.getJSONArray("states");
                        for (int i = 0; i < stateArray.length(); i++) {
                            Country my_country = new Country();
                            JSONObject dataObj = stateArray.getJSONObject(i);

                            my_country.setCountryName(dataObj.getString("name"));
                            my_country.setId(dataObj.getInt("id"));

                            countryNewList.add(my_country);
                        }

                        for (int i = 0; i < countryNewList.size(); i++) {
                            mCountries.add(countryNewList.get(i).getCountryName().toString());
                        }
                    }

                    Log.i("MainActivity", "onResponseNew: " + mCountries);

                    final CountryAdapter countryAdapter = new CountryAdapter(HomeActivity.this, R.layout.country_list,
                            R.id.country_spinnerText, countryNewList);
                    countrySpinner.setAdapter(countryAdapter);

                    String countryText = countrySpinner.getSelectedItem().toString();
                    if (countryText != null){
                        loadStateData();
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
            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(stringCountryRequest);


    }


    private void displayLoader() {
        pDialog = new ProgressDialog(HomeActivity.this);
        pDialog.setMessage("Loading Data.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

    }

    private void loadStateData() {
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedCountry = countrySpinner.getSelectedItem().toString();

                int myCountryId = (int) (id + 1);
                //Toast.makeText(HomeActivity.this, "Selected : " + myCountryId, Toast.LENGTH_SHORT).show();


                String states_url = "http://139.59.87.150/imakeprofile/public/api/states/" + myCountryId + "?api_token=H2Y6wI6itQycNLPwoRI5BwrCBAodEFY7G21m3d9ueQW2zAWQGh8jYTQwAMcH43IVCgJKmrO8eVJmnzJNrc2guMYOZzOGlNHDFZ9k";

                StringRequest stringStateRequest = new StringRequest(Request.Method.GET, states_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.dismiss();

                        try {

                            JSONObject obj = new JSONObject(response);
                            if (obj.optString("return").equals("true")) {

                                JSONArray stateArray = obj.getJSONArray("states");

                                for (int i = 0; i < stateArray.length(); i++) {
                                    State my_state = new State();
                                    JSONObject dataObj = stateArray.getJSONObject(i);

                                    my_state.setStateName(dataObj.getString("name"));
                                    stateNewList.add(my_state);
                                }

                                for (int i = 0; i < stateNewList.size(); i++) {
                                    mStates.add(stateNewList.get(i).getStateName().toString());
                                }
                            }

                            Log.i("MainActivity", "onResponseNew: " + mStates);

                            loadCityData();

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
                MySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringStateRequest);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadCityData() {

        stateSpinner.setVisibility(View.VISIBLE);
        final StateAdapter stateAdapter = new StateAdapter(HomeActivity.this, R.layout.state_list, R.id.state_spinnerText, stateNewList);
        stateSpinner.setAdapter(stateAdapter);



        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                int myCityId = (int) (id + 1);
                //Toast.makeText(HomeActivity.this, "Selected : " + myCityId, Toast.LENGTH_SHORT).show();

                String cities_url = "http://139.59.87.150/imakeprofile/public/api/cities/" + myCityId + "?api_token=H2Y6wI6itQycNLPwoRI5BwrCBAodEFY7G21m3d9ueQW2zAWQGh8jYTQwAMcH43IVCgJKmrO8eVJmnzJNrc2guMYOZzOGlNHDFZ9k";

                final StringRequest stringCitiesRequest = new StringRequest(Request.Method.GET, cities_url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        try {

                            JSONObject obj = new JSONObject(response);
                            if (obj.optString("return").equals("true")) {
                                JSONArray cityArray = obj.getJSONArray("cities");
                                for (int i = 0; i < cityArray.length(); i++) {
                                    City my_city = new City();
                                    JSONObject dataObj = cityArray.getJSONObject(i);

                                    my_city.setCityName(dataObj.getString("name"));
                                    cityNewList.add(my_city);
                                }

                                for (int i = 0; i < cityNewList.size(); i++) {
                                    mCities.add(cityNewList.get(i).getCityName().toString());
                                }

                                Log.i("MainActivity", "onResponseNew: " + mCities);
                                 if (mCities == null){
                                     Toast.makeText(HomeActivity.this, "No city data available", Toast.LENGTH_SHORT).show();
                                 }

                                citiesSpinner.setVisibility(View.VISIBLE);
                                final CityAdapter cityAdapter = new CityAdapter(HomeActivity.this, R.layout.city_list,
                                        R.id.city_spinnerText, cityNewList);
                                citiesSpinner.setAdapter(cityAdapter);

                                citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        String citySpinnerText = citiesSpinner.getSelectedItem().toString();
                                        //Toast.makeText(HomeActivity.this, "Selected city : " + citySpinnerText, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
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
                    }
                });

                MySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringCitiesRequest);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void nextActivty(View view) {
        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
    }
}
