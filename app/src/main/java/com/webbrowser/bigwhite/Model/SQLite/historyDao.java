package com.webbrowser.bigwhite.Model.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class historyDao {

    private static final String TAG = "SQL_history";
    /*列定义*/
    private final String[] HISTORY_COLUMNS = new String[]{"Name", "Age"};
    /*操作类存储的数据*/
    private Context context;
    private  historyHelper historyHelper;
    /*构造函数*/
    public historyDao(Context context){
        this.context = context;
        historyHelper = new historyHelper(context);
    }

    /*初始化数据*/
    public void initHistory(){
        SQLiteDatabase db = null;
        try{
            db = historyHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("insert into " + historyHelper.TABLE_NAME_HISTORY + " (Name,Address) value ('百度','www.baidu.com')");
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG,"初始化错误");
        }finally {
            if(db!=null){
               db.endTransaction();
               db.close();
            }
        }
    }

}
