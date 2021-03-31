package com.golden.antelope.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import com.golden.antelope.R;

public class MusicService extends Service implements MediaPlayer.OnErrorListener {

    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mp;
    private int length = 0;

    public MusicService() {
    }

    public class ServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mp = MediaPlayer.create(this, R.raw.main_song);
        mp.setOnErrorListener(this);

        if (mp != null) {
            mp.setLooping(true);
            mp.setVolume(50, 50);
        }


        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            public boolean onError(MediaPlayer mp, int what, int
                    extra) {

                onError(MusicService.this.mp, what, extra);
                return true;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mp != null) {
            mp.start();
        }
        return START_NOT_STICKY;
    }




    public void pauseMusic() {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.pause();
                length = mp.getCurrentPosition();
            }
        }
    }

    public void resumeMusic() {
        if (mp != null) {
            if (!mp.isPlaying()) {
                mp.seekTo(length);
                mp.start();
            }
        }
    }

    public void startMusic() {
        mp = MediaPlayer.create(this, R.raw.main_song);
        mp.setOnErrorListener(this);

        if (mp != null) {
            mp.setLooping(true);
            mp.setVolume(50, 50);
            mp.start();
        }

    }

    public void stopMusic() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
            } finally {
                mp = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "Music player failed", Toast.LENGTH_SHORT).show();
        if (this.mp != null) {
            try {
                this.mp.stop();
                this.mp.release();
            } finally {
                this.mp = null;
            }
        }
        return false;
    }
}