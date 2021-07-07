package com.webbrowser.bigwhite.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.hb.dialog.myDialog.ActionSheetDialog;
import com.webbrowser.bigwhite.Model.SQLite.bookmarkDao;
import com.webbrowser.bigwhite.Model.data.bookmarkResponse;
import com.webbrowser.bigwhite.Model.data.simpleResponse;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.adapter.bookmarkAdapter;
import com.webbrowser.bigwhite.View.adapter.bookmarkFileAdapter;
import com.webbrowser.bigwhite.utils.httpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class bookmark extends BaseActivity {
    private List<bookmarkResponse.DataBean> data;
    private List<bookmarkResponse.DataBean> temList;
    private List<String> list_file;
    private bookmarkDao bookmark;

    private String fileName;

    private LinearLayout files;
    private LinearLayout bookmark_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);
        files = findViewById(R.id.file_layout);
        bookmark_list = findViewById(R.id.bookmark_layout);
        bookmark_list.setVisibility(View.GONE);
        bookmark = new bookmarkDao(this);
        list_file = new ArrayList<>();
        initFile();
    }

    public void initFile(){
        /*初始化list中history的属性*/
        list_file = bookmark.queryFilename();
        bookmarkFileAdapter myFilesAdapter = new bookmarkFileAdapter(bookmark.this,list_file);
        ListView fileList = findViewById(R.id.bookmark_files_list);
        fileList.setAdapter(myFilesAdapter);

        fileList.setOnItemClickListener((parent,view, position,id)->{
            fileName = list_file.get(position);
            files.setVisibility(View.GONE);
            bookmark_list.setVisibility(View.VISIBLE);
            initBookmarkArray();
        });
    }

    private void initBookmarkArray() {
        data = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences("sp_list", MODE_PRIVATE);
        String head = sp.getString("token", "");
        String ifBookmark = sp.getString("bookmark","");
        temList = bookmark.querySimilarRecord(fileName);
        reversedList();
        bookmarkAdapter bookmarkAdapter = new bookmarkAdapter(bookmark.this, R.layout.h_b_item,data);
        ListView bookmarkList = findViewById(R.id.bookmark_list);
        bookmarkList.setAdapter(bookmarkAdapter);

        bookmarkList.setOnItemClickListener((parent, view, position, id) -> {
            String address = data.get(position).getUrl();
            Intent intent = new Intent();
            intent.putExtra("address",address);
            bookmark.this.setResult(123,intent);
            bookmark.this.finish();
        });

        bookmarkList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            ActionSheetDialog dialog = new ActionSheetDialog(bookmark.this).builder().setTitle("请选择")
                    .addSheetItem("删除当前信息", null, which -> {
                        bookmarkResponse.DataBean hs = data.get(i);
                        bookmark.clearThisMess(hs);
                        if(ifBookmark.equals("true")){
                            String backAddress1 = "http://139.196.180.89:8137/api/v1/collections/id/"+hs.getId();
                            httpUtils.delete(backAddress1, head, new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    runOnUiThread(() -> showToast("获取历史记录网络错误"));
                                }
                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    /*得到的服务器返回值具体内容*/
                                    assert response.body() != null;
                                    final String responseData = response.body().string();
                                    runOnUiThread(() -> {
                                        Gson gson = new Gson();
                                        simpleResponse responsePut = gson.fromJson(responseData, simpleResponse.class);
                                        if (responsePut.getState().getCode() == 0) {
                                            showToast("删除成功");
                                        } else {
                                            showToast("由后端更新标签记录失败");
                                        }
                                    });
                                }
                            });
                        }
                        initBookmarkArray();
                        if (bookmarkAdapter.getCount() == 1) {
                            list_file = bookmark.queryFilename();
                            bookmarkFileAdapter myFilesAdapter = new bookmarkFileAdapter(bookmark.this,list_file);
                            ListView fileList = findViewById(R.id.bookmark_files_list);
                            fileList.setAdapter(myFilesAdapter);
                            bookmark_list.setVisibility(View.GONE);
                            files.setVisibility(View.VISIBLE);
                        }
                    }).addSheetItem("删除当前文件夹", null, which -> {
                        bookmark.clearThisFile(fileName);
                        if(ifBookmark.equals("true")){
                            String backAddress1 = "http://139.196.180.89:8137/api/v1/collections/tag/"+fileName;
                            httpUtils.delete(backAddress1, head, new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    runOnUiThread(() -> showToast("获取历史记录网络错误"));
                                }
                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    /*得到的服务器返回值具体内容*/
                                    assert response.body() != null;
                                    final String responseData = response.body().string();
                                    runOnUiThread(() -> {
                                        Gson gson = new Gson();
                                        simpleResponse responsePut = gson.fromJson(responseData, simpleResponse.class);
                                        if (responsePut.getState().getCode() == 0) {
                                            showToast("删除成功"+fileName);
                                        } else {
                                            showToast("由后端更新标签记录失败");
                                        }
                                    });
                                }
                            });
                        }
                        bookmark_list.setVisibility(View.GONE);
                        files.setVisibility(View.VISIBLE);
                        initFile();
                    });
            dialog.show();
            return true;
        });

    }

    public void back(View view) {
        if (bookmark_list.getVisibility() == View.VISIBLE) {
            bookmark_list.setVisibility(View.GONE);
            files.setVisibility(View.VISIBLE);
        }else{
            finish();
        }
    }
    /*颠倒list顺序，用户输入的信息会从上依次往下显示*/
    private void reversedList(){
        data.clear();
        for(int i = temList.size() - 1 ; i >= 0 ; i --) {
            data.add(temList.get(i));
        }
    }


    @Override
    public void onClick(View v) {

    }
}
