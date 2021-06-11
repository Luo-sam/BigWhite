package com.webbrowser.bigwhite.Model.SQLite;

/**
 * Created by Administrator on 2018/2/11.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 搜索记录帮助类
 * Created by 05 on 2016/7/27.
 */
public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "temp.db";
    private final static int DB_VERSION = 1;

    public RecordSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStr = "CREATE TABLE IF NOT EXISTS records (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
        db.execSQL(sqlStr);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
