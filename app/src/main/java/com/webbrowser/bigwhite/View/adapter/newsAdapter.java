package com.webbrowser.bigwhite.View.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.utils.CrawlPageUtil;

import java.util.List;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.ViewHoder> {
    private List<String> data;
    static class ViewHoder extends RecyclerView.ViewHolder{
        TextView author;
        TextView title;
        TextView textView1;
        TextView textView2;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            author=itemView.findViewById(R.id.author);
            textView1=itemView.findViewById(R.id.content);
            textView2=itemView.findViewById(R.id.imageUrl);
            title=itemView.findViewById(R.id.title);
        }
    }
    public newsAdapter(List<String> data){
        this.data=data;
    }
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_advisory,parent,false);
        ViewHoder holder = new ViewHoder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.textView1.setText(data.get(position));
        //holder.textView2.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
