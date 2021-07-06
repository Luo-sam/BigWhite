package com.webbrowser.bigwhite.View.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.webbrowser.bigwhite.MainActivity;
import com.webbrowser.bigwhite.Model.SQLite.RecordsDao;
import com.webbrowser.bigwhite.Model.SQLite.historyDao;
import com.webbrowser.bigwhite.Model.data.NewsData;
import com.webbrowser.bigwhite.Model.data.VideoData;
import com.webbrowser.bigwhite.Model.data.historyResponse;
import com.webbrowser.bigwhite.Model.data.ilLegWebsite;
import com.webbrowser.bigwhite.Model.data.responseData_put;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.adapter.newsAdapter;
import com.webbrowser.bigwhite.View.adapter.searchHistoryAdapter;
import com.webbrowser.bigwhite.View.myView.MingWebView;
import com.webbrowser.bigwhite.utils.CrawlPageUtil;
import com.webbrowser.bigwhite.utils.OkHttpUtil;
import com.webbrowser.bigwhite.utils.httpUtils;
import com.webbrowser.bigwhite.utils.method.OnRcvScrollListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


public class SearchFragment extends BaseFragment implements View.OnKeyListener, View.OnClickListener, View.OnFocusChangeListener {
    @SuppressLint("StaticFieldLeak")
    private MingWebView webView;
    private View view;
    private InputMethodManager manager;
    private FrameLayout web;
    private LinearLayout searchHis;
    private List<String> data, temList;
    private RecordsDao sc;
    private ProgressBar progressBar;
    private EditText textUrl;
    private ImageView webIcon, btnStart;
    private LinearLayout Liner_search, illegWebsite, linearLayout;
    private TextView title, video, author;
    /*上传用的token*/
    private String token;
    /*历史记录*/
    private historyDao history;
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";

    private Activity mActivity;
    private LinearLayout advisory;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.web_search, container, false);
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
    @SuppressLint("CutPasteId")
    private void initView(View view) {
        mainActivity=(MainActivity)getActivity();
        video = view.findViewById(R.id.video);
        linearLayout = view.findViewById(R.id.linearLayout);
        advisory = view.findViewById(R.id.advisory);
        advisory.setVisibility(View.GONE);
        illegWebsite = view.findViewById(R.id.illeg);
        illegWebsite.setVisibility(View.GONE);
        TextView textView = view.findViewById(R.id.goback);

        /*对两个页面的初始化*/
        Liner_search = view.findViewById(R.id.linearLayout);
        web = view.findViewById(R.id.web);
        searchHis = view.findViewById(R.id.search_his);
        title = view.findViewById(R.id.title);
        author = view.findViewById(R.id.author);
        recyclerView = view.findViewById(R.id.recyclerview);



        history = new historyDao(mActivity);
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
        textUrl.setOnFocusChangeListener(this);
        textUrl.setOnKeyListener(this);
    }

    /*初始化webView*/
    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb() {
        webView.setWebViewClient(new MkWebViewClient());
        webView.setWebChromeClient(new MkWebChromeClient());
        webView.loadUrl(getResources().getString(R.string.home_url));
        WebSettings settings = webView.getSettings();
        initSetting(settings);
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
            String[] data;
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
            // 网页加载完毕，隐藏进度条
            progressBar.setVisibility(View.INVISIBLE);
            // 改变标题
            mActivity.setTitle(webView.getTitle());
            // 显示页面标题
            textUrl.setText(webView.getTitle());

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                String name = textUrl.getText().toString().trim();
                showToast(name);
                String address = webView.getUrl();
                historyResponse.DataBean his = new historyResponse.DataBean(name, address);
                /*添加到远程仓库*/
                SharedPreferences sp = mActivity.getSharedPreferences("sp_list", MODE_PRIVATE);
                String head = sp.getString("token", "");
                if (!name.equals("百度一下")) {
                    showToast("开始上传");
                    if (history.isHasRecord(his)) {
                        history.clearThisMess(his);
                    }

                    /*添加到本地仓库*/
                    history.addHistory(his);

                    httpUtils.putHistory(head, "http://139.196.180.89:8137/api/v1/histories", name, address, new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            mActivity.runOnUiThread(() -> showToast("上传历史记录网络错误"));
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            /*得到的服务器返回值具体内容*/
                            assert response.body() != null;
                            final String responseData = response.body().string();
                            Gson gson = new Gson();
                            responseData_put responsePut = gson.fromJson(responseData, responseData_put.class);
                            mActivity.runOnUiThread(() -> {
                                if (responsePut.getState().getCode() == 0) {
                                    showToast("上传历史记录成功");
                                } else {
                                    showToast("上传历史记录失败");
                                }
                            });
                        }
                    });

                }
            }, 2000);

            /**
             *@Author luo
             *@Time 2021/7/5 15:12
             *@Description 获取data信息
             */

            if (view.getUrl().contains("https://www.baidu.com/#iact=wiseindex%")) {
                String viewUrl = view.getUrl();
                String pattern = "news_(\\d+)%";

                // 创建 Pattern 对象
                Pattern r = Pattern.compile(pattern);
                // 现在创建 matcher 对象
                Matcher m = r.matcher(viewUrl);
                if (m.find()) {
                    String id = m.group(1);
                    viewUrl = "https://mbd.baidu.com/newspage/data/landingpage?s_type=news&dsp=wise&context=%7B%22nid%22%3A%22news_" +
                            id +
                            "%22%7D&pageType=1&n_type=1&p_from=-1&quot";

                    if (!CrawlPageUtil.newsMap.containsKey(viewUrl)) {
                        String html = null;
                        try {
                            html = OkHttpUtil.OkGetArt(viewUrl);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        NewsData newsData = CrawlPageUtil.spiderNewsData(html, viewUrl);
                        try {
                            CrawlPageUtil.newsMap.put(viewUrl, (NewsData) newsData.clone());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        CrawlPageUtil.currentNews = newsData;
                    } else
                        CrawlPageUtil.currentNews = CrawlPageUtil.newsMap.get(viewUrl);
                } else {
                    // 默认是视频
                    String html = null;
                    try {
                        viewUrl = OkHttpUtil.getRealVideoUrl(viewUrl);
                        html = OkHttpUtil.OkGetArt(viewUrl);
                        CrawlPageUtil.videoData = CrawlPageUtil.spiderVideoUrl(html);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

            /**
             *@Author luo
             *@Time 2021/7/5 15:11
             *@Description 处理资讯界面的显示
             */
            NewsData news = CrawlPageUtil.currentNews;
            VideoData videa = CrawlPageUtil.videoData;
//            RecyclerView recyclerView = mActivity.findViewById(R.id.recyclerview);
            newsAdapter newsAdapter = null;
            if (news != null || videa != null) {
                if (news != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    video.setVisibility(View.GONE);
                    title.setText(news.getTitle());
                    author.setText(news.getAuthor());
                    LinearLayoutManager manager = new LinearLayoutManager(mActivity);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);

                    recyclerView.setLayoutManager(manager);
                    newsAdapter = new newsAdapter(news.getContents(), mActivity);
                    /*避免数据改变的时候重新加载*/
                    newsAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(newsAdapter);
                    /*避免闪烁*/
                    recyclerView.setItemAnimator(null);
                    /*设置动态更新recyclerView*/
                    recyclerView.setOnScrollListener(new OnRcvScrollListener());
                    recyclerView.setItemViewCacheSize(50);
                    mainActivity.getWindows().setVisibility(View.GONE);
                    illegWebsite.setVisibility(View.GONE);
                    searchHis.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    web.setVisibility(View.GONE);
                    advisory.setVisibility(View.VISIBLE);
                }
                if (videa != null) {
                    if (recyclerView.getAdapter() != null) {
                        recyclerView.setVisibility(View.GONE);
                        video.setVisibility(View.VISIBLE);
                    }
                    title.setText(CrawlPageUtil.videoData.getTitle());
                    author.setText(CrawlPageUtil.videoData.getAuthor());
                    video.setText(CrawlPageUtil.videoData.getVideoUrl());
                    mainActivity.getWindows().setVisibility(View.GONE);
                    illegWebsite.setVisibility(View.GONE);
                    searchHis.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    web.setVisibility(View.GONE);
                    advisory.setVisibility(View.VISIBLE);
                }

            }
            CrawlPageUtil.currentNews = null;
            CrawlPageUtil.videoData = null;
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

    /*颠倒list顺序，用户输入的信息会从上依次往下显示*/
    private void reversedList() {
        data.clear();
        for (int i = temList.size() - 1; i >= 0; i--) {
            data.add(temList.get(i));
        }
    }

    public LinearLayout getAdvisory() {
        return advisory;
    }

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

    //返回webView
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

    /**
     * @Author luo
     * @Time 2021/7/5 15:10
     * @Description activity返回值的处理
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            webView.goBack();
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
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent event) {
        if (i == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            //执行搜索
            btnStart.callOnClick();
            textUrl.clearFocus();
        }
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        int id = view.getId();
        if (id == R.id.textUrl) {
            if (textUrl.hasFocus()) {
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

}
