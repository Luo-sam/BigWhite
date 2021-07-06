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

import com.webbrowser.bigwhite.Model.data.bookmarkResponse;
import com.webbrowser.bigwhite.R;

import java.util.List;

public class bookmarkAdapter extends ArrayAdapter<bookmarkResponse.DataBean> {
    private final int newResourceId;
    public bookmarkAdapter(@NonNull Context context, int resource, @NonNull List<bookmarkResponse.DataBean> objects) {
        super(context, resource, objects);
        newResourceId = resource;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        bookmarkResponse.DataBean bookmarkData = getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(newResourceId,parent,false);
        TextView name = view.findViewById(R.id.internet_name);
        TextView address = view.findViewById(R.id.internet_address);
        address.setText(bookmarkData.getUrl());
        name.setText(bookmarkData.getTitle());
        return view;
    }
}
