package com.webbrowser.bigwhite.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.webbrowser.bigwhite.MainActivity;
import com.webbrowser.bigwhite.R;

import java.util.Timer;
import java.util.TimerTask;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        Timer timer = new Timer();
        timer.schedule(timerTask,2000);
    }
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            startActivity(new Intent(splash.this, MainActivity.class));
        }
    };
}
