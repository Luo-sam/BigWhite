package com.webbrowser.bigwhite.View.adapter;


/**
 * Created by Administrator on 2018/2/11.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.webbrowser.bigwhite.R;

import java.util.List;

/**
 * Created by 05 on 2016/7/27.
 */
public class searchHistoryAdapter extends BaseAdapter {

    private Context context;
    private List<String> searchRecordsList;
    private LayoutInflater inflater;

    public searchHistoryAdapter(Context context, List<String> searchRecordsList) {
        this.context = context;
        this.searchRecordsList = searchRecordsList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        //设置listView的显示条目数量为5
        return searchRecordsList.size() > 5 ? 5 : searchRecordsList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchRecordsList.size() == 0 ? null : searchRecordsList.get(position);
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
            convertView = inflater.inflate(R.layout.list_item,null);
            viewHolder.recordTv = (TextView) convertView.findViewById(R.id.search_content_tv);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String content = searchRecordsList.get(position);
        viewHolder.recordTv.setText(content);

        return convertView;
    }

    private class ViewHolder {
        TextView recordTv;
    }
}