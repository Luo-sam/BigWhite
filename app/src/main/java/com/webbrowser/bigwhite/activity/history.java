package com.webbrowser.bigwhite.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.webbrowser.bigwhite.Model.SQLite.historyDao;
import com.webbrowser.bigwhite.Model.data.historyData;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.adapter.historyAdapter;

import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        /*初始化list中history的属性*/
        List<historyData> data = new ArrayList<>();
        historyDao history = new historyDao(this);
        /*历史记录初始化*/
        history.initHistory();
        historyData b = new historyData("腾讯","www.baidu.com");
        history.addHistory(b);
        Log.d("add data", "cg");
        data = history.queryHistory();
        historyAdapter historyAdapter = new historyAdapter(history.this, R.layout.h_b_item,data);
        ListView historyList = findViewById(R.id.history_list);
        historyList.setAdapter(historyAdapter);
    }

    public void back(View view) {
        finish();
    }
}
