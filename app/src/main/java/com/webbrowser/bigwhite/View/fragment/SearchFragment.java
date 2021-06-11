package com.webbrowser.bigwhite.View.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.activity.popWindows.myPopWin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SearchFragment extends Fragment implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static WebView webView;

    private InputMethodManager manager;

    /*对mContext初始化*/
    private final Context mContext = getContext();
    private ProgressBar progressBar;
    private EditText textUrl;
    private ImageView webIcon;
    private ImageView btnStart;

    private final long exitTime=0;
    private static final String HTTP="http://";
    private static final String HTTPS="https://";
    private static final int PRESS_BACK_EXIT_GAP=2000;

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_search,container,false);
        initView(view);
        initWeb(view);
        return view;
    }

    /*初始化得到的view*/
    private void initView(View view){
        /*键盘事件绑定*/
        manager=(InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
        /*搜索ID的获取*/
        webView= view.findViewById(R.id.webView);
        btnStart= view.findViewById(R.id.btnStart);
        textUrl= view.findViewById(R.id.textUrl);
        progressBar= view.findViewById(R.id.progressBar);
        webIcon= view.findViewById(R.id.webIcon);
        /*绑定点击事件*/
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
                    //显示搜索按钮
                    btnStart.setImageResource(R.drawable.search);
                    /*显示搜索历史记录弹窗*/
                    showDownSearchHistory(view);
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
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode== KeyEvent.KEYCODE_ENTER &&event.getAction()== KeyEvent.ACTION_DOWN){
                    //执行搜索
                    btnStart.callOnClick();
                    textUrl.clearFocus();
                }
                return false;
            }
        });
    }

    /*搜索历史记录弹窗*/
    public void showDownSearchHistory(View view){
        /*个人栏弹窗*/
        com.webbrowser.bigwhite.activity.popWindows.myPopWin myPopWin = new myPopWin(mActivity, onClickListener);
        myPopWin.showAtLocation(view.findViewById(R.id.textUrl), Gravity.CENTER,0,0);
    }
    private final View.OnClickListener onClickListener = v -> {
        int id = v.getId();
    };


    /*初始化webView*/
    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb(View view){
        webView.setWebViewClient(new MkWebViewClient());
        webView.setWebChromeClient(new MkWebChormeClient());
        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(settings.getUserAgentString()+"mkBrowser");
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
        webView.loadUrl(getResources().getString(R.string.home_url));
    }

    private class MkWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            //设置在webView点击打开的新网页在当前界面显示，而不跳转到新的浏览器中
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
            getActivity().setTitle(webView.getTitle());
            // 显示页面标题
            textUrl.setText(webView.getTitle());
        }
    }
    private class MkWebChormeClient extends WebChromeClient {
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
            getActivity().setTitle(title);
            textUrl.setText(title);
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


    /*实现webView的goBack效果*/
    public static void goToBack(){
        webView.goBack();
    }



    /*解决视频声音问题的方法*/
    @Override
    public void onPause() {
        super.onPause();
        try{
            webView.getClass().getMethod("onPause").invoke(webView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            webView.getClass().getMethod("onResume").invoke(webView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btnStart){
            if(textUrl.hasFocus()){
                //隐藏软键盘
                if(manager.isActive()){
                    manager.hideSoftInputFromWindow
                            (textUrl.getApplicationWindowToken(),0);
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
    }


}
