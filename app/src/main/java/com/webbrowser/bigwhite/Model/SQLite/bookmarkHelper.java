package com.webbrowser.bigwhite.Model.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class bookmarkHelper extends SQLiteOpenHelper {
    public String TABLE_NAME_BOOKMARK = "mybookmark";
    /*设置初始值*/
    private static final String name = "mybookmark.db";
    private static final int version = 1;
    public bookmarkHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    /*
     * 数据库建表
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_history =   "create table if not exists " + TABLE_NAME_BOOKMARK +
                " (Id Integer, fileName char(20), Name char(20), Address char(20));";
        db.execSQL(sql_history);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
