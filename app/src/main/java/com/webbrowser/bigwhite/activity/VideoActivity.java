package com.webbrowser.bigwhite.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.VideoView;
import com.webbrowser.bigwhite.R;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;//视频播放器
    private StandardVideoController standardVideoController;//播放控制器
    private String videoURL = "https://vd2.bdstatic.com/mda-mf52c7c77vefqn5a/cae_h264_nowatermark/1622944010878835898/mda-mf52c7c77vefqn5a.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = (VideoView) findViewById(R.id.player);

        initPlayer(videoURL);

        videoView.addOnStateChangeListener(new VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                if (playState == VideoView.STATE_PREPARING)
                    Log.d("videoView", "STATE_PREPARING");
                else if (playState == VideoView.STATE_PREPARED)
                    Log.d("videoView", "STATE_PREPARED");
                else if (playState == VideoView.STATE_PLAYING) {
                    Log.d("videoView", "STATE_PREPARED");
                }
            }
        });
    }

    public void initPlayer(String videoURL) {
        videoView.setUrl(videoURL);

        standardVideoController = new StandardVideoController(this);
        standardVideoController.addDefaultControlComponent("标题", false);

        videoView.setVideoController(null);
        videoView.release();

        videoView.setVideoController(standardVideoController);

        videoView.setPlayerFactory(IjkPlayerFactory.create());

        videoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.resume();
            Log.d("videoView", "onResume");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause();
            Log.d("videoView", "onPause");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.release();
            Log.d("videoView", "onDestroy");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (videoView == null || !videoView.onBackPressed()) {
            Log.d("videoView", "onBackPressed");
        }
    }
}