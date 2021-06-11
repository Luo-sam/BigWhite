package com.webbrowser.bigwhite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.webbrowser.bigwhite.R;

public class chooseLoginRegister extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
    }


    public void login_onClick(View view) {
        startActivity(new Intent(chooseLoginRegister.this,login.class));
    }

    public void register_onClick(View view) {
        startActivity(new Intent(chooseLoginRegister.this,register.class));
    }

    public void back(View view) {
        finish();
    }
}
