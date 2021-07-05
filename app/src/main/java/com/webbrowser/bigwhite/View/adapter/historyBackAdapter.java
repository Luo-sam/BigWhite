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

import com.webbrowser.bigwhite.Model.data.historyResponse;
import com.webbrowser.bigwhite.R;

import java.util.List;

/**
 * 后端的adapter
 *
 * @author luo
 */
public class historyBackAdapter extends ArrayAdapter<historyResponse.DataBean> {
    private final int newResourceId;
    public historyBackAdapter(@NonNull Context context, int resource, @NonNull List<historyResponse.DataBean> objects) {
        super(context, resource, objects);
        newResourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        historyResponse.DataBean historyData = getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(newResourceId,parent,false);
        TextView name = view.findViewById(R.id.internet_name);
        TextView address = view.findViewById(R.id.internet_address);

        name.setText(historyData.getTitle());
        address.setText(historyData.getUrl());
        return view;
    }

}
