package com.webbrowser.bigwhite.View.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.myView.MyImageView;
import com.webbrowser.bigwhite.utils.method.picDialog;
import com.webbrowser.bigwhite.utils.url_image.URLImageParser;

import java.util.List;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.ViewHolder> {
    private List<String> data;
    boolean[] flags;
    View view;
    Context mContext;


    public newsAdapter(List<String> data, Context context) {
        this.data = data;
        flags = new boolean[data.size()];
        mContext = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView title;
        TextView textView1;
        MyImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            textView1 = itemView.findViewById(R.id.content);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.imageUrl);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_advisory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s = data.get(position);
        if (s.startsWith("https://f") || s.startsWith("https://p")||s.endsWith("gif")) {
            holder.imageView.setImageURL(s);

            /**
             *@Author luo
             *@Time 2021/7/1 10:38
             *@Description ?????????????????????
             */
            picDialog picDialog = new picDialog(mContext);
            picDialog.addDialog(holder.imageView);
            picDialog.dismissDia();
            picDialog.save(holder.imageView);
        } else {
            holder.textView1.setText(s);
            holder.imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void setRichText(ViewHolder holder, Context context, String text) {
        TextView textView = holder.textView1;
        float imageSize = 10 * 15;
        //???????????????text????????????"[]"????????????
//        String text = textView.getText().toString();
        SpannableString spannableString = new SpannableString(" ");
        //??????"[(??????']'????????????)]"??????????????????????????????????????????????????????????????????
        //?????????????????????????????????????????????????????????
        ImageSpan imageSpan;
        //????????????????????????[http://hao.qudao.com/upload/article/20160120/82935299371453253610.jpg]???[??????]
        if (text.contains("http")) {
            //????????????
            //????????????url?????????'['???']'???
            String url = text;
            //????????????????????????
            Drawable drawableFromNet = new URLImageParser(textView, context, (int) imageSize).getDrawable(url);
            imageSpan = new ImageSpan(drawableFromNet, ImageSpan.ALIGN_BASELINE);
            //??????????????????
            spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else {
            //????????????
        }

        textView.setText(spannableString);
    }

}
