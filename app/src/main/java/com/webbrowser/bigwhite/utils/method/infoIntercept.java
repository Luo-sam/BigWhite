package com.webbrowser.bigwhite.utils.method;

import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.webbrowser.bigwhite.Model.data.ilLegWebsite;

/**
 * @Author luo
 * @Time 2021/7/5 15:12
 * @Description 资讯拦截
 */
public class infoIntercept {
    public static void getData(Activity mActivity,WebView view, String url, LinearLayout Liner_search, FrameLayout web, LinearLayout searchHis, LinearLayout illegWebsite) {
        String[] data;
        data = ilLegWebsite.illegWebsite();
        for (int i = 0; i < data.length; i++) {
            if (url.equals(data[i])) {
                Toast.makeText(mActivity, "非法网站", Toast.LENGTH_LONG).show();
                view.stopLoading();
                Liner_search.setVisibility(View.GONE);
                web.setVisibility(View.GONE);
                searchHis.setVisibility(View.GONE);
                illegWebsite.setVisibility(View.VISIBLE);

            }
        }
    }
}
