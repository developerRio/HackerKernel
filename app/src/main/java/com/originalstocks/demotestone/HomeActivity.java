package com.originalstocks.demotestone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

   // public static final String API_TOKEN = "H2Y6wI6itQycNLPwoRI5BwrCBAodEFY7G21m3d9ueQW2zAWQGh8jYTQwAMcH43IVCgJKmrO8eVJmnzJNrc2guMYOZzOGlNHDFZ9k";
    final List<String> mCountries = new ArrayList<>();
    final List<String> mStates = new ArrayList<>();
    final List<String> mCities = new ArrayList<>();
    final ArrayList<Country> countryNewList = new ArrayList<>();
    final ArrayList<State> stateNewList = new ArrayList<>();
    final ArrayList<City> cityNewList = new ArrayList<>();
    CountryAdapter countryAdapter;
    StateAdapter stateAdapter;
    CityAdapter cityAdapter;
    private Spinner countrySpinner;
    private Spinner stateSpinner;
    private Spinner citiesSpinner;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        countrySpinner = findViewById(R.id.country_spinner);
        stateSpinner = findViewById(R.id.state_spinner);
        citiesSpinner = findViewById(R.id.cities_spinner);

        stateSpinner.setVisibility(View.GONE);
        citiesSpinner.setVisibility(View.GONE);

        countryAdapter = new CountryAdapter(HomeActivity.this, R.layout.country_list,
                R.id.country_spinnerText, countryNewList);
        stateAdapter = new StateAdapter(HomeActivity.this, R.layout.state_list,
                R.id.state_spinnerText, stateNewList);
        cityAdapter = new CityAdapter(HomeActivity.this, R.layout.city_list,
                R.id.city_spinnerText, cityNewList);

        displayLoader();
        loadCountryDetails();

    }

    private void loadCountryDetails() {

        SharedPreferences writeData = getSharedPreferences("apiToken", MODE_PRIVATE);
        String api_token = writeData.getString("api", null);
        //Log.i("MyApiToken", "loadCountryDetails: " + api_token);

        String country_url = "http://139.59.87.150/imakeprofile/public/api/countries?api_token=" + api_token;

        StringRequest stringCountryRequest = new StringRequest(Request.Method.GET, country_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.optString("return").equals("true")) {
                        JSONArray stateArray = obj.getJSONArray("states");
                        for (int i = 0; i < stateArray.length(); i++) {
                            final Country my_country = new Country();
                            JSONObject dataObj = stateArray.getJSONObject(i);
                            my_country.setCountryName(dataObj.getString("name"));
                            my_country.setId(dataObj.getInt("id"));
                            countryNewList.add(my_country);
                        }
                        for (int i = 0; i < countryNewList.size(); i++) {
                            mCountries.add(countryNewList.get(i).getCountryName().toString());
                        }
                    }
                    Log.i("HomeActivity", "onResponseNew: " + mCountries);
                    countryAdapter.notifyDataSetChanged();
                    countrySpinner.setAdapter(countryAdapter);

                    countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int myCountryId = (int) (id + 1);
                            Log.i("COUNTRY_ID", "onResponse: Country Id: " + myCountryId);
                            loadStateData(myCountryId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(stringCountryRequest);
    }

    private void loadStateData(final int countryId) {

        SharedPreferences writeData = getSharedPreferences("apiToken", MODE_PRIVATE);
        String api_token = writeData.getString("api", null);
        // Log.i("MyApiToken", "loadCountryDetails: " + api_token);

        String countryIdStr = String.valueOf(countryId);
        // Log.i("GettingID", "loadStateData: " + countryIdStr);
        String states_url = "http://139.59.87.150/imakeprofile/public/api/states/" + countryIdStr + "?api_token=" + api_token;

        StringRequest stringStateRequest = new StringRequest(Request.Method.GET, states_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    final JSONObject obj = new JSONObject(response);
                    if (obj.optString("return").equals("true")) {

                        stateAdapter.updateDataSet(stateNewList);

                        JSONArray stateArray = obj.getJSONArray("states");
                        for (int i = 0; i < stateArray.length(); i++) {
                            final State my_state = new State();
                            JSONObject dataObj = stateArray.getJSONObject(i);
                            my_state.setState_id(dataObj.getInt("id"));
                            my_state.setStateName(dataObj.getString("name"));
                            my_state.setState_country_id(dataObj.getInt("country_id"));

                            stateNewList.add(my_state);
                        }
                        for (int i = 0; i < stateNewList.size(); i++) {
                            mStates.add(stateNewList.get(i).getStateName().toString());
                        }

                    }
                    stateSpinner.setVisibility(View.VISIBLE);
                    stateSpinner.setAdapter(stateAdapter);
                    stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int myStateId = (int) (id + 1);
                            Log.i("State_Id", "loadStateId: " + myStateId);

                            Log.i("State_Id_From_Model", "loadStateId: " + position + " id: " + id + " stateIds: ");

                            loadCityData(myStateId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringStateRequest);
    }

    private void loadCityData(int myStateId) {

        SharedPreferences writeData = getSharedPreferences("apiToken", MODE_PRIVATE);
        String api_token = writeData.getString("api", null);
       // Log.i("MyApiToken", "loadCountryDetails: " + api_token);

        String stateIdStr = String.valueOf(myStateId);
        Log.i("GettingID", "loadStateData: " + stateIdStr);
        String cities_url = "http://139.59.87.150/imakeprofile/public/api/cities/" + stateIdStr + "?api_token=" + api_token;

        final StringRequest stringCitiesRequest = new StringRequest(Request.Method.GET, cities_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.optString("return").equals("true")) {

                        cityAdapter.updateDataSet(cityNewList);

                        JSONArray cityArray = obj.getJSONArray("cities");
                        for (int i = 0; i < cityArray.length(); i++) {
                            City my_city = new City();
                            JSONObject dataObj = cityArray.getJSONObject(i);

                            my_city.setCityName(dataObj.getString("name"));
                            my_city.setCity_id(dataObj.getInt("id"));
                            my_city.setCity_state_id(dataObj.getInt("state_id"));
                            my_city.setCity_country_id(dataObj.getInt("country_id"));
                            cityNewList.add(my_city);
                        }

                        for (int i = 0; i < cityNewList.size(); i++) {
                            mCities.add(cityNewList.get(i).getCityName().toString());
                        }
                        citiesSpinner.setVisibility(View.VISIBLE);
                        citiesSpinner.setAdapter(cityAdapter);
                        citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringCitiesRequest);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(HomeActivity.this);
        pDialog.setMessage("Loading Data.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

    }

    public void nextActivty(View view) {
        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
    }
}
