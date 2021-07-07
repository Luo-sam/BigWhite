package com.webbrowser.bigwhite.utils.morewindows_image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.webbrowser.bigwhite.Model.SQLite.WebPageHelper;
import com.webbrowser.bigwhite.R;

public class MoreWindowsDrawable {

    public static Bitmap generatorContactCountIcon(Context context, Bitmap icon){
        //初始化画布
        int iconSize=(int)context.getResources().getDimension(R.dimen.dimen_40dp);
        Bitmap contactIcon=Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(contactIcon);

        //拷贝图片
        Paint iconPaint=new Paint();
        iconPaint.setDither(true);//防抖动
        iconPaint.setFilterBitmap(true);//用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果
        Rect src=new Rect(0, 0, icon.getWidth(), icon.getHeight());
        Rect dst=new Rect(0, 0, iconSize, iconSize);
        canvas.drawBitmap(icon, src, dst, iconPaint);

        //在图片上创建一个覆盖的窗口个数
        int contacyCount= WebPageHelper.webpagelist.size();
        //启用抗锯齿和使用设备的文本字距
        Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);
        countPaint.setColor(Color.GRAY);
        countPaint.setTextSize(iconSize/2);
        countPaint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText(String.valueOf(contacyCount), iconSize/3, 2*iconSize/3, countPaint);
        return contactIcon;
    }
}
