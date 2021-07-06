package com.webbrowser.bigwhite.Model.SQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.webbrowser.bigwhite.Model.data.historyResponse;

import java.util.ArrayList;
import java.util.List;

public class historyDao {

    private static final String TAG = "SQL_history";
    private final historyHelper historyHelper;

    /*构造函数*/
    public historyDao(Context context) {
        /*操作类存储的数据*/
        historyHelper = new historyHelper(context);
    }

    //判断是否含有该搜索记录
    public boolean isHasRecord(historyResponse.DataBean historyData) {
        boolean isHasRecord = false;
        SQLiteDatabase db = historyHelper.getWritableDatabase();
        Cursor cursor = db.query("myhistory", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (historyData.getUrl().equals(cursor.getString
                    (cursor.getColumnIndexOrThrow("Address")))) {
                isHasRecord = true;
            }
        }
        //关闭数据库
        db.close();
        cursor.close();
        return isHasRecord;
    }

    /*添加记录*/
    public void addHistory(historyResponse.DataBean historyData) {
        if (!historyData.getTitle().equals("百度一下")) {
            SQLiteDatabase db = null;
            try {
                db = historyHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("Name", historyData.getTitle());
                contentValues.put("Address", historyData.getUrl());
                db.insertOrThrow(historyHelper.TABLE_NAME_HISTORY, null, contentValues);
            } catch (Exception e) {
                Log.e(TAG, "add error", e);
            } finally {
                if (db != null && db.inTransaction()) {
                    db.endTransaction();
                    db.close();
                }
            }
        }
    }


    public void addHistoryFromBack(List<historyResponse.DataBean> historyData) {
        SQLiteDatabase db = null;
        try {
            db = historyHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            for (historyResponse.DataBean data : historyData) {
                contentValues.put("Name", data.getTitle());
                contentValues.put("Address", data.getUrl());
                db.insertOrThrow(historyHelper.TABLE_NAME_HISTORY, null, contentValues);
            }
        } catch (Exception e) {
            Log.e(TAG, "add error", e);
        } finally {
            if (db != null && db.inTransaction()) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /*得到所有历史记录*/
    public List<historyResponse.DataBean> queryHistory() {
        SQLiteDatabase db = historyHelper.getWritableDatabase();
        List<historyResponse.DataBean> list = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = db.query(historyHelper.TABLE_NAME_HISTORY, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString((cursor.getColumnIndex("Name")));
                String address = cursor.getString((cursor.getColumnIndex("Address")));
                list.add(new historyResponse.DataBean(name, address));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void clearThisMess(historyResponse.DataBean historyData) {
        SQLiteDatabase db = historyHelper.getWritableDatabase();
        db.execSQL("delete from " + historyHelper.TABLE_NAME_HISTORY + " where Address = " + "'" + historyData.getUrl() + "'" + ";");
        db.close();
    }


    /*删除所有历史记录*/
    public void clearHistory() {
        SQLiteDatabase db = historyHelper.getWritableDatabase();
        db.execSQL("delete from " + historyHelper.TABLE_NAME_HISTORY);

        db.close();
    }
}
