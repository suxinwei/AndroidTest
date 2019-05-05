package com.example.suxinwei.authcode.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicManager();
    }

    private void play(String name) {
        Toast.makeText(this, "歌开始播放", Toast.LENGTH_SHORT).show();
    }

    private void pause() {
        Toast.makeText(this, "音乐暂停了", Toast.LENGTH_SHORT).show();
    }

    private void stop() {
        Toast.makeText(this, "音乐停止了", Toast.LENGTH_SHORT).show();
    }

    public class MusicManager extends Binder {
        public void play(String name) {
            MusicService.this.play(name);
        }

        public void pause() {
            MusicService.this.pause();
        }

        public void stop() {
            MusicService.this.stop();
        }

    }

}
