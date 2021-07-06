package com.webbrowser.bigwhite.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.webbrowser.bigwhite.R;

public class personalCenterActivity extends BaseActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_center);
        Switch switchBookmark = findViewById(R.id.sync_Bookmark);
        Switch switchHistory = findViewById(R.id.sync_History);
        boolean bookmark = Boolean.parseBoolean(getStringFromSp("bookmark"));
        switchBookmark.setChecked(bookmark);
        boolean history = Boolean.parseBoolean(getStringFromSp("history"));
        switchHistory.setChecked(history);
        switchBookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "";
                if (isChecked) {
                    str = "开启";
                    saveToSp("bookmark", "true");
                } else {
                    str = "关闭";
                    saveToSp("bookmark", "false");
                }
                Toast.makeText(personalCenterActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
        switchHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "";
                if (isChecked) {
                    str = "开启";
                    saveToSp("history", "true");
                } else {
                    str = "关闭";
                    saveToSp("history", "false");
                }
                Toast.makeText(personalCenterActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }


}
