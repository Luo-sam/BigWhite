package com.webbrowser.bigwhite.utils.method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.webbrowser.bigwhite.MainActivity;
import com.webbrowser.bigwhite.Model.SQLite.bookmarkDao;
import com.webbrowser.bigwhite.Model.SQLite.historyDao;
import com.webbrowser.bigwhite.Model.data.bookmarkResponse;
import com.webbrowser.bigwhite.Model.data.historyData;
import com.webbrowser.bigwhite.Model.data.historyResponse;
import com.webbrowser.bigwhite.Model.data.responseData_put;
import com.webbrowser.bigwhite.View.myView.MingWebView;
import com.webbrowser.bigwhite.activity.login;
import com.webbrowser.bigwhite.utils.httpUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * 保存历史记录到本地
 */
public class saveInfoToThis {
    public static void saveHistory(Context mContext, EditText textUrl, MingWebView webView, Activity mActivity, historyDao history) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            String name = textUrl.getText().toString().trim();
            String address = webView.getUrl();
            historyResponse.DataBean his = new historyResponse.DataBean(name, address);
            /*添加到远程仓库*/
            SharedPreferences sp = mActivity.getSharedPreferences("sp_list", MODE_PRIVATE);
            String head = sp.getString("token", "");
            String ifHistory = sp.getString("history", "");

            if (!name.equals("百度一下")) {
                if (history.isHasRecord(his)) {
                    history.clearThisMess(his);
                }
                /*添加到本地仓库*/
                history.addHistory(his);
                if (ifHistory.equals("true")) {
                    httpUtils.putHistory(head, "http://139.196.180.89:8137/api/v1/histories", name, address, new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            mActivity.runOnUiThread(() -> showToast("上传历史记录网络错误", mContext));
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
                                    showToast("上传历史记录成功", mContext);
                                } else {
                                    showToast("上传历史记录失败", mContext);
                                }
                            });
                        }
                    });
                }
            }
        }, 2000);
    }

    public static void saveBookmark(bookmarkDao bookmarkDao, String name,
                                    String url, String fileName, Activity mActivity) {
        bookmarkDao.addBookmark(new historyData(name, url), fileName);
        SharedPreferences sp = mActivity.getSharedPreferences("sp_list", MODE_PRIVATE);
        String head = sp.getString("token", "");
        String ifBookmark = sp.getString("bookmark", "");
        if (ifBookmark.equals("true")) {
            httpUtils.putBookMark(head, "http://139.196.180.89:8137/api/v1/collections", fileName, name, url, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    mActivity.runOnUiThread(() -> showToast("上传标签网络错误", mActivity));
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
                            showToast("添加到后端成功", mActivity);
                        } else {
                            mActivity.startActivity(new Intent(mActivity, login.class));
                        }
                    });
                }
            });
        }
    }

    private static List<historyResponse.DataBean> temListBack;
    private static List<bookmarkResponse.DataBean> bookmarkTemList;

    public static void loginFrom(String acc, String key, String address, Activity mActivity) {
        httpUtils.loginWithOkHttp(address, acc, key, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                /*异常处理*/
                showToast("网络错误", mActivity);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                /*得到的服务器返回值具体内容*/
                assert response.body() != null;
                final String responseHeader = response.header("Authorization");

                mActivity.runOnUiThread(() -> {
                    if (responseHeader == null) {
                        showToast("账号密码错误", mActivity);
                    } else {
                        showToast("登录成功", mActivity);
                        saveToSp("token", responseHeader, mActivity);
                        saveToSp("Name",acc,mActivity);
                        saveToSp("history","true",mActivity);
                        saveToSp("bookmark","true",mActivity);
                        toActivity(MainActivity.class, mActivity);
                    }
                });

            }
        });
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            /*初始化list中history的属性*/
            historyDao history = new historyDao(mActivity);
            bookmarkDao bookmarkDao = new bookmarkDao(mActivity);
            SharedPreferences sp = mActivity.getSharedPreferences("sp_list", MODE_PRIVATE);
            String head = sp.getString("token", "");
            if (!head.equals("")) {
                String backAddress = "http://139.196.180.89:8137/api/v1/histories";
                httpUtils.getHistoryOrCollectionFromBack(backAddress, head, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        mActivity.runOnUiThread(() -> showToast("获取历史记录网络错误", mActivity));
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        /*得到的服务器返回值具体内容*/
                        assert response.body() != null;
                        final String responseData = response.body().string();
                        mActivity.runOnUiThread(() -> {
                            Gson gson = new Gson();
                            historyResponse responsePut = gson.fromJson(responseData, historyResponse.class);

                            if (responsePut.getState().getCode() == 0) {
                                temListBack = responsePut.getData();
                                history.clearHistory();
                                history.addHistoryFromBack(temListBack);
                            } else {
                                showToast("由后端更新历史记录失败", mActivity);
                            }
                        });

                    }
                });
            }
            /*获取用户的所有书签*/
            if (!head.equals("")) {
                String backAddress = "http://139.196.180.89:8137/api/v1/collections";
                httpUtils.getHistoryOrCollectionFromBack(backAddress, head, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        mActivity.runOnUiThread(() -> showToast("获取历史记录网络错误", mActivity));
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        /*得到的服务器返回值具体内容*/
                        assert response.body() != null;
                        final String responseData = response.body().string();
                        mActivity.runOnUiThread(() -> {
                            Gson gson = new Gson();
                            bookmarkResponse responsePut = gson.fromJson(responseData, bookmarkResponse.class);
                            if (responsePut.getState().getCode() == 0) {
                                bookmarkTemList = responsePut.getData();
                                bookmarkDao.clearBookmark();
                                bookmarkDao.addBookmarkFromBack(bookmarkTemList);
                                showToast("更新书签记录成功", mActivity);
                            } else {
                                showToast("由后端更新书签记录失败", mActivity);
                            }

                        });

                    }
                });
            }
        }, 2000);
    }

    protected static void saveToSp(String key, String val, Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences("sp_list", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static void showToast(String str, Context mContext) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }

    public static void toActivity(Class cls, Context mContext) {
        Intent intent = new Intent(mContext, cls);
        mContext.startActivity(intent);
    }
}
