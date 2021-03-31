package com.golden.antelope;

import android.content.*;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.golden.antelope.animation.AnimationUtils;
import com.golden.antelope.service.MusicService;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

public class AntelopeRunActivity extends AppCompatActivity {
    private boolean mIsBound = false;
    private MusicService mService;
    boolean isMusic = true;
    Intent music;
    private int choosedOptionMusic;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.antelope_run);
        doBindService();
        music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);
        AnimationUtils.scale(findViewById(R.id.start_ant), 0.7f, 1000l);

        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4164972649105783/3341579099");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {
            }
        });


    }

    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mService = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.info:
                openInfoActivity();
                return true;
            case R.id.music:
                musicAlertDialog();
                return true;
            case R.id.rate:
                rateUs();
                return true;
            case R.id.share:
                shareUs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private  void rateUs(){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        }catch (ActivityNotFoundException e){
            Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName());
        }

    }

    private  void shareUs(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void musicAlertDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View settingsLayout = inflater.inflate(R.layout.musi_dialog, null);

        AlertDialog.Builder settingsBuilder = new AlertDialog.Builder(AntelopeRunActivity.this, R.style.AlertDialogTheme);
        settingsBuilder.setCustomTitle(settingsLayout);
        choosedOptionMusic = settings.getInt("MUSIC_FLAG",1);
        settingsBuilder.setSingleChoiceItems(R.array.music_onOff, choosedOptionMusic, (DialogInterface.OnClickListener) (dialog, which) -> {

            switch(which)
            {

                case 0:
                    mService.pauseMusic();
                    isMusic = false;
                    editor = settings.edit();
                    editor.putInt("MUSIC_FLAG", 0);
                    editor.apply();
                    break;
                case 1:
                    mService.resumeMusic();
                    isMusic = true;
                    editor = settings.edit();
                    editor.putInt("MUSIC_FLAG", 1);
                    editor.apply();;
                    break;
                default:
                    throw new IllegalStateException("wtf");
            }
        });
        settingsBuilder.setPositiveButton("OK", (dialog, which) -> {
        });
        AlertDialog dialog = settingsBuilder.create();
        dialog.show();
     }

    void openInfoActivity(){
        Intent i = new Intent(AntelopeRunActivity.this, InfoActivity.class);
        i.putExtra("antelope_policy", true);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void startAntelopeGame(View view) {
        Intent intent = new Intent(AntelopeRunActivity.this, AntelopeGame.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

                if (mService != null && isMusic) {
                    mService.pauseMusic();
            }
    }

    public void statAntelopeGame(View view) {
        Intent intent=new Intent(AntelopeRunActivity.this,HighScoreActivity.class);
        finishAffinity();
        startActivity(intent);
    }
}