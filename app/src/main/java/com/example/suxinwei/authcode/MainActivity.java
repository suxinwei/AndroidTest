package com.example.suxinwei.authcode;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import com.example.suxinwei.authcode.receiver.NetStateReceiver;
import com.example.suxinwei.authcode.service.MusicService;
import com.example.suxinwei.authcode.service.MusicService.MusicManager;

public class MainActivity extends Activity {

    private NetStateReceiver netStateReceiver;
    private MusicServiceConnection musicServiceConnection;
    private MusicManager musicManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNetReceiver();

        initMusicService();
    }

    private void initNetReceiver() {
        netStateReceiver = new NetStateReceiver();
        //封装一个IntentFilter
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netStateReceiver, filter);
    }

    private void initMusicService() {
        // 目的就是为了延长进程的生命的周期
        startService(new Intent(this, MusicService.class));

        musicServiceConnection = new MusicServiceConnection();
        //绑定音乐服务(目的是为了获取到控制音乐的播放器管理器)
        bindService(new Intent(this, MusicService.class), musicServiceConnection, BIND_AUTO_CREATE);
    }

    //显示启动SecondActivity
    public void startSecond(View view) {
        Intent intent = new Intent();
        //将目标Activity的字节码设置给Intent对象
        intent.setClass(this, SecondActivity.class);
        //给intent绑定数据
        intent.putExtra("name", "张三");
        intent.putExtra("age", 24);
        intent.putExtra("score", 99.5);
        startActivity(intent);
    }

    //隐式意图启动ThirdActivity
    public void startThird(View view) {
        Intent intent = new Intent();
        intent.setAction("com.itheima.myaction");
        //如果目标Activity的category是DEFAULT,该行代码可以不要
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivity(intent);
    }

    public void startBrowser(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://www.taobao.com"));
        startActivity(intent);
    }

    public void startFourth(View view) {
        Intent intent = new Intent();
        intent.setAction("com.itheima.action.Fourth");
        intent.setDataAndType(Uri.parse("itheima:这是我给你的数据"), "text/html");
        startActivity(intent);
    }

    //隐式意图启动ThirdActivity
    public void startOtherApp(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.sxw.materialtest", "com.sxw.materialtest.FruitActivity");
        intent.setAction("com.itheima.myaction");
        //如果目标Activity的category是DEFAULT,该行代码可以不要
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivity(intent);
    }

    class MusicServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicManager = (MusicManager) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }


    public void play(View view) {
        if (musicManager == null) {
            Toast.makeText(this, "还没绑定服务,无法调用", Toast.LENGTH_SHORT).show();
            return;
        }
        musicManager.play("分手快乐");
    }

    public void pause(View view) {
        if (musicManager == null) {
            Toast.makeText(this, "还没绑定服务,无法调用", Toast.LENGTH_SHORT).show();
            return;
        }
        musicManager.pause();
    }

    public void stop(View view) {
        if (musicManager == null) {
            Toast.makeText(this, "还没绑定服务,无法调用", Toast.LENGTH_SHORT).show();
            return;
        }
        musicManager.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (netStateReceiver != null) {
            unregisterReceiver(netStateReceiver);
            netStateReceiver = null;
        }

        if (musicServiceConnection != null) {
            //解绑服务
            unbindService(musicServiceConnection);
            musicServiceConnection = null;
            musicManager = null;
        }
    }


}
