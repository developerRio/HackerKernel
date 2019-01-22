package com.originalstocks.demotestone;

import java.util.List;

public class State {
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
