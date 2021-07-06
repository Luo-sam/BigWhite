package com.webbrowser.bigwhite.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;

import com.webbrowser.bigwhite.R;

public class personalCenterActivity extends AppCompatActivity {
    final boolean flag1=false;
    final boolean flag2=false;
    SharedPreferences preferences1;
    SharedPreferences preferences2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_center);
        Switch switchBookmark = findViewById(R.id.sync_Bookmark);
        Switch switchHistory = findViewById(R.id.sync_History);
        preferences1 = getSharedPreferences("user1",Context.MODE_PRIVATE);
        preferences2 =getSharedPreferences("user2",Context.MODE_PRIVATE);
        if(preferences1!=null){
            boolean name=preferences1.getBoolean("flag",flag1);
            switchBookmark.setChecked(name);
        }
        if(preferences2!=null){
            boolean name=preferences2.getBoolean("flag",flag2);
            switchHistory.setChecked(name);
        }
        switchBookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "";
                if(isChecked==true){
                    str="开启";
                    SharedPreferences preferences1= getSharedPreferences("user1",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences1.edit();
                    editor.putBoolean("flag",true);
                    editor.commit();
                }else {
                    str="关闭";
                    SharedPreferences preferences1= getSharedPreferences("user1",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences1.edit();
                    editor.putBoolean("flag",false);
                    editor.commit();
                }
                Toast.makeText(personalCenterActivity.this,str,Toast.LENGTH_SHORT).show();
            }
        });
        switchHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String str = "";
                if(isChecked==true){
                    str="开启";
                    SharedPreferences preferences2= getSharedPreferences("user2",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences2.edit();
                    editor.putBoolean("flag",true);
                    editor.commit();
                }else {
                    str="关闭";
                    SharedPreferences preferences2= getSharedPreferences("user2",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences2.edit();
                    editor.putBoolean("flag",false);
                    editor.commit();
                }
                Toast.makeText(personalCenterActivity.this,str,Toast.LENGTH_SHORT).show();
            }
        });
    }


}
