package com.webbrowser.bigwhite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hb.dialog.myDialog.ActionSheetDialog;
import com.webbrowser.bigwhite.Model.SQLite.bookmarkDao;
import com.webbrowser.bigwhite.Model.data.historyData;
import com.webbrowser.bigwhite.R;
import com.webbrowser.bigwhite.View.adapter.bookmarkAdapter;
import com.webbrowser.bigwhite.View.adapter.bookmarkFileAdapter;

import java.util.ArrayList;
import java.util.List;

public class bookmark extends AppCompatActivity {
    private List<historyData> data;
    private List<historyData> temList;
    private List<String> list_file;
    private bookmarkDao bookmark;

    private String fileName;

    private LinearLayout files;
    private LinearLayout bookmark_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);
        initFile();
    }
    public void initFile(){
        files = findViewById(R.id.file_layout);
        bookmark_list = findViewById(R.id.bookmark_layout);
        bookmark_list.setVisibility(View.GONE);

        /*初始化list中history的属性*/
        bookmark = new bookmarkDao(this);
        list_file = new ArrayList<>();
        list_file = bookmark.queryFilename();
        bookmarkFileAdapter myFilesAdapter = new bookmarkFileAdapter(bookmark.this,list_file);
        ListView fileList = findViewById(R.id.bookmark_files_list);
        fileList.setAdapter(myFilesAdapter);

        fileList.setOnItemClickListener((parent,view, position,id)->{
            fileName = list_file.get(position);
            Log.d("TAG", fileName);
            files.setVisibility(View.GONE);
            bookmark_list.setVisibility(View.VISIBLE);
            initBookmarkArray();
        });
    }

    private void initBookmarkArray() {
        data = new ArrayList<>();
        temList = bookmark.querySimilarRecord(fileName);
        reversedList();
        bookmarkAdapter historyAdapter = new bookmarkAdapter(bookmark.this, R.layout.h_b_item,data);
        ListView bookmarkList = findViewById(R.id.bookmark_list);
        bookmarkList.setAdapter(historyAdapter);

        bookmarkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String address = data.get(position).getAddress();
                Intent intent = new Intent();
                intent.putExtra("address",address);
                bookmark.this.setResult(123,intent);
                bookmark.this.finish();
            }
        });

        bookmarkList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActionSheetDialog dialog = new ActionSheetDialog(bookmark.this).builder().setTitle("请选择")
                        .addSheetItem("删除当前信息", null, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                historyData hs = data.get(i);
                                bookmark.clearThisMess(hs);
                                initBookmarkArray();
                                Log.d("clear", hs.getName());
                            }
                        }).addSheetItem("删除当前文件夹", null, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                bookmark.clearThisFile(fileName);
                                bookmark_list.setVisibility(View.GONE);
                                files.setVisibility(View.VISIBLE);
                                initFile();
                            }
                        });
                dialog.show();
                return true;
            }
        });

    }


    public void clearBookmark(View view) {
        AlertDialog.Builder clearSure = new AlertDialog.Builder(bookmark.this);
        clearSure.setPositiveButton("确认",
                (dialog, which) -> {
                    bookmark.clearBookmark();
                    bookmark_list.setVisibility(View.GONE);
                    files.setVisibility(View.VISIBLE);
                    initFile();
                });

        clearSure.setNegativeButton("取消",(dialog, which) -> dialog.dismiss());
        clearSure.setTitle("提示");
        clearSure.setMessage("您确认清空搜索历史吗");
        clearSure.show();
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
}
