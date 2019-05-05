package com.example.suxinwei.authcode.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class UserContentProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int MATCH_WOMAN = 1;
    private static final int MATCH_MAN = 2;

    static {
        //给UriMatcher初始化,添加其可以支持的uri
        sUriMatcher.addURI("com.itheima.userProvider", "t_woman", MATCH_WOMAN);
        sUriMatcher.addURI("com.itheima.userProvider", "t_man", MATCH_MAN);
    }

    private UserSQLiteOpenHelper userSQLiteOpenHelper;

    /*
     * 调用的时机:at application launch time 在主线程中被调用
     */
    @Override
    public boolean onCreate() {
        //在内容提供者中获取Context对象
        Context context = getContext();
        //初始化SQLiteOpenHelper
        userSQLiteOpenHelper = new UserSQLiteOpenHelper(context);

        //返回值:如果初始化成功了,就返回true.
        return true;
    }


    /*
     * 参数1:内容解析者传递过来的uri
     * 参数2:内容解析者访问内容提供者时传递的参数
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase database = userSQLiteOpenHelper.getWritableDatabase();
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            return null;
        }
        long insert = database.insert(tableName, null, values);
        Log.d("tag", "insert:" + insert);

        //需要返回插入成功后的id
        //但是这里只能返回一个uri类型的数据,不能直接返回id
        //解决方法:uri+id
        Uri withAppendedId = ContentUris.withAppendedId(uri, insert);
        return withAppendedId;

    }

    //自定义的方法
    private String getTableName(Uri uri) {
        String tableName = "";
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MATCH_WOMAN:
                tableName = "t_woman";
                break;
            case MATCH_MAN:
                tableName = "t_man";
                break;
            case UriMatcher.NO_MATCH:
                Log.d("tag", "错误的uri地址:" + uri);
                return null;

            default:
                break;
        }
        return tableName;
    }

    /*
     * 参数2:内容解析者提供的where表达式
     * 参数3:where表达式中的?号的真实值
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = userSQLiteOpenHelper.getReadableDatabase();

        String tableName = getTableName(uri);
        //删除成功的行数
        int delete = database.delete(tableName, selection, selectionArgs);

        return delete;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        SQLiteDatabase database = userSQLiteOpenHelper.getWritableDatabase();

        String tableName = getTableName(uri);
        /*
         * 参数1:表名
         * 参数2:要更新的字段的key和value
         */
        int update = database.update(tableName, values, selection, selectionArgs);
        //将影响的行数返回给内容解析者
        return update;
    }

    /*
     * 参数2:要查询哪些字段
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = userSQLiteOpenHelper.getReadableDatabase();

        String tableName = getTableName(uri);
        /*
         * 参数2:要查询哪些字段
         */

        Cursor cursor = database.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        //给内容解析者返回游标
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

}
