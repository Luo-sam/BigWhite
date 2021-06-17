package com.webbrowser.bigwhite.View.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.webbrowser.bigwhite.Model.data.historyData;
import com.webbrowser.bigwhite.R;

import java.util.List;

public class bookmarkAdapter extends ArrayAdapter<historyData> {
    private final int newResourceId;
    public bookmarkAdapter(@NonNull Context context, int resource, @NonNull List<historyData> objects) {
        super(context, resource, objects);
        newResourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        historyData historyData = getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(newResourceId,parent,false);
        TextView name = view.findViewById(R.id.internet_name);
        TextView address = view.findViewById(R.id.internet_address);
        address.setText(historyData.getAddress());
        name.setText(historyData.getName());
        return view;
    }
}
