package com.webbrowser.bigwhite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.webbrowser.bigwhite.MainActivity;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.fragment.SearchFragment;

public class infoDetail extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_detail);
        Intent intent = getIntent();
        String s = intent.getExtras().getString("url");
        showToast(s);

        Log.d("recourseUel",getClass().getSimpleName());
    }

    public void back(View view) {
        setResult(123);
        finish();
    }
}
