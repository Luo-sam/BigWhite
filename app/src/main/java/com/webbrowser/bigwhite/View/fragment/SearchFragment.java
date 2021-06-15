package com.webbrowser.bigwhite.View.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webbrowser.bigwhite.Model.SQLite.RecordsDao;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.adapter.searchHistoryAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchFragment extends Fragment implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static WebView webView;
    private View view;
    private InputMethodManager manager;

    private FrameLayout web;
    private LinearLayout searchHis;
    private List<String> data;
    private List<String> temList;

    private ProgressBar progressBar;
    private EditText textUrl;
    private ImageView webIcon;
    private ImageView btnStart;
    private RecordsDao sc;

    private static final String HTTP="http://";
    private static final String HTTPS="https://";

    private Activity mActivity;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.web_search,container,false);
        sc = new RecordsDao(mActivity);
        initView(view);
        initWeb();
        initList();
        return view;
    }

    /*初始化搜索历史*/
    private void initList() {
        data = new ArrayList<>();
        temList = sc.getRecordsList();
        reversedList();
        searchHistoryAdapter scAdapter = new searchHistoryAdapter(mActivity,data);
        ListView scList  = view.findViewById(R.id.sc_history_list);
        scList.setAdapter(scAdapter);

        scList.setOnItemClickListener((parent, view, position, id) -> {
            textUrl.setText(data.get(position));
            btnStart.callOnClick();
            textUrl.clearFocus();
            Toast.makeText(mActivity,data.get(position),Toast.LENGTH_SHORT).show();
        });

    }

    /*初始化得到的view*/
    private void initView(View view){
        /*对两个页面的初始化*/
        web = view.findViewById(R.id.web);
        searchHis = view.findViewById(R.id.search_his);
        searchHis.setVisibility(View.GONE);

        /*键盘事件绑定*/
        manager=(InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        /*搜索ID的获取*/
        webView= view.findViewById(R.id.webView);
        btnStart= view.findViewById(R.id.btnStart);
        textUrl= view.findViewById(R.id.textUrl);
        progressBar= view.findViewById(R.id.progressBar);
        webIcon= view.findViewById(R.id.webIcon);
        TextView clear = view.findViewById(R.id.clear);
        /*绑定点击事件*/
        btnStart.setOnClickListener(this);
        /*设置webIcon的点击事件返回*/
        webIcon.setOnClickListener(this);
        /*绑定清空搜索历史功能*/
        clear.setOnClickListener(this);
        //地址输入栏获取和失去焦点处理
        textUrl.setOnFocusChangeListener((popView, hasFocus) -> {
            if(hasFocus){
                //显示back的图标
                webIcon.setImageResource(R.drawable.left);
                //显示搜索按钮
                btnStart.setImageResource(R.drawable.search);
                /*设置点击搜索获取焦距后使得搜索历史的layout界面出现*/
                web.setVisibility(View.GONE);
                searchHis.setVisibility(View.VISIBLE);

            }else{
                //显示网站名
                textUrl.setText(webView.getTitle());
                //显示网站图标
                webIcon.setImageBitmap(webView.getFavicon());
                //显示刷新按钮
                btnStart.setImageResource(R.drawable.refresh);
                /*当失去焦距时，显示的界面切换*/
                web.setVisibility(View.VISIBLE);
                searchHis.setVisibility(View.GONE);
            }
        });
        textUrl.setOnKeyListener((v, keyCode, event) -> {
            if(keyCode== KeyEvent.KEYCODE_ENTER &&event.getAction()== KeyEvent.ACTION_DOWN){
                //执行搜索
                btnStart.callOnClick();
                textUrl.clearFocus();
            }
            return false;
        });
    }

    /*初始化webView*/
    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb(){
        webView.setWebViewClient(new MkWebViewClient());
        webView.setWebChromeClient(new MkWebChromeClient());
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
            mActivity.setTitle(webView.getTitle());
            // 显示页面标题
            textUrl.setText(webView.getTitle());
        }
    }
    private class MkWebChromeClient extends WebChromeClient {
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
            mActivity.setTitle(title);
            textUrl.setText(title);
        }

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
    /*实现webView的goForward效果*/
    public static void goToForward(){
        webView.goForward();
    }
    /*判断webView是否可以goBack*/
    public static boolean canBack(){
        return webView.canGoBack();
    }
    /*颠倒list顺序，用户输入的信息会从上依次往下显示*/
    private void reversedList(){
        data.clear();
        for(int i = temList.size() - 1 ; i >= 0 ; i --) {
            data.add(temList.get(i));
        }
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
                sc.addRecords(input);
                initList();
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
        if(id ==R.id.webIcon){
            if (textUrl.hasFocus()){
                //隐藏软键盘
                if(manager.isActive()){
                    manager.hideSoftInputFromWindow
                            (textUrl.getApplicationWindowToken(),0);
                }
                textUrl.clearFocus();
            }
        }
        if(id == R.id.clear){
            AlertDialog.Builder clearSure = new AlertDialog.Builder(mActivity);
            clearSure.setPositiveButton("确认",
                    (dialog, which) -> {
                        sc.deleteAllRecords();
                        SearchFragment.this.initList();
                    });

            clearSure.setNegativeButton("取消",(dialog, which) -> dialog.dismiss());
            clearSure.setTitle("提示");
            clearSure.setMessage("您确认清空搜索历史吗");
            clearSure.show();
        }
    }
}
