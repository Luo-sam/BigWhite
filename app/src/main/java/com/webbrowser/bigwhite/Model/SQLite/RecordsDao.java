package com.webbrowser.bigwhite.Model.SQLite;


/**
 * Created by Administrator on 2018/2/11.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索记录操作类
 * Created by 05 on 2016/7/27.
 */
public class RecordsDao {
    RecordSQLiteOpenHelper recordHelper;

    SQLiteDatabase recordsDb;

    public RecordsDao(Context context) {
        recordHelper = new RecordSQLiteOpenHelper(context);
    }

    //添加搜索记录
    public void addRecords(String record) {

        if (!isHasRecord(record)) {
            recordsDb = recordHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", record);
            //添加
            recordsDb.insert("records", null, values);
            //关闭
            recordsDb.close();
        }
    }

    //判断是否含有该搜索记录
    public boolean isHasRecord(String record) {
        boolean isHasRecord = false;
        recordsDb = recordHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query("records", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (record.equals(cursor.getString(cursor.getColumnIndexOrThrow("name")))) {
                isHasRecord = true;
            }
        }
        //关闭数据库
        recordsDb.close();
        cursor.close();
        return isHasRecord;
    }

    //获取全部搜索记录
    public List<String> getRecordsList() {
        List<String> recordsList = new ArrayList<>();
        recordsDb = recordHelper.getReadableDatabase();
        Cursor cursor = recordsDb.query("records", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            recordsList.add(name);
        }
        //关闭数据库
        recordsDb.close();
        cursor.close();
        return recordsList;
    }

    //模糊查询
    public List<String> querySimlarRecord(String record){
        String queryStr = "select * from records where name like '%" + record + "%' order by name ";
        List<String> similarRecords = new ArrayList<>();
        Cursor cursor= recordHelper.getReadableDatabase().rawQuery(queryStr,null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            similarRecords.add(name);
        }

        cursor.close();
        return similarRecords;
    }

    //清空搜索记录
    public void deleteAllRecords() {
        recordsDb = recordHelper.getWritableDatabase();
        recordsDb.execSQL("delete from records");

        recordsDb.close();
    }
    // 删除
    public int delete(int _id) {

        SQLiteDatabase db = recordHelper.getWritableDatabase();
        int d = db.delete("records", "_id=?", new String[] { _id + "" });

        db.close();
        return d;

    }

}