package com.webbrowser.bigwhite.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.webbrowser.bigwhite.MainActivity;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.fragment.SearchFragment;

public class infoDetail extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_detail);
        Log.d("recourseUel",getClass().getSimpleName());
    }

    public void back(View view) {

        finish();
    }
}
