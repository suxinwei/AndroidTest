package com.example.suxinwei.authcode;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecondActivity extends Activity {
    private ListView lv_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        lv_users = (ListView) findViewById(R.id.lv_users);

        //获取到其他Activity启动我时给我传递的Intent对象
        Intent intent = getIntent();
        //从Intent对象中获取数据
        String username = intent.getStringExtra("name");
        int age = intent.getIntExtra("age", 0);
        double score = intent.getDoubleExtra("score", 0);

        Toast.makeText(this, "usernmae=" + username + "\nage=" + age + "\nscore=" + score, Toast.LENGTH_SHORT).show();
    }

    public void insertWoman(View view) {
        /*
         * 1. 获取内容解析者
         */
        ContentResolver contentResolver = getContentResolver();

        ContentValues values = new ContentValues();
        values.put("c_name", "lucy" + new Random().nextInt(100));
        values.put("c_age", 20 + new Random().nextInt(30));
        values.put("c_phone", "110");
        /*
         * 2. 调用内容解析者的insert方法
         */
        Uri insert = contentResolver.insert(Uri.parse("content://com.itheima.userProvider/t_woman"), values);
        /*
         * 3. 将返回的uri中的id再解析出来
         */
        long parseId = ContentUris.parseId(insert);
        Toast.makeText(this, "插入成功后的id是:" + parseId, Toast.LENGTH_SHORT).show();

    }

    public void insertMan(View view) {
        /*
         * 1. 获取内容解析者
         */
        ContentResolver contentResolver = getContentResolver();

        ContentValues values = new ContentValues();
        values.put("c_name", "高斯雷" + new Random().nextInt(100));
        values.put("c_age", 30 + new Random().nextInt(30));
        values.put("c_phone", "120");
        /*
         * 2. 调用内容解析者的insert方法
         */
        Uri insert = contentResolver.insert(Uri.parse("content://com.itheima.userProvider/t_man"), values);
        /*
         * 3. 将返回的uri中的id再解析出来
         */
        long parseId = ContentUris.parseId(insert);
        Toast.makeText(this, "插入成功后的id是:" + parseId, Toast.LENGTH_SHORT).show();
    }

    public void deleteMan(View view) {
        /*
         * 获取内容解析者,然后调用delete方法
         * 需求:将年龄大于54的干掉
         */
        int delete = getContentResolver().delete(Uri.parse("content://com.itheima.userProvider/t_man"), "c_age>?", new String[]{"54"});
        Toast.makeText(this, "成功删除了:" + delete, Toast.LENGTH_SHORT).show();
    }

    public void updateMan(View view) {
        ContentValues values = new ContentValues();
        values.put("c_name", "雨泽2");
        /*
         * 需求:将年龄小于40的记录的c_name改为宇泽
         *
         * update t_user set c_name='雨泽' where c_age<40;
         *
         */
        int update = getContentResolver().update(Uri.parse("content://com.itheima.userProvider/t_man"), values, "c_age<?", new String[]{"40"});
        Toast.makeText(this, "修改了:" + update, Toast.LENGTH_SHORT).show();
    }

    public void queryMan(View view) {
        List<String> users = new ArrayList<>();
        /*
         * 最后一个参数:排序表达式,不能包好order by本身
         */
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.itheima.userProvider/t_man"), new String[]{"c_name", "c_age", "c_phone"}, null, null, "c_age desc");
        /*
         * 遍历cursor
         */
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            int age = cursor.getInt(1);
            String phone = cursor.getString(2);

            users.add("name=" + name + "\nage=" + age + "\nphone=" + phone);
        }
        cursor.close();
        //将数据显示到ListView上
        lv_users.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users));

    }
}
