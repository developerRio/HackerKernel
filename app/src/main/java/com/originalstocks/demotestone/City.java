package com.originalstocks.demotestone;

import java.util.List;

public class City {

    private int city_id;
    private int city_state_id;
    private int city_country_id;
    private String cityName;

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getCity_state_id() {
        return city_state_id;
    }

    public void setCity_state_id(int city_state_id) {
        this.city_state_id = city_state_id;
    }

    public int getCity_country_id() {
        return city_country_id;
    }

    public void setCity_country_id(int city_country_id) {
        this.city_country_id = city_country_id;
    }

    private List<String> cityList;

    public City() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<String> getCityList() {
        return cityList;
    }

    public void setCityList(List<String> cityList) {
        this.cityList = cityList;
    }
}
