package com.example.suxinwei.authcode.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UserSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "user.db";
    private static final int VERSION = 1;

    private UserSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public UserSQLiteOpenHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 初始化两张表
        // t_woman
        String sql = "create table t_woman(_id integer primary key,c_name varchar(20),c_age integer,c_phone varchar(12))";
        db.execSQL(sql);

        // t_man
        String sql2 = "create table t_man(_id integer primary key,c_name varchar(20),c_age integer,c_phone varchar(12))";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
