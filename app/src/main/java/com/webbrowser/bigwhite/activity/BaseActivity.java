package com.webbrowser.bigwhite.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    public void showToast(String str){
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }
    public void toActivity(Class cls){
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }
    protected void saveToSp(String key,String val){
        SharedPreferences sp = getSharedPreferences("sp_list",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,val);
        editor.apply();
    }
    protected String getStringFromSp(String key){
        SharedPreferences sp = getSharedPreferences("sp_list",MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public abstract void onClick(View v);
}

