package com.example.suxinwei.authcode;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class FourthActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        //获取intent
        Intent intent = getIntent();
        //获取data
        Uri data = intent.getData();
        //获取数据类型
        String type = intent.getType();

        Toast.makeText(this, "data=" + data + "\ntype=" + type, Toast.LENGTH_LONG).show();
    }
}
