package com.originalstocks.demotestone;

import java.util.List;

public class State {

    private int state_id;
    private int state_country_id;

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public int getState_country_id() {
        return state_country_id;
    }

    public void setState_country_id(int state_country_id) {
        this.state_country_id = state_country_id;
    }

    private String stateName;
    private List<String> stateList;

    public State(String stateName, List<String> stateList) {
        this.stateName = stateName;
        this.stateList = stateList;
    }

    public State() {
    }

    public List<String> getStateList() {
        return stateList;
    }

    public void setStateList(List<String> stateList) {
        this.stateList = stateList;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

}
