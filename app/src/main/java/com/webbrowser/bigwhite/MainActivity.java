package com.webbrowser.bigwhite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.webbrowser.bigwhite.activity.TakePhotoPopWin;
import com.webbrowser.bigwhite.activity.bookmark;
import com.webbrowser.bigwhite.activity.history;
import com.webbrowser.bigwhite.activity.login;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /*log的标签*/
    private static final String TAG = "main";
    private Context mContext;
    private WebView webView;
    private ProgressBar progressBar;
    private EditText textUrl;
    private ImageView webIcon;
    private ImageView backright;
    private ImageView home;
    private ImageView morewindows;
    private ImageView my;
    private ImageView backleft;
    private ImageView btnStart;
    private long exitTime=0;
    private InputMethodManager manager;
    private static final String HTTP="http://";
    private static final String HTTPS="https://";
    private static final int PRESS_BACK_EXIT_GAP=2000;
    /*onCreate方法*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止底部导航栏上移
        getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        //mContext = MainActivity.this;
        manager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        /*打印成功显示在logcat*/
        Log.d(TAG, "onCreate: 创建成功");
        initView();
        initWeb();
    }
    private void initView(){
        /*对mContext初始化*/
        mContext = MainActivity.this;
        /*底部导航栏的id获取*/
        //ImageView backleft, backright, home, morewindows, my;
        //View view1=this.getLayoutInflater().inflate(R.layout.activity_main,null);
        //ImageView backleft = findViewById(R.id.backLeft);
        webView=findViewById(R.id.webView);
        backleft=findViewById(R.id.backLeft);
        backright = findViewById(R.id.backRight);
        home = findViewById(R.id.home);
        morewindows = findViewById(R.id.more_windows);
        btnStart=findViewById(R.id.btnStart);
        my = findViewById(R.id.my);
        textUrl=findViewById(R.id.textUrl);
        progressBar=findViewById(R.id.progressBar);
        webIcon=findViewById(R.id.webIcon);
        /*绑定点击事件*/
        backleft.setOnClickListener(this);
        backright.setOnClickListener(this);
        home.setOnClickListener(this);
        morewindows.setOnClickListener(this);
        my.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        //地址输入栏获取和失去焦点处理
        textUrl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    //显示当前网页的网址
                    textUrl.setText(webView.getUrl());
                    //将光标置于末尾
                    textUrl.setSelection(textUrl.getText().length());
                    //显示internet的图标
                    webIcon.setImageResource(R.drawable.internet);
                    //webIcon.setImageResource(R.drawable.internet);
                    //显示搜索按钮
                    btnStart.setImageResource(R.drawable.search);
                }else{
                    //显示网站名
                    textUrl.setText(webView.getTitle());
                    //显示网站图标
                    webIcon.setImageBitmap(webView.getFavicon());
                    //显示刷新按钮
                    btnStart.setImageResource(R.drawable.refresh);
                }
            }
        });
        textUrl.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode==keyEvent.KEYCODE_ENTER&&keyEvent.getAction()==keyEvent.ACTION_DOWN){
                    //执行搜索
                    btnStart.callOnClick();
                    textUrl.clearFocus();
                }
                return false;
            }
        });
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb(){
        webView.setWebViewClient(new MkWebViewClient());
        webView.setWebChromeClient(new MkWebChormeClient());
        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(settings.getUserAgentString()+"mkBrowser"+getVerName(mContext));
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setDomStorageEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //webView.loadUrl(getResources().getString(R.string.home_url));
    }
    //重写WebViewClient
    private class MkWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            //设置在webview点击打开的新网页在当前界面显示，而不跳转到新的浏览器中
            if(url==null){
                return true;
            }
            if(url.startsWith(HTTP)||url.startsWith(HTTPS)){
                view.loadUrl(url);
                return true;
            }
            //调用第三方应用，防止crash (如果手机上没有安装处/理某个scheme开头的url的APP, 会导致crash)
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }catch (Exception e){
                return true;
            }
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            super.onPageStarted(view,url,favicon);
            // 网页开始加载，显示进度条
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
            // 更新状态文字
            textUrl.setText("加载中...");
            // 切换默认网页图标
            webIcon.setImageResource(R.drawable.internet);
        }
        @Override
        public void onPageFinished(WebView view,String url){
            super.onPageFinished(view,url);
            // 网页加载完毕，隐藏进度条
            progressBar.setVisibility(View.INVISIBLE);
            // 改变标题
            setTitle(webView.getTitle());
            // 显示页面标题
            textUrl.setText(webView.getTitle());
        }
    }
    private class MkWebChormeClient extends WebChromeClient{
        private final  static int WEB_PROGRESS_MAX=100;
        @Override
        public void onProgressChanged(WebView view,int newProgress){
            super.onProgressChanged(view,newProgress);
            //加载进度条，刷新进度条
            progressBar.setProgress(newProgress);
            if(newProgress>0){
                if(newProgress==WEB_PROGRESS_MAX){
                    progressBar.setVisibility(View.INVISIBLE);
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        }
        @Override
        public void onReceivedIcon(WebView view,Bitmap icon){
            super.onReceivedIcon(view,icon);
            webIcon.setImageBitmap(icon);
        }
        @Override
        public void onReceivedTitle(WebView view,String title){
            super.onReceivedTitle(view,title);
            setTitle(title);
            textUrl.setText(title);
        }

    }
    private TakePhotoPopWin takePhotoPopWin;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btnStart){
            if(textUrl.hasFocus()){
                if(manager.isActive()){
                    manager.hideSoftInputFromWindow(textUrl.getApplicationWindowToken(),0);
                }
                String input = textUrl.getText().toString();
                if(!isHttpUrl(input)){
                    try {
                        input= URLEncoder.encode(input,"utf-8");
                    }catch (UnsupportedEncodingException e){
                        e.printStackTrace();
                    }
                    input="https://www.baidu.com/s?wd="+input+"&ie=UTF-8";
                }
                webView.loadUrl(input);
                textUrl.clearFocus();
            }else {
                webView.reload();
            }
        }
        if(id == R.id.backLeft){
            webView.goBack();
        }else if(id == R.id.backRight){
            webView.goForward();
        }else if(id == R.id.home){
            Intent intent=new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if(id == R.id.more_windows){
            Toast.makeText(mContext, "功能开发中", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.my){
            showPopFormBottom();
    }
  }
    @Override
    protected void onPause(){
        super.onPause();
        try{
            webView.getClass().getMethod("onPause").invoke(webView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        try {
            webView.getClass().getMethod("onResume").invoke(webView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static String getVerName(Context context){
        String verName="unKnow";
        try{
            verName=context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return verName;
    }
    public static boolean isHttpUrl(String urls){
        boolean isUrl;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";

        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(urls.trim());
        isUrl = mat.matches();
        return isUrl;
    }

    public void showPopFormBottom(){

        takePhotoPopWin  = new TakePhotoPopWin(this,onClickListener);
        takePhotoPopWin.showAtLocation(findViewById(R.id.my), Gravity.CENTER,0,0);
    }
    @Override
    public void onBackPressed(){
        //返回上一页
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            if((System.currentTimeMillis()-exitTime)>PRESS_BACK_EXIT_GAP){
                //点击两次就退出
                Toast.makeText(mContext,"再按一次退出浏览器",Toast.LENGTH_SHORT).show();
                exitTime=System.currentTimeMillis();
            }else {
                super.onBackPressed();
            }
        }
    }



    private final View.OnClickListener onClickListener = v -> {
        int id = v.getId();
        if(id == R.id.login){
            startActivity(new Intent(MainActivity.this, login.class));
        }else if(id == R.id.add_bookmark){
            Toast.makeText(mContext, "添加书签功能开发中", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.bookmark){
            startActivity(new Intent(MainActivity.this, bookmark.class));
        }else if(id == R.id.history){
            takePhotoPopWin.dismiss();
            startActivity(new Intent(MainActivity.this, history.class));
        }else if(id == R.id.exit){
            Toast.makeText(mContext, "退出登录功能开发中", Toast.LENGTH_SHORT).show();
        }
    };
}