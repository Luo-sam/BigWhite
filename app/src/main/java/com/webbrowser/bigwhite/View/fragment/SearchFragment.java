package com.webbrowser.bigwhite.View.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.VideoView;
import com.webbrowser.bigwhite.MainActivity;
import com.webbrowser.bigwhite.Model.SQLite.RecordsDao;
import com.webbrowser.bigwhite.Model.SQLite.historyDao;
import com.webbrowser.bigwhite.Model.data.NewsData;
import com.webbrowser.bigwhite.Model.data.VideoData;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.adapter.newsAdapter;
import com.webbrowser.bigwhite.View.adapter.searchHistoryAdapter;
import com.webbrowser.bigwhite.View.myView.MingWebView;
import com.webbrowser.bigwhite.utils.CrawlPageUtil;
import com.webbrowser.bigwhite.utils.method.OnRcvScrollListener;
import com.webbrowser.bigwhite.utils.method.infoIntercept;
import com.webbrowser.bigwhite.utils.method.infoRead;
import com.webbrowser.bigwhite.utils.method.saveInfoToThis;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends BaseFragment implements View.OnKeyListener, View.OnClickListener, View.OnFocusChangeListener {
    @SuppressLint("StaticFieldLeak")
    private MingWebView webView;
    private View view;
    private InputMethodManager manager;
    private FrameLayout web;
    private List<String> data, temList;
    private RecordsDao sc;
    private ProgressBar progressBar;
    private EditText textUrl;
    private ImageView webIcon, btnStart;
    private LinearLayout Liner_search, illegWebsite, linearLayout, searchHis;
    private TextView title, author;
    private VideoView videoView;
    private StandardVideoController standardVideoController;//???????????????
    /*????????????*/
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


    /*?????????????????????*/
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

    /*??????????????????view*/
    @SuppressLint("CutPasteId")
    private void initView(View view) {
        videoView = view.findViewById(R.id.player);
        mainActivity = (MainActivity) getActivity();
        linearLayout = view.findViewById(R.id.linearLayout);
        advisory = view.findViewById(R.id.advisory);
        advisory.setVisibility(View.GONE);
        illegWebsite = view.findViewById(R.id.illeg);
        illegWebsite.setVisibility(View.GONE);
        TextView textView = view.findViewById(R.id.goback);
        /*???????????????????????????*/
        Liner_search = view.findViewById(R.id.linearLayout);
        web = view.findViewById(R.id.web);
        searchHis = view.findViewById(R.id.search_his);
        title = view.findViewById(R.id.title);
        author = view.findViewById(R.id.author);
        recyclerView = view.findViewById(R.id.recyclerview);
        history = new historyDao(mActivity);
        /*??????????????????*/
        manager = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        /*??????ID?????????*/
        webView = view.findViewById(R.id.webView);
        btnStart = view.findViewById(R.id.btnStart);
        textUrl = view.findViewById(R.id.textUrl);
        progressBar = view.findViewById(R.id.progressBar);
        webIcon = view.findViewById(R.id.webIcon);
        TextView clear = view.findViewById(R.id.clear);
        /*??????????????????*/
        btnStart.setOnClickListener(this);
        /*??????webIcon?????????????????????*/
        webIcon.setOnClickListener(this);
        /*??????????????????????????????*/
        clear.setOnClickListener(this);
        textView.setOnClickListener(this);
        textUrl.setOnFocusChangeListener(this);
        textUrl.setOnKeyListener(this);
    }

    /*?????????webView*/
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
            //?????????webView?????????????????????????????????????????????????????????????????????????????????
            if (url == null) {
                return true;
            }
            if (url.startsWith(HTTP) || url.startsWith(HTTPS)) {
                view.loadUrl(url);
                return true;
            }
            //??????????????????????????????crash (??????????????????????????????/?????????scheme?????????url???APP, ?????????crash)
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
            /*????????????*/
            infoIntercept.getData(mActivity,view,url,Liner_search,web,searchHis,illegWebsite);

            super.onPageStarted(view, url, favicon);
            // ????????????????????????????????????
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
            // ??????????????????
            textUrl.setText("?????????...");
            // ????????????????????????
            webIcon.setImageResource(R.drawable.internet);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // ????????????????????????????????????
            progressBar.setVisibility(View.INVISIBLE);
            // ????????????
            mActivity.setTitle(webView.getTitle());
            // ??????????????????
            textUrl.setText(webView.getTitle());
            /*???????????????????????????????????????*/
            saveInfoToThis.saveHistory(getContext(), textUrl, webView, mActivity, history);
            /*??????????????????*/
            infoRead.getData(view);
            /**
             *@Author luo
             *@Time 2021/7/5 15:11
             *@Description ???????????????????????????
             */
            NewsData news = CrawlPageUtil.currentNews;
            VideoData videa = CrawlPageUtil.videoData;
            newsAdapter newsAdapter = null;
            if (news != null || videa != null) {
                if (news != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.GONE);
                    title.setText(news.getTitle());
                    author.setText(news.getAuthor());
                    LinearLayoutManager manager = new LinearLayoutManager(mActivity);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);

                    recyclerView.setLayoutManager(manager);
                    newsAdapter = new newsAdapter(news.getContents(), mActivity);
                    /*???????????????????????????????????????*/
                    newsAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(newsAdapter);
                    /*????????????*/
                    recyclerView.setItemAnimator(null);
                    /*??????????????????recyclerView*/
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
                        videoView.setVisibility(View.VISIBLE);
                    }
                    title.setText(CrawlPageUtil.videoData.getTitle());
                    author.setText(CrawlPageUtil.videoData.getAuthor());

                    standardVideoController = new StandardVideoController(getContext());
                    standardVideoController.addDefaultControlComponent(CrawlPageUtil.videoData.getTitle(), false);

                    videoView.setVideoController(null);
                    videoView.release();

                    videoView.setVideoController(standardVideoController);
                    videoView.setUrl(CrawlPageUtil.videoData.getVideoUrl());

                    //??????IjkPlayer??????
                    videoView.setPlayerFactory(IjkPlayerFactory.create());
                    videoView.start();

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
            //?????????????????????????????????
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


    /*??????list?????????????????????????????????????????????????????????*/
    private void reversedList() {
        data.clear();
        for (int i = temList.size() - 1; i >= 0; i--) {
            data.add(temList.get(i));
        }
    }

    public LinearLayout getAdvisory() {
        return advisory;
    }

    //????????????????????????
    public LinearLayout getIllegWebsite() {
        return illegWebsite;
    }

    public LinearLayout getSearchHis() {
        return searchHis;
    }

    public EditText getTextUrl() {
        return textUrl;
    }

    //??????webView
    public FrameLayout getWeb() {
        return web;
    }

    //???????????????

    public LinearLayout getLiner_search() {
        return Liner_search;
    }

    public WebView getWebView() {
        return webView;
    }

    public VideoView getVideoView() {
        return videoView;
    }

    /**
     * @Author luo
     * @Time 2021/7/5 15:10
     * @Description activity??????????????????
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            webView.goBack();
        }
    }

    /*?????????????????????????????????*/
    @Override
    public void onPause() {
        super.onPause();
        try {
            webView.getClass().getMethod("onPause").invoke(webView);
            if (videoView != null) {
                videoView.pause();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            webView.getClass().getMethod("onResume").invoke(webView);
            if (videoView != null) {
                videoView.resume();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.release();
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
            //????????????
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
                //????????????????????????
                textUrl.setText(webView.getUrl());
                //??????back?????????
                webIcon.setImageResource(R.drawable.left);
                //??????????????????
                btnStart.setImageResource(R.drawable.search);
                /*??????????????????????????????????????????????????????layout????????????*/
                web.setVisibility(View.GONE);
                searchHis.setVisibility(View.VISIBLE);

            } else {
                //???????????????
                textUrl.setText(webView.getTitle());
                //??????????????????
                webIcon.setImageBitmap(webView.getFavicon());
                //??????????????????
                btnStart.setImageResource(R.drawable.refresh);
                /*??????????????????????????????????????????*/
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
            webView.goBack();
        }
        if (id == R.id.btnStart) {
            if (textUrl.hasFocus()) {
                //???????????????
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
                        // URL ??????
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
                //???????????????
                if (manager.isActive()) {
                    manager.hideSoftInputFromWindow
                            (textUrl.getApplicationWindowToken(), 0);
                }
                textUrl.clearFocus();
            }
        }
        if (id == R.id.clear) {
            AlertDialog.Builder clearSure = new AlertDialog.Builder(mActivity);
            clearSure.setPositiveButton("??????",
                    (dialog, which) -> {
                        sc.deleteAllRecords();
                        SearchFragment.this.initList();
                    });

            clearSure.setNegativeButton("??????", (dialog, which) -> dialog.dismiss());
            clearSure.setTitle("??????");
            clearSure.setMessage("??????????????????????????????");
            clearSure.show();
        }
    }

}
