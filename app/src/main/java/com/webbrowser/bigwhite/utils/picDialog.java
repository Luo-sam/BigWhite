package com.webbrowser.bigwhite.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ParseException;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.webbrowser.bigwhite.R;

import java.io.FileNotFoundException;

/**
 * 用来显示dialog$
 *
 * @author luo$
 */
public class picDialog extends Dialog {
    /**
     * @Time 2021/6/28 9:28
     * @Description 图片的编辑器测试
     */

    private Dialog dia;
    private Context mContext;
    private ImageView imageView;

    public picDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }


    /**
     * @Author luo
     * @Time 2021/6/28 9:33
     * @Description dialog中的实现
     */
    public void addDialog(ImageView im) {
        im.setOnClickListener(view -> dia.show());
        dia = new Dialog(mContext, R.style.edit_AlertDialog_style);
        dia.setContentView(R.layout.pic_dialog);
        imageView = dia.findViewById(R.id.ima_edit);
        imageView.setImageDrawable(im.getDrawable());
        setDialogMatchParent(dia);
    }
//    /**
//     *@Author
//     *@Time 2021/6/28 17:03
//     *@Description
//     */
//    private void edit(ImageView image) {
//        ImageView edit = dia.findViewById(R.id.edit);
//    }

    /**
     *@Author luo
     *@Time 2021/6/28 17:03
     *@Description 图片保存功能的实现
     */
    public void save(ImageView image) {
        TextView save = dia.findViewById(R.id.save);
        save.setOnClickListener(view -> {
            String[] PERMISSIONS = {
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE"};
            //检测是否有写的权限
            int permission = ContextCompat.checkSelfPermission(mContext,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, 1);
            }
            try {
                SavePhoto savePhoto = new SavePhoto(mContext);
                savePhoto.SaveBitmapFromView(imageView);
            } catch (ParseException | FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @Author luo
     * @Time 2021/6/28 14:39
     * @Description 用于消除当前dialog
     */
    public void dismissDia() {
        TextView dismiss = dia.findViewById(R.id.dismiss_dia);
        dismiss.setOnClickListener(view -> dia.dismiss());
    }


    /**
     * @Author luo
     * @Time 2021/6/28 10:34
     * @Description 设置dialog的大小
     */
    public void setDialogMatchParent(Dialog dialog) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height = dm.heightPixels; // 屏幕高度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        //这个地方可以用ViewGroup.LayoutParams.MATCH_PARENT属性，各位试试看看有没有效果
        layoutParams.width = width;
        layoutParams.height = height;
        dialog.getWindow().setAttributes(layoutParams);
    }

}
