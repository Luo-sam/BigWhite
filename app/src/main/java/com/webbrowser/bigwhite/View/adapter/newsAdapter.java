package com.webbrowser.bigwhite.View.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.imageview.MyImageView;
import com.webbrowser.bigwhite.utils.picDialog;
import com.webbrowser.bigwhite.utils.url_image.URLImageParser;

import java.util.List;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.ViewHoder> {
    private List<String> data;
    boolean[] flags;
    View view;
    Context mContext;

    public void updateData(List<String> mData) {
        this.data = mData;
        notifyDataSetChanged();
    }

    static class ViewHoder extends RecyclerView.ViewHolder {
        TextView author;
        TextView title;
        TextView textView1;
        MyImageView imageView;



        //        TextView textView2;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            textView1 = itemView.findViewById(R.id.content);
//            textView2=itemView.findViewById(R.id.imageUrl);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.imageUrl);
        }
    }

    public newsAdapter(List<String> data, Context context) {
        this.data = data;
        flags = new boolean[data.size()];
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_advisory, parent, false);
        ViewHoder holder = new ViewHoder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        String s = data.get(position);
        if (s.startsWith("https://f") || s.startsWith("https://p")) {
            Log.d("pic", s);
            holder.imageView.setImageURL(s);

            picDialog picDialog = new picDialog(mContext);
            picDialog.addDialog(holder.imageView);
            picDialog.dismissDia();
            picDialog.save(holder.imageView);
            int wid = holder.imageView.getWidth();
            int hei = holder.imageView.getHeight();
            Log.d("wid", String.valueOf(wid));

            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();

            if (wid != 0) {
                int height = (int) width * (hei / wid);
                Log.d("height1", String.valueOf(height));
                holder.imageView.setMaxHeight(height);
                //holder.imageView.setMaxWidth(width);
                Log.d("width1", String.valueOf(width));

            }
//        int wid = wm.getDefaultDisplay().getWidth();
            holder.textView1.setVisibility(View.GONE);
            //setRichText(holder, WebPageHelper.webpagelist.get(0).getActivity().getBaseContext(), s);
        } else {
            holder.textView1.setText(s);
            holder.imageView.setVisibility(View.GONE);
            //setRichText(holder, WebPageHelper.webpagelist.get(0).getActivity().getBaseContext(), s);
        }
        //holder.textView2.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHoder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void setRichText(ViewHoder hoder, Context context, String text) {
        TextView textView = hoder.textView1;
        float imageSize = 10 * 15;
        //图文混排的text，这里用"[]"标示图片
//        String text = textView.getText().toString();
        SpannableString spannableString = new SpannableString(" ");
        //匹配"[(除了']'任意内容)]"的正则表达式，获取网络图片和本地图片替换位置
        //简书中正则表达式显示有问题，正确如上图
        ImageSpan imageSpan;
        //匹配的内容，例如[http://hao.qudao.com/upload/article/20160120/82935299371453253610.jpg]或[哈哈]
        if (text.contains("http")) {
            //网络图片
            //获取图片url（去掉'['和']'）
            String url = text;
            //异步获取网络图片
            Drawable drawableFromNet = new URLImageParser(textView, context, (int) imageSize).getDrawable(url);
            imageSpan = new ImageSpan(drawableFromNet, ImageSpan.ALIGN_BASELINE);
            //设置网络图片
            spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else {
            //本地图片
        }

        textView.setText(spannableString);
    }

}
