package com.webbrowser.bigwhite.View.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.webbrowser.bigwhite.R;

import java.util.List;

public class bookmarkFileAdapter extends BaseAdapter {
    private final List<String> fileList;
    private final LayoutInflater inflater;


    public bookmarkFileAdapter(Context context, List<String> fileList) {
        this.fileList = fileList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileList.size() == 0 ? null : fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_file,null);
            viewHolder.recordTv = convertView.findViewById(R.id.search_content_tv);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String content = fileList.get(position);
        viewHolder.recordTv.setText(content);

        return convertView;
    }
    private static class ViewHolder {
        TextView recordTv;
    }

}
