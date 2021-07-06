package com.webbrowser.bigwhite.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.hb.dialog.myDialog.ActionSheetDialog;
import com.webbrowser.bigwhite.Model.SQLite.historyDao;
import com.webbrowser.bigwhite.Model.data.deleteThisHis;
import com.webbrowser.bigwhite.Model.data.historyResponse;
import com.webbrowser.bigwhite.Model.data.simpleResponse;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.adapter.historyBackAdapter;
import com.webbrowser.bigwhite.utils.httpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class history extends BaseActivity {

    private List<historyResponse.DataBean> dataBack;
    private List<historyResponse.DataBean> temListBack;
    private historyDao history;
    private String head;

    public history() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        initHistory();
    }

    public void initHistory() {
        dataBack = new ArrayList<>();
        history = new historyDao(this);
        SharedPreferences sp = getSharedPreferences("sp_list", MODE_PRIVATE);
        String head = sp.getString("token", "");
        String ifHistory = sp.getString("history","");
        ListView historyList = findViewById(R.id.history_list);
        temListBack = history.queryHistory();
        reversedListBack();
        historyBackAdapter historyAdapter = new historyBackAdapter(history.this, R.layout.h_b_item, dataBack);
        historyList.setAdapter(historyAdapter);
        historyList.setOnItemClickListener((parent, view, position, id) -> {
            String address = dataBack.get(position).getUrl();
            Intent intent = new Intent();
            intent.putExtra("address", address);
            history.this.setResult(456, intent);
            finish();
        });

        historyList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            ActionSheetDialog dialog = new ActionSheetDialog(history.this).builder().setTitle("请选择")
                    .addSheetItem("删除当前信息", null, which -> {
                        historyResponse.DataBean hs = dataBack.get(i);
                        history.clearThisMess(hs);
                        if(ifHistory.equals("true")){
                            String backAddress1 = "http://139.196.180.89:8137/api/v1/histories/"+hs.getId();
                            httpUtils.delete(backAddress1, head, new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    runOnUiThread(() -> showToast("获取历史记录网络错误"));
                                }
                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    /*得到的服务器返回值具体内容*/
                                    assert response.body() != null;
                                    final String responseData = response.body().string();
                                    runOnUiThread(() -> {
                                        Gson gson = new Gson();
                                        deleteThisHis responsePut = gson.fromJson(responseData, deleteThisHis.class);
                                        if (responsePut.getState().getCode() == 0) {
                                            showToast("删除成功");
                                        } else {
                                            showToast("由后端更新历史记录失败");
                                        }
                                    });
                                }
                            });
                        }
                        initHistory();
                    }).addSheetItem("删除全部信息", null, whi -> {
                        AlertDialog.Builder clearSure = new AlertDialog.Builder(history.this);
                        clearSure.setPositiveButton("确认",
                                (dialog1, which) -> {
                                    history.clearHistory();
                                    initHistory();
                                    if(ifHistory.equals("true")){
                                        String backAddress1 = "http://139.196.180.89:8137/api/v1/histories";
                                        httpUtils.delete(backAddress1, head, new Callback() {
                                            @Override
                                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                runOnUiThread(() -> showToast("获取历史记录网络错误"));
                                            }
                                            @Override
                                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                /*得到的服务器返回值具体内容*/
                                                assert response.body() != null;
                                                final String responseData = response.body().string();
                                                runOnUiThread(() -> {
                                                    Gson gson = new Gson();
                                                    simpleResponse responsePut = gson.fromJson(responseData, simpleResponse.class);
                                                    if (responsePut.getState().getCode() == 0) {
                                                        showToast("删除成功");
                                                    } else {
                                                        showToast("由后端更新标签记录失败");
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                        clearSure.setNegativeButton("取消", (dialog1, which) -> dialog1.dismiss());
                        clearSure.setTitle("提示");
                        clearSure.setMessage("您确认清空搜索历史吗");
                        clearSure.show();
                    });
            dialog.show();
            return true;
        });


    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {

    }
    private void reversedListBack() {
        dataBack.clear();
        for (int i = temListBack.size() - 1; i >= 0; i--) {
            dataBack.add(temListBack.get(i));
        }
    }
}
