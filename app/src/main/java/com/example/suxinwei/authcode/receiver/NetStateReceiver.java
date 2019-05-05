package com.example.suxinwei.authcode.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetStateReceiver extends BroadcastReceiver {

    /*
     * 当该广播接收者接收到广播的时候,被系统回调该方法
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "监听到网络状态改变了额", Toast.LENGTH_SHORT).show();
    }

}
