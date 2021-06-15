package com.webbrowser.bigwhite.Model.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.webbrowser.bigwhite.Model.data.historyData;

import java.util.ArrayList;
import java.util.List;

public class historyDao {

    private static final String TAG = "SQL_history";
    /*列定义*/
    private final String[] HISTORY_COLUMNS = new String[]{"Id","Name", "Age"};
    private final historyHelper historyHelper;
    /*构造函数*/
    public historyDao(Context context){
        /*操作类存储的数据*/
        historyHelper = new historyHelper(context);
    }

    /*初始化数据*/
    public void initHistory(){
        SQLiteDatabase db = null;
        try{
            db = historyHelper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("INSERT INTO " + historyHelper.TABLE_NAME_HISTORY + " (Name,Address) " +
                    "VALUES ('百度','www.baidu.com');");
            Log.d(TAG, historyHelper.TABLE_NAME_HISTORY);
            db.setTransactionSuccessful();
            Log.d(TAG, "initHistory: 成功");
        }catch (Exception e){
            Log.d(TAG,"init：失败");
        }finally {
            if(db!=null&&db.inTransaction()){
               db.endTransaction();
               db.close();
            }
        }
    }
    /*添加记录*/
    public void addHistory(historyData historyData){
        SQLiteDatabase db = null;
        try{
            db = historyHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Name",historyData.getName());
            contentValues.put("Address",historyData.getAddress());
            db.insertOrThrow(historyHelper.TABLE_NAME_HISTORY,null,contentValues);
        }catch (Exception e){
            Log.e(TAG, "add error", e);
        }finally {
            if(db!=null&&db.inTransaction()){
                db.endTransaction();
                db.close();
            }
        }
    }
    /*得到所有历史记录*/
    public List<historyData> queryHistory(){
        SQLiteDatabase db = historyHelper.getWritableDatabase();
        List<historyData> list = new ArrayList<>();
        Cursor cursor = db.query(historyHelper.TABLE_NAME_HISTORY,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                String name = cursor.getString((cursor.getColumnIndex("Name")));
                String address = cursor.getString((cursor.getColumnIndex("Address")));
                list.add(new historyData(name,address));
            }while (cursor.moveToNext());
        }
        return list;
    }
}
