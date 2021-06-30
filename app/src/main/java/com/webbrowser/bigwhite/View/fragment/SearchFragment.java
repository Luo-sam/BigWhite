package com.webbrowser.bigwhite.View.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.webbrowser.bigwhite.MainActivity;
import com.webbrowser.bigwhite.Model.SQLite.RecordsDao;
import com.webbrowser.bigwhite.Model.SQLite.bookmarkDao;
import com.webbrowser.bigwhite.Model.SQLite.historyDao;
import com.webbrowser.bigwhite.Model.data.NewsData;
import com.webbrowser.bigwhite.Model.data.historyData;
import com.webbrowser.bigwhite.Model.data.ilLegWebsite;
import com.webbrowser.bigwhite.Model.data.responseData_put;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.action.OnRcvScrollListener;
import com.webbrowser.bigwhite.View.adapter.newsAdapter;
import com.webbrowser.bigwhite.View.adapter.searchHistoryAdapter;
import com.webbrowser.bigwhite.activity.infoDetail;
import com.webbrowser.bigwhite.activity.login;
import com.webbrowser.bigwhite.utils.CrawlPageUtil;
import com.webbrowser.bigwhite.utils.OkHttpUtil;
import com.webbrowser.bigwhite.utils.httpUtils;
import com.webbrowser.bigwhite.widget.MingWebView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.http.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


public class SearchFragment extends Fragment implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private MingWebView webView;
    private View view;
    private InputMethodManager manager;

    private FrameLayout web;
    private LinearLayout searchHis;
    private List<String> data;
    private List<String> temList;
    private RecordsDao sc;

    private LinearLayout Liner_search;

    private ProgressBar progressBar;
    private EditText textUrl;
    private ImageView webIcon;
    private ImageView btnStart;
    private LinearLayout illegWebsite;

    private LinearLayout linearLayout;
    private TextView textView;
    private TextView title;
    private TextView author;

    /*上传用的token*/
    private String token;


    /*历史记录*/
    private historyDao history;


    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";

    private Activity mActivity;
    private LinearLayout advisory;


    //返回非法网站页面
    public LinearLayout getIllegWebsite() {
        return illegWebsite;
    }

    public LinearLayout getSearchHis() {
        return searchHis;
    }

    public EditText getTextUrl() {
        return textUrl;
    }

    //返回webview
    public FrameLayout getWeb() {
        return web;
    }

    //返回搜索栏

    public LinearLayout getLiner_search() {
        return Liner_search;
    }

    public WebView getWebView() {
        return webView;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.web_search, container, false);
        sc = new RecordsDao(mActivity);
        initView(view);
        initWeb();
        initList();
        initHistory();
        return view;
    }


    private void initHistory() {
        history = new historyDao(mActivity);
    }

    /*初始化搜索历史*/
    private void initList() {
        data = new ArrayList<>();
        temList = sc.getRecordsList();
        reversedList();
        searchHistoryAdapter scAdapter = new searchHistoryAdapter(mActivity, data);
        ListView scList = view.findViewById(R.id.sc_history_list);
        scList.setAdapter(scAdapter);

        scList.setOnItemClickListener((parent, view, position, id) -> {
            textUrl.setText(data.get(position));
            btnStart.callOnClick();
            textUrl.clearFocus();
            Toast.makeText(mActivity, data.get(position), Toast.LENGTH_SHORT).show();
        });

    }

    /*初始化得到的view*/
    private void initView(View view) {
        linearLayout=view.findViewById(R.id.linearLayout);
        advisory=view.findViewById(R.id.advisory);
        advisory.setVisibility(View.GONE);
        //mainActivity=(MainActivity)getActivity();
        illegWebsite = view.findViewById(R.id.illeg);
        illegWebsite.setVisibility(View.GONE);
        textView = view.findViewById(R.id.goback);

        /*对两个页面的初始化*/
        Liner_search = view.findViewById(R.id.linearLayout);
        web = view.findViewById(R.id.web);
        searchHis = view.findViewById(R.id.search_his);
        searchHis.setVisibility(View.GONE);//隐藏
        title=view.findViewById(R.id.title);
        author=view.findViewById(R.id.author);



        /*键盘事件绑定*/
        manager = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        /*搜索ID的获取*/
        webView = view.findViewById(R.id.webView);
        btnStart = view.findViewById(R.id.btnStart);
        textUrl = view.findViewById(R.id.textUrl);
        progressBar = view.findViewById(R.id.progressBar);
        webIcon = view.findViewById(R.id.webIcon);
        TextView clear = view.findViewById(R.id.clear);
        /*绑定点击事件*/
        btnStart.setOnClickListener(this);
        /*设置webIcon的点击事件返回*/
        webIcon.setOnClickListener(this);
        /*绑定清空搜索历史功能*/
        clear.setOnClickListener(this);
        textView.setOnClickListener(this);
        //地址输入栏获取和失去焦点处理
        textUrl.setOnFocusChangeListener((popView, hasFocus) -> {
            if (hasFocus) {
                //设置聚焦时为网址
                textUrl.setText(webView.getUrl());
                //显示back的图标
                webIcon.setImageResource(R.drawable.left);
                //显示搜索按钮
                btnStart.setImageResource(R.drawable.search);
                /*设置点击搜索获取焦距后使得搜索历史的layout界面出现*/
                web.setVisibility(View.GONE);
                searchHis.setVisibility(View.VISIBLE);

            } else {
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
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                //执行搜索
                btnStart.callOnClick();
                textUrl.clearFocus();
            }
            return false;
        });
    }

    /*初始化webView*/
    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb() {
        webView.setWebViewClient(new MkWebViewClient());
        webView.setWebChromeClient(new MkWebChromeClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString(settings.getUserAgentString() + "mkBrowser");
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.loadUrl(getResources().getString(R.string.home_url));

    }

    private class MkWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //设置在webView点击打开的新网页在当前界面显示，而不跳转到新的浏览器中
            if (url == null) {
                return true;
            }
            if (url.startsWith(HTTP) || url.startsWith(HTTPS)) {
                view.loadUrl(url);
                return true;
            }
            //调用第三方应用，防止crash (如果手机上没有安装处/理某个scheme开头的url的APP, 会导致crash)
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            } catch (Exception e) {
                return true;
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            String[] data = new String[3];
            data = ilLegWebsite.illegWebsite();
            for (int i = 0; i < data.length; i++) {
                if (url.equals(data[i])) {
                    Toast.makeText(getActivity(), "非法网站", Toast.LENGTH_LONG).show();
                    view.stopLoading();
                    Liner_search.setVisibility(View.GONE);
                    web.setVisibility(View.GONE);
                    searchHis.setVisibility(View.GONE);
                    illegWebsite.setVisibility(View.VISIBLE);

                }
            }
            super.onPageStarted(view, url, favicon);
            // 网页开始加载，显示进度条
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
            // 更新状态文字
            textUrl.setText("加载中...");
            // 切换默认网页图标
            webIcon.setImageResource(R.drawable.internet);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (view.getUrl().contains("https://www.baidu.com/#iact=wiseindex%")) {
                String viewUrl = view.getUrl();
                String pattern = "news_(\\d+)%";

                // 创建 Pattern 对象
                Pattern r = Pattern.compile(pattern);

                // 现在创建 matcher 对象
                Matcher m = r.matcher(viewUrl);
                if (m.find( )) {
                    String id = m.group(1);
                    viewUrl = "https://mbd.baidu.com/newspage/data/landingpage?s_type=news&dsp=wise&context=%7B%22nid%22%3A%22news_" +
                            id +
                            "%22%7D&pageType=1&n_type=1&p_from=-1&quot";

                    if(!CrawlPageUtil.newsMap.containsKey(viewUrl)) {
                        String html = null;
                        try {
                            html = OkHttpUtil.OkGetArt(viewUrl);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        NewsData newsData = CrawlPageUtil.spiderNewsData(html, viewUrl);
                        CrawlPageUtil.newsMap.put(viewUrl, newsData);
                        CrawlPageUtil.currentNews = newsData;
                    }
                    else
                        CrawlPageUtil.currentNews = CrawlPageUtil.newsMap.get(url);

                } else {
//                    System.out.println("NO MATCH");
                    // 默认是视频
                    String html = null;
                    try {
                        viewUrl = OkHttpUtil.getRealVideoUrl(viewUrl);
                        html = OkHttpUtil.OkGetArt(viewUrl);
                        CrawlPageUtil.videoUrl = CrawlPageUtil.spiderVideoUrl(html);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }



            }
            // 网页加载完毕，隐藏进度条
            progressBar.setVisibility(View.INVISIBLE);
            // 改变标题
            mActivity.setTitle(webView.getTitle());
            // 显示页面标题
            textUrl.setText(webView.getTitle());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String name = textUrl.getText().toString().trim();
                    String address = webView.getUrl();
                    /*添加到本地仓库*/
                    history.addHistory(new historyData(name, address));
                    /*添加到远程仓库*/
                    SharedPreferences sp = mActivity.getSharedPreferences("sp_list", MODE_PRIVATE);
                    String head = sp.getString("token", "");
//                    if (!name.equals("百度一下")) {
//                        httpUtils.putHistory(head, "http://139.196.180.89:8137/api/v1/histories", name, address, new Callback() {
//                            @Override
//                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                                Log.d("his","failure");
//                            }
//
//                            @Override
//                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                                /*得到的服务器返回值具体内容*/
//                                assert response.body() != null;
//                                final String responseData = response.body().string();
//                                mActivity.runOnUiThread(()-> Log.d("his", responseData));
//                                Gson gson = new Gson();
//                                responseData_put responsePut = gson.fromJson(responseData,responseData_put.class);
//                                if(responsePut.getState().getCode() == 0){
//                                    Log.d("his","success");
//                                }else {
//                                    startActivity(new Intent(getActivity(), login.class));
//                                }
//                            }
//                        });
//                    }
                }
            }, 4000);
            if(CrawlPageUtil.currentNews != null) {
                title.setText(CrawlPageUtil.currentNews.getTitle());
                author.setText(CrawlPageUtil.currentNews.getAuthor());
                RecyclerView recyclerView=mActivity.findViewById(R.id.recyclerview);
                LinearLayoutManager manager = new LinearLayoutManager(mActivity);
                recyclerView.setLayoutManager(manager);
                Log.d("MYURL", CrawlPageUtil.currentNews.getAddress());
                newsAdapter newsAdapter = new newsAdapter(CrawlPageUtil.currentNews.getContents());
                newsAdapter.setHasStableIds(true);
                recyclerView.setAdapter(newsAdapter);
                ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                recyclerView.setOnScrollListener(new OnRcvScrollListener());
                recyclerView.setItemViewCacheSize(30);
                illegWebsite.setVisibility(View.GONE);
                searchHis.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                web.setVisibility(View.GONE);
                advisory.setVisibility(View.VISIBLE);

            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            webView.goBack();
        }
    }
    private class MkWebChromeClient extends WebChromeClient {
        private final static int WEB_PROGRESS_MAX = 100;

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            //加载进度条，刷新进度条
            progressBar.setProgress(newProgress);
            if (newProgress > 0) {
                if (newProgress == WEB_PROGRESS_MAX) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            webIcon.setImageBitmap(icon);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mActivity.setTitle(title);
            textUrl.setText(title);


        }

    }


    public static boolean isHttpUrl(String urls) {
        boolean isUrl;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";

        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(urls.trim());
        isUrl = mat.matches();
        return isUrl;
    }

    private static boolean iswwwbutNOHttp(String input) {
        boolean isUrl;
        String regex = "(([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";
        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(input.trim());
        isUrl = mat.matches();
        return isUrl;
    }

    /*颠倒list顺序，用户输入的信息会从上依次往下显示*/
    private void reversedList() {
        data.clear();
        for (int i = temList.size() - 1; i >= 0; i--) {
            data.add(temList.get(i));
        }
    }

    /*解决视频声音问题的方法*/
    @Override
    public void onPause() {
        super.onPause();
        try {
            webView.getClass().getMethod("onPause").invoke(webView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            webView.getClass().getMethod("onResume").invoke(webView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goback) {
            illegWebsite.setVisibility(View.GONE);
            Liner_search.setVisibility(View.VISIBLE);
            web.setVisibility(View.VISIBLE);
        }
        if (id == R.id.btnStart) {
            if (textUrl.hasFocus()) {
                //隐藏软键盘
                if (manager.isActive()) {
                    manager.hideSoftInputFromWindow
                            (textUrl.getApplicationWindowToken(), 0);
                }
                String input = textUrl.getText().toString();
                sc.addRecords(input);
                initList();
                if (!isHttpUrl(input)) {
                    try {
                        input = URLEncoder.encode(input, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    input = "https://www.baidu.com/s?wd=" + input + "&ie=UTF-8";
                }
                if (iswwwbutNOHttp(input)) {
                    try {
                        // URL 编码
                        input = URLEncoder.encode(input, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    input = "https://" + input;
                }
                webView.loadUrl(input);
                textUrl.clearFocus();
            } else {
                webView.reload();
            }
        }
        if (id == R.id.webIcon) {
            if (textUrl.hasFocus()) {
                //隐藏软键盘
                if (manager.isActive()) {
                    manager.hideSoftInputFromWindow
                            (textUrl.getApplicationWindowToken(), 0);
                }
                textUrl.clearFocus();
            }
        }
        if (id == R.id.clear) {
            AlertDialog.Builder clearSure = new AlertDialog.Builder(mActivity);
            clearSure.setPositiveButton("确认",
                    (dialog, which) -> {
                        sc.deleteAllRecords();
                        SearchFragment.this.initList();
                    });

            clearSure.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
            clearSure.setTitle("提示");
            clearSure.setMessage("您确认清空搜索历史吗");
            clearSure.show();
        }
    }

    public void setWebView(MingWebView webView) {
        this.webView = webView;
    }
}
