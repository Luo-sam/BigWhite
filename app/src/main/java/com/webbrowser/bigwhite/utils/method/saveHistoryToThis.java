package com.webbrowser.bigwhite.utils.method;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.webbrowser.bigwhite.Model.SQLite.historyDao;
import com.webbrowser.bigwhite.Model.data.historyResponse;
import com.webbrowser.bigwhite.Model.data.responseData_put;
import com.webbrowser.bigwhite.View.myView.MingWebView;
import com.webbrowser.bigwhite.utils.httpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 *保存历史记录到本地
 */
public class saveHistoryToThis {
    public static void saveHistory(Context mContext, EditText textUrl, MingWebView webView, Activity mActivity, historyDao history) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            String name = textUrl.getText().toString().trim();
            String address = webView.getUrl();
            historyResponse.DataBean his = new historyResponse.DataBean(name, address);
            /*添加到远程仓库*/
            SharedPreferences sp = mActivity.getSharedPreferences("sp_list", MODE_PRIVATE);
            String head = sp.getString("token", "");
            if (!name.equals("百度一下")) {
                if (history.isHasRecord(his)) {
                    history.clearThisMess(his);
                }

                /*添加到本地仓库*/
                history.addHistory(his);
                httpUtils.putHistory(head, "http://139.196.180.89:8137/api/v1/histories", name, address, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        mActivity.runOnUiThread(() -> showToast("上传历史记录网络错误",mContext));
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        /*得到的服务器返回值具体内容*/
                        assert response.body() != null;
                        final String responseData = response.body().string();
                        Gson gson = new Gson();
                        responseData_put responsePut = gson.fromJson(responseData, responseData_put.class);
                        mActivity.runOnUiThread(() -> {
                            if (responsePut.getState().getCode() == 0) {
                                showToast("上传历史记录成功",mContext);
                            } else {
                                showToast("上传历史记录失败",mContext);
                            }
                        });
                    }
                });

            }
        }, 2000);
    }
    public static void showToast(String str, Context mContext){
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }
}
