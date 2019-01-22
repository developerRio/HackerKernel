package com.originalstocks.demotestone;

import java.util.List;

public class Country {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private List<String> statesList;
    private String countryName;

    public Country() {
    }

    public void setStatesList(List<String> statesList) {
        this.statesList = statesList;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Country(List<String> statesList) {
        this.statesList = statesList;
    }

    public List<String> getStatesList() {
        return statesList;
    }
}
