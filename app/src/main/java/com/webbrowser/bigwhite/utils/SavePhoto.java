package com.webbrowser.bigwhite.utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.OutputStream;

/**
 * 保存图片到相册$
 *
 * @author luo$
 */
public class SavePhoto {
    //存调用该类的活动
    Context context;

    public SavePhoto(Context context) {
        this.context = context;
    }

    public void SaveBitmapFromView(View view) throws FileNotFoundException {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        Uri saveUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());

        OutputStream it = context.getContentResolver().openOutputStream(saveUri);
        if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)) {
            Toast.makeText(context, "存储成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "存储失败", Toast.LENGTH_SHORT).show();
        }
    }
}


