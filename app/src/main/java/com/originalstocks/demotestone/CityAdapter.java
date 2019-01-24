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

public class CityAdapter extends ArrayAdapter<City> {

    private List<City> cityList = new ArrayList<>();

    CityAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<City> cityList) {
        super(context, resource, spinnerText, cityList);
        this.cityList = cityList;
    }


    @Override
    public City getItem(int position) {
        return cityList.get(position);
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


    public void updateDataSet(List<City> items) {
        if (null != cityList) {
            cityList.clear();
            cityList.addAll(items);
        } else
            cityList = items;
        notifyDataSetChanged();
    }


    private View initView(int position) {
        City city = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.city_list, null);
        TextView textView =  v.findViewById(R.id.city_spinnerText);
        textView.setText(city.getCityName());
        return v;

    }

}
