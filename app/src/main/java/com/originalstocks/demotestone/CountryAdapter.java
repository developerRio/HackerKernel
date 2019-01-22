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

public class CountryAdapter extends ArrayAdapter<Country> {


    private List<Country> countryList = new ArrayList<>();

    CountryAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<Country> countryList) {
        super(context, resource, spinnerText, countryList);
        this.countryList = countryList;
    }

    @Override
    public Country getItem(int position) {
        return countryList.get(position);
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

    private View initView(int position) {
        Country country = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.country_list, null);
        TextView textView = v.findViewById(R.id.country_spinnerText);
        textView.setText(country.getCountryName());
        return v;

    }

}
