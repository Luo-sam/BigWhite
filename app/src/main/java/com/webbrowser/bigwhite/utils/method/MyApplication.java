package com.webbrowser.bigwhite.utils.method;

import android.app.Application;
import android.content.Context;

import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.webbrowser.bigwhite.BuildConfig;

public class MyApplication extends Application {
    private static Context context;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;

        initDKPlayer();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    public void initDKPlayer() {
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                .setLogEnabled(BuildConfig.DEBUG)
                .setPlayerFactory(IjkPlayerFactory.create())
                //.setPlayerFactory(AndroidMediaPlayerFactory.create())
                .build());
    }


}
