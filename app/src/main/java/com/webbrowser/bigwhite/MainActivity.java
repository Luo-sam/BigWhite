package com.webbrowser.bigwhite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dueeeke.videoplayer.player.VideoView;
import com.google.gson.Gson;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.webbrowser.bigwhite.Model.SQLite.WebPageHelper;
import com.webbrowser.bigwhite.Model.SQLite.bookmarkDao;
import com.webbrowser.bigwhite.Model.data.historyData;
import com.webbrowser.bigwhite.Model.data.responseData_put;
import com.webbrowser.bigwhite.View.adapter.SectionsPageAdapter;
import com.webbrowser.bigwhite.View.adapter.bookmarkFileAdapter;
import com.webbrowser.bigwhite.View.fragment.SearchFragment;
import com.webbrowser.bigwhite.View.myView.MyViewPager;
import com.webbrowser.bigwhite.activity.BaseActivity;
import com.webbrowser.bigwhite.activity.bookmark;
import com.webbrowser.bigwhite.activity.chooseLoginRegister;
import com.webbrowser.bigwhite.activity.history;
import com.webbrowser.bigwhite.activity.login;
import com.webbrowser.bigwhite.activity.personalCenterActivity;
import com.webbrowser.bigwhite.utils.CrawlPageUtil;
import com.webbrowser.bigwhite.utils.httpUtils;
import com.webbrowser.bigwhite.utils.popWindows.myPopWin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//import com.webbrowser.bigwhite.utils.WebPageHelper;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    /*log的标签*/
    /*保存Token值*/
    private String Token;

    private Context mContext;
    /*个人栏弹窗*/
    private myPopWin myPopWin;

    /*添加标签页面*/
    private LinearLayout select_list;
    private ListView file_list;
    private bookmarkDao bookmarkDao;
    private List<String> list_file;
    /*搜索栏*/
    private MyViewPager viewPager;
    public List<Fragment> fragments;
    private SearchFragment sc;
    private  LinearLayout windows,videoView;


    /*点击返回键调用*/
    private long exitTime = 0;
    private static final int PRESS_BACK_EXIT_GAP = 2000;

    /*主页初始layoutParams*/
    ViewGroup.LayoutParams mylayoutParams;

    public LinearLayout getWindows() {
        return windows;
    }

    /*onCreate方法*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止底部导航栏上移
        getWindow().setSoftInputMode
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        list_file = bookmarkDao.queryFilename();
    }

    private void initView() {

        /*添加标签文件夹*/
        TextView add_file = findViewById(R.id.add_file);
        list_file = new ArrayList<>();
        bookmarkDao = new bookmarkDao(MainActivity.this);
        list_file = bookmarkDao.queryFilename();
        windows=findViewById(R.id.windows);

        /*添加标签*/
        select_list = findViewById(R.id.select_list);
        file_list = findViewById(R.id.file_list);
        viewPager = findViewById(R.id.viewPager);
        /*新增窗口*/
        ImageView addition = findViewById(R.id.add_win);

        /*获取token*/
        Token = getStringFromSp("token");
        /*对viewPager进行初始化*/
        initViewPager();
        /*对mContext初始化*/
        mContext = MainActivity.this;
        /*底部导航栏的id获取*/
        ImageView backleft = findViewById(R.id.backLeft);
        ImageView backright = findViewById(R.id.backRight);
        ImageView home = findViewById(R.id.home);
        ImageView morewindows = findViewById(R.id.more_windows);
        ImageView my = findViewById(R.id.my);
        /*绑定点击事件*/
        backleft.setOnClickListener(this);
        backright.setOnClickListener(this);
        home.setOnClickListener(this);
        morewindows.setOnClickListener(this);
        my.setOnClickListener(this);
        addition.setOnClickListener(this);
        add_file.setOnClickListener(this);
    }

    private void initViewPager() {
        /*配置viewPager*/
        fragments = new ArrayList<>();
        Fragment fragment = new SearchFragment();
        this.fragments.add(fragment);
        WebPageHelper.webpagelist = this.fragments;




//        ((ViewGroup) viewPager.getParent()).setOnTouchListener(new View.OnTouchListener() {
//            protected float point_x, point_y; //手指按下的位置
//            private int left, right, bottom;
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        WebViewFragment webViewFragment;
//                        point_x = event.getRawX();
//                        point_y = event.getRawY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float mov_x = event.getRawX() - point_x;
//                        float mov_y = event.getRawY() - point_y;
//                        Log.d("trr", "mov_y" + mov_y);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        break;
//
//                }
//                return viewPager.dispatchTouchEvent(event);
//            }
//        });

        viewPager.setOffscreenPageLimit(8);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d("MotionEvent_pageScro", "" + position);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        viewPager.setAdapter(new SectionsPageAdapter(getSupportFragmentManager(), this.fragments));
        //保存主页初始宽度
        mylayoutParams = viewPager.getLayoutParams();
        /*页面初始间距*/
        int pageMargin = viewPager.getPageMargin();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.backLeft) {
            sc = (SearchFragment) fragments.get(viewPager.getCurrentItem());
            if (sc.getAdvisory().getVisibility() == View.VISIBLE) {
                windows.setVisibility(View.VISIBLE);
                sc.getAdvisory().setVisibility(View.GONE);
                sc.getLiner_search().setVisibility(View.VISIBLE);
                sc.getWeb().setVisibility(View.VISIBLE);
            }
            if (sc.getIllegWebsite().getVisibility() == View.VISIBLE) {
                sc.getWebView().goBack();
            }
            if (sc.getSearchHis().getVisibility() == View.VISIBLE) {
                sc.getSearchHis().setVisibility(View.GONE);
                sc.getTextUrl().clearFocus();
            }
            sc.getWebView().goBack();
        } else if (id == R.id.backRight) {
            sc = (SearchFragment) fragments.get(viewPager.getCurrentItem());
            sc.getWebView().goForward();
        } else if (id == R.id.home) {
            CrawlPageUtil.currentNews = null;
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.more_windows) {
            //加载多窗口视图
            initMultiWindowsView();
        } else if (id == R.id.my) {
            showPopFormBottom();
        } else if (id == R.id.add_file) {
            final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(MainActivity.this).builder()
                    .setTitle("请输入")
                    .setEditText("");
            myAlertInputDialog.setPositiveButton("确认", v1 -> {
                showToast(myAlertInputDialog.getResult());
                list_file.add(myAlertInputDialog.getResult());
                initFile();
                myAlertInputDialog.dismiss();
            }).setNegativeButton("取消", v2 -> {
                showToast("取消");
                myAlertInputDialog.dismiss();
            });
            myAlertInputDialog.show();
        } else if (id == R.id.add_win) {
            addWin();
        }
    }

    @Override
    public void onBackPressed() {
        //返回上一页
        sc = (SearchFragment) fragments.get(viewPager.getCurrentItem());
        if (sc.getAdvisory().getVisibility() == View.VISIBLE) {
            windows.setVisibility(View.VISIBLE);
            sc.getLiner_search().setVisibility(View.VISIBLE);
            sc.getWeb().setVisibility(View.VISIBLE);
        }

        if (sc.getIllegWebsite().getVisibility() == View.VISIBLE) {
            sc.getIllegWebsite().setVisibility(View.GONE);
            sc.getLiner_search().setVisibility(View.VISIBLE);
            sc.getWeb().setVisibility(View.VISIBLE);
        }
        if (sc.getSearchHis().getVisibility() == View.VISIBLE) {
            sc.getSearchHis().setVisibility(View.GONE);
            sc.getTextUrl().clearFocus();
        }
        if (sc.getWebView().canGoBack()) {
            sc.getWebView().goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > PRESS_BACK_EXIT_GAP) {
                //点击两次就退出
                Toast.makeText(mContext, "再按一次退出浏览器", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    /*my弹窗*/
    public void showPopFormBottom() {
        myPopWin = new myPopWin(this, onClickListener);
        myPopWin.showAtLocation(findViewById(R.id.my), Gravity.CENTER, 0, 0);
    }

    public void initFile() {
        bookmarkFileAdapter mainFile = new bookmarkFileAdapter(MainActivity.this, list_file);
        file_list.setAdapter(mainFile);

        /*绑定item的点击事件*/
        file_list.setOnItemClickListener((parent, view, position, id) -> {
            String fileName = list_file.get(position);
            String name = sc.getWebView().getTitle().trim();
            String url = sc.getWebView().getUrl();
            SharedPreferences sp = getSharedPreferences("sp_list", MODE_PRIVATE);
            String head = sp.getString("token", "");

            bookmarkDao.addBookmark(new historyData(name, url), fileName);
            httpUtils.putBookMark(head, "http://139.196.180.89:8137/api/v1/collections", fileName, name, url, new Callback() {
                @Override
                public void onFailure(@NonNull Call call , @NonNull IOException e) {
                    runOnUiThread(() -> showToast("上传标签网络错误"));
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    /*得到的服务器返回值具体内容*/
                    assert response.body() != null;
                    final String responseData = response.body().string();
                    Gson gson = new Gson();
                    responseData_put responsePut = gson.fromJson(responseData, responseData_put.class);
                    runOnUiThread(() -> {
                        if (responsePut.getState().getCode() == 0) {
                            showToast("添加到后端成功");
                        } else {
                            startActivity(new Intent(MainActivity.this, login.class));
                        }
                    });
                }
            });

            select_list.setVisibility(View.GONE);
            showToast("添加成功");
        });
    }


    private final View.OnClickListener onClickListener = v -> {
        int id = v.getId();
        sc = (SearchFragment) fragments.get(viewPager.getCurrentItem());
        if (id == R.id.login) {
            myPopWin.dismiss();
            startActivity(new Intent(MainActivity.this, chooseLoginRegister.class));
        } else if (id == R.id.add_bookmark) {
            myPopWin.dismiss();
            select_list.setVisibility(View.VISIBLE);
            initFile();
        } else if (id == R.id.bookmark) {
            myPopWin.dismiss();
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, bookmark.class);
            startActivityForResult(intent, 123);
        } else if (id == R.id.history) {
            myPopWin.dismiss();
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, history.class);
            startActivityForResult(intent, 456);
        } else if (id == R.id.exit) {
            saveToSp("token", "");
            myPopWin.change();
        }else if(id==R.id.person){
            startActivity(new Intent(MainActivity.this, personalCenterActivity.class));
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            sc = (SearchFragment) fragments.get(viewPager.getCurrentItem());
            sc.getWebView().loadUrl(data != null ? data.getExtras().getString("address") : "www.baidu.com");
        }
    }

    public void list_back(View view) {
        select_list.setVisibility(View.GONE);

    }

    private void initMultiWindowsView() {


        this.fragments = WebPageHelper.webpagelist;

        for (Fragment webViewFragment : this.fragments) {
            WebView webView = ((SearchFragment) webViewFragment).getWebView();
            webView.onPause();
            webView.pauseTimers();
        }


        LinearLayout layout = (LinearLayout) findViewById(R.id.navigation_bar);

        LinearLayout win_add = (LinearLayout) findViewById(R.id.multywin);
        //隐藏底部导航栏
        layout.setVisibility(View.GONE);

        scaleWindow();
        //设置背景颜色为灰黑色
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_activity);
        mainLayout.setBackgroundColor(0xFF292727);

        //设置页间距
        viewPager.setPageMargin(40);
//        //设置页面左右间距
//        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
//        WindowManager wm = (WindowManager) this
//                .getSystemService(Context.WINDOW_SERVICE);
//        int width = wm.getDefaultDisplay().getWidth();
//        layoutParams.width = width - 120;
//        viewPager.setLayoutParams(layoutParams);

        viewPager.setClipChildren(false);

        viewPager.setFullScreen(false);

        //显示添加按钮
        win_add.setVisibility(View.VISIBLE);
    }

    private void scaleWindow() {

        /** 设置缩放动画 */
        final ScaleAnimation animation = new ScaleAnimation(1f, 0.8f, 1f, 0.75f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);// 从相对于自身0.5倍的位置开始缩放，也就是从控件的位置缩放
        animation.setDuration(200);//设置动画持续时间

        /** 常用方法 */
        //animation.setRepeatCount(int repeatCount);//设置重复次数
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        //animation.setStartOffset(long startOffset);//执行前的等待时间

        viewPager.setAnimation(animation);
        /** 开始动画 */
        animation.startNow();
    }

    public void addWin() {

        for (Fragment webViewFragment : this.fragments) {
            ((SearchFragment) webViewFragment).getWebView().onResume();     //由于调用onResume会导致所有WebView都处于活动状态，而onPause只是针对单个
            ((SearchFragment) webViewFragment).getWebView().resumeTimers();
        }


        Fragment fragment = new SearchFragment();
        fragments.add(fragment);
        int currentItem = fragments.size() - 1;

        //隐藏添加按钮
        LinearLayout win_add = (LinearLayout) findViewById(R.id.multywin);
        win_add.setVisibility(View.GONE);
        //显示底部导航栏
        LinearLayout layout = (LinearLayout) findViewById(R.id.navigation_bar);
        layout.setVisibility(View.VISIBLE);
        viewPager.getAdapter().notifyDataSetChanged();
        viewPager.setCurrentItem(currentItem, false);
        enlargeWindow();
        //设置背景颜色为白色
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_activity);
        mainLayout.setBackgroundColor(0xF4F2F2);

//        //设置页间距
//        viewPager.setPageMargin(pageMargin);
//        Log.d("margin", ""+pageMargin);
////        //设置页面左右间距
//        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
//        WindowManager wm = (WindowManager) this
//                .getSystemService(Context.WINDOW_SERVICE);
//        layoutParams.width = wm.getDefaultDisplay().getWidth();
//        viewPager.setLayoutParams(layoutParams);


        viewPager.setClipChildren(true);
        viewPager.setFullScreen(true);
    }

    private void enlargeWindow() {

        /** 设置缩放动画 */
        final ScaleAnimation animation = new ScaleAnimation(0.8f, 1f, 0.75f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);// 从相对于自身0.5倍的位置开始缩放，也就是从控件的位置缩放
        animation.setDuration(200);//设置动画持续时间

        /** 常用方法 */
        //animation.setRepeatCount(int repeatCount);//设置重复次数
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        //animation.setStartOffset(long startOffset);//执行前的等待时间

        viewPager.setAnimation(animation);
        /** 开始动画 */
        animation.startNow();
    }

    public void toWindow() {
        for (Fragment webViewFragment : this.fragments) {
            ((SearchFragment) webViewFragment).getWebView().onResume();     //由于调用onResume会导致所有WebView都处于活动状态，而onPause只是针对单个
            ((SearchFragment) webViewFragment).getWebView().resumeTimers();
        }
        //隐藏添加按钮
        LinearLayout win_add = (LinearLayout) findViewById(R.id.multywin);
        win_add.setVisibility(View.GONE);
        //显示底部导航栏
        LinearLayout layout = (LinearLayout) findViewById(R.id.navigation_bar);
        layout.setVisibility(View.VISIBLE);

        viewPager.getAdapter().notifyDataSetChanged();
        enlargeWindow();
        //设置背景颜色为白色
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_activity);
        mainLayout.setBackgroundColor(0xF4F2F2);

        viewPager.setClipChildren(true);
        viewPager.setFullScreen(true);
    }

}