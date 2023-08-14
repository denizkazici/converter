package com.example.convert_app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class spinnerAdapter extends ArrayAdapter<String> {

    Context context;
    List<String> currencyList;

    public spinnerAdapter(Context context, List<String> currencyList) {
        super(context, R.layout.spinner_value, currencyList);
        this.context = context;
        this.currencyList = currencyList;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }


    public View getCustomView(int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View spin = inflater.inflate(R.layout.spinner_value, parent, false);


        TextView spinnerText = spin.findViewById(R.id.tvSpinner);
        ImageView spinnerImg = spin.findViewById(R.id.ivSpinner);

        Resources res = context.getResources();
        String drawableName = currencyList.get(position).toLowerCase();
        int resId = res.getIdentifier(drawableName, "drawable", context.getPackageName());
        Drawable drawable = context.getDrawable(resId);

        spinnerText.setText(currencyList.get(position));
        spinnerImg.setImageDrawable(drawable);

        return spin;
    }


}

