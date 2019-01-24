package com.originalstocks.demotestone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends ArrayAdapter<State> {

    private List<State> stateList = new ArrayList<>();

    StateAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<State> stateList) {
        super(context, resource, spinnerText, stateList);
        this.stateList = stateList;
    }

    @Override
    public State getItem(int position) {
        return stateList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    public void updateDataSet(List<State> items) {
        if (null != stateList) {
            stateList.clear();
            stateList.addAll(items);
        } else
            stateList = items;
        notifyDataSetChanged();
    }

    private View initView(int position) {
        State state = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.state_list, null);
        TextView textView =  v.findViewById(R.id.state_spinnerText);
        textView.setText(state.getStateName());
        return v;

    }
}
