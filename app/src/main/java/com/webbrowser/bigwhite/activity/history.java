package com.webbrowser.bigwhite.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.webbrowser.bigwhite.Model.SQLite.historyDao;
import com.webbrowser.bigwhite.Model.data.historyData;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.adapter.historyAdapter;

import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {
    private List<historyData> data;
    private List<historyData> temList;
    private historyDao history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        initHistory();
    }
    public void initHistory(){
        data = new ArrayList<>();
        /*初始化list中history的属性*/
        history = new historyDao(this);
        temList = history.queryHistory();
        reversedList();
        historyAdapter historyAdapter = new historyAdapter(history.this, R.layout.h_b_item,data);
        ListView historyList = findViewById(R.id.history_list);
        historyList.setAdapter(historyAdapter);
    }
    public void back(View view) {
        finish();
    }

    /*颠倒list顺序，用户输入的信息会从上依次往下显示*/
    private void reversedList(){
        data.clear();
        for(int i = temList.size() - 1 ; i >= 0 ; i --) {
            data.add(temList.get(i));
        }
    }

    public void clearHis(View view) {
        AlertDialog.Builder clearSure = new AlertDialog.Builder(history.this);
        clearSure.setPositiveButton("确认",
                (dialog, which) -> {
                    history.clearHistory();
                    initHistory();
                });

        clearSure.setNegativeButton("取消",(dialog, which) -> dialog.dismiss());
        clearSure.setTitle("提示");
        clearSure.setMessage("您确认清空搜索历史吗");
        clearSure.show();

    }
}
