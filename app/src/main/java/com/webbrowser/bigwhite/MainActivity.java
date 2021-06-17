package com.webbrowser.bigwhite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

//import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.webbrowser.bigwhite.Model.SQLite.bookmarkDao;
import com.webbrowser.bigwhite.Model.data.historyData;
import com.webbrowser.bigwhite.View.adapter.SectionsPageAdapter;
import com.webbrowser.bigwhite.View.adapter.bookmarkFileAdapter;
import com.webbrowser.bigwhite.View.fragment.SearchFragment;
import com.webbrowser.bigwhite.activity.BaseActivity;
import com.webbrowser.bigwhite.utils.popWindows.myPopWin;
import com.webbrowser.bigwhite.activity.bookmark;
import com.webbrowser.bigwhite.activity.chooseLoginRegister;
import com.webbrowser.bigwhite.activity.history;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    /*log的标签*/
//    private static final String TAG = "main";
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
    private ViewPager viewPager;
    public List<Fragment> fragments;
    private SearchFragment sc;

    /*点击返回键调用*/
    private long exitTime=0;
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
        initView();
    }


    private void initView(){
        /*添加标签文件夹*/
        TextView add_file = findViewById(R.id.add_file);
        list_file = new ArrayList<>();
        bookmarkDao = new bookmarkDao(MainActivity.this);
//        bookmarkDao.addBookmark(new historyData("百度","www.baidu.com"),"我的标签");
        list_file = bookmarkDao.queryFilename();
        Log.d("TAG", String.valueOf(list_file));




        /*添加标签*/
        select_list = findViewById(R.id.select_list);
        file_list = findViewById(R.id.file_list);
        viewPager = findViewById(R.id.viewPager);

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

        add_file.setOnClickListener(this);
    }

    private void initViewPager() {
        /*配置viewPager*/
        /*可以有多少个搜索fragment*/
//        viewPager.setOffscreenPageLimit(10);
        fragments = new ArrayList<>();
        fragments.add(new SearchFragment());

        viewPager.setAdapter
                (new SectionsPageAdapter(getSupportFragmentManager(),fragments));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.backLeft){
            sc = (SearchFragment) fragments.get(viewPager.getCurrentItem());
            sc.getWebView().goBack();
        }else if(id == R.id.backRight){
            sc = (SearchFragment) fragments.get(viewPager.getCurrentItem());
            sc.getWebView().goForward();
        }else if(id == R.id.home){
            Intent intent=new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if(id == R.id.more_windows){
            Toast.makeText(mContext, "功能开发中", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.my){
            showPopFormBottom();
        }else if(id ==R.id.add_file){
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
        }
    }






    @Override
    public void onBackPressed(){
       //返回上一页
        sc = (SearchFragment) fragments.get(viewPager.getCurrentItem());
        if(sc.getWebView().canGoBack()){
            sc.getWebView().goBack();
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

    /*my弹窗*/
    public void showPopFormBottom(){
        myPopWin = new myPopWin(this,onClickListener);

        myPopWin.showAtLocation(findViewById(R.id.my), Gravity.CENTER,0,0);
    }
    public void initFile(){
        bookmarkFileAdapter mainFile = new bookmarkFileAdapter(MainActivity.this,list_file);
        file_list.setAdapter(mainFile);

        /*绑定item的点击事件*/
        file_list.setOnItemClickListener((parent, view, position, id) -> {
            String fileName = list_file.get(position);
            String name = sc.getWebView().getTitle().trim();
            String url =  sc.getWebView().getUrl();
            bookmarkDao.addBookmark(new historyData(name,url),fileName);
            select_list.setVisibility(View.GONE);
        });
    }




    private final View.OnClickListener onClickListener = v -> {
        int id = v.getId();
        sc = (SearchFragment) fragments.get(viewPager.getCurrentItem());
        if(id == R.id.login){
            myPopWin.dismiss();
            startActivity(new Intent(MainActivity.this, chooseLoginRegister.class));
        }else if(id == R.id.add_bookmark){
            myPopWin.dismiss();
            select_list.setVisibility(View.VISIBLE);
            initFile();
        }else if(id == R.id.bookmark){
            myPopWin.dismiss();
            startActivity(new Intent(MainActivity.this, bookmark.class));
        }else if(id == R.id.history){
            myPopWin.dismiss();
            startActivity(new Intent(MainActivity.this, history.class));
        }else if(id == R.id.exit){
            Toast.makeText(mContext, "退出登录功能开发中", Toast.LENGTH_SHORT).show();
        }
    };


    public void list_back(View view) {
        select_list.setVisibility(View.GONE);
    }
}