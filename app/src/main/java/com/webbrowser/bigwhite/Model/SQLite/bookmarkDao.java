package com.webbrowser.bigwhite.Model.SQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.webbrowser.bigwhite.Model.data.historyData;

import java.util.ArrayList;
import java.util.List;

public class bookmarkDao {

    private static final String TAG = "SQL_Bookmark";
    /*列定义*/
    private final String[] HISTORY_COLUMNS = new String[]{"fileName", "Name", "Age"};
    private final bookmarkHelper bookmarkHelper;
    private final Context mContext;

    /*构造函数*/
    public bookmarkDao(Context context) {
        /*操作类存储的数据*/
        bookmarkHelper = new bookmarkHelper(context);
        mContext = context;
    }

    /*添加记录*/
    public void addBookmark(historyData historyData, String fileName) {
        if (!historyData.getName().equals("百度一下")) {
            SQLiteDatabase db = null;
            try {
                db = bookmarkHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("fileName", fileName);
                contentValues.put("Name", historyData.getName());
                contentValues.put("Address", historyData.getAddress());
                db.insertOrThrow(bookmarkHelper.TABLE_NAME_BOOKMARK, null, contentValues);
            } catch (Exception e) {
                Log.e(TAG, "add error", e);
            } finally {
                if (db != null && db.inTransaction()) {
                    db.endTransaction();
                    db.close();
                }
            }
        } else {
            Toast.makeText(mContext, "首页不必添加书签", Toast.LENGTH_SHORT).show();
        }
    }

    /*得到所有标签记录*/
    public List<historyData> queryBookmark() {
        SQLiteDatabase db = bookmarkHelper.getWritableDatabase();
        List<historyData> list = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = db.query(bookmarkHelper.TABLE_NAME_BOOKMARK, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString((cursor.getColumnIndex("Name")));
                String address = cursor.getString((cursor.getColumnIndex("Address")));
                list.add(new historyData(name, address,"1"));
            } while (cursor.moveToNext());
        }
        return list;
    }

    /*根据名称得到需要的记录*/
    public List<historyData> querySimilarRecord(String record) {
        String queryStr = "select * from " + bookmarkHelper.TABLE_NAME_BOOKMARK + " where fileName = " + "'" + record + "'; ";
        List<historyData> similarRecords = new ArrayList<>();
        Cursor cursor = bookmarkHelper.getReadableDatabase().rawQuery(queryStr, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("Address"));
            similarRecords.add(new historyData(name, address,"1"));
        }

        cursor.close();
        return similarRecords;
    }


    public List<String> queryFilename() {
        List<String> similarRecords = new ArrayList<>();
        try (SQLiteDatabase db = bookmarkHelper.getReadableDatabase()) {
            String queryStr = "select distinct fileName from " + bookmarkHelper.TABLE_NAME_BOOKMARK + "; ";
            Cursor cursor = db.rawQuery(queryStr, null);
            while (cursor.moveToNext()) {
                String file = cursor.getString(cursor.getColumnIndexOrThrow("fileName"));
                if (file != null && file.length() > 0) {
                    similarRecords.add(file);
                }
            }
            cursor.close();

        } catch (Exception e) {
            Log.d(TAG, "queryError", e);
        }

        return similarRecords;
    }


    /*删除所有标签记录*/
    public void clearBookmark() {
        SQLiteDatabase db = bookmarkHelper.getWritableDatabase();
        db.execSQL("delete from " + bookmarkHelper.TABLE_NAME_BOOKMARK);

        db.close();
    }

    public void clearThisMess(historyData historyData) {
        SQLiteDatabase db = bookmarkHelper.getWritableDatabase();
        db.execSQL("delete from " + bookmarkHelper.TABLE_NAME_BOOKMARK + " where Name = " + "'" + historyData.getName() + "'" +
                " and Address = " + "'" + historyData.getAddress() + "'" + ";");
        db.close();
    }

    public void clearThisFile(String fileName) {
        SQLiteDatabase db = bookmarkHelper.getWritableDatabase();
        db.execSQL("delete from " + bookmarkHelper.TABLE_NAME_BOOKMARK
                + " where fileName = "
                + "'"
                + fileName + "'"
                + ";");
        db.close();
    }

}
