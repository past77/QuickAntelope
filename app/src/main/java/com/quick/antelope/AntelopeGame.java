package com.golden.antelope;

import android.animation.ValueAnimator;
import android.content.*;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.golden.antelope.service.MusicService;

import java.util.Timer;
import java.util.TimerTask;

public class AntelopeGame extends AppCompatActivity {

    private int shipSpeed = 10;
    private long AnimationDuration;
    private ImageView bg1, bg2;
    private ImageView s1, s2, s3, lion1, grassScore, lion0;
    private TextView scorelabel, startlabel, actualscore;
    private TextView lvlcount;
    private ImageView antelope;
    private int antelopeY, antelopeX = 50;
    private int frameW;
    private int screenW, screenH;
    private int antelopeW, antelopeH;
    private int lion0X, lion0Y, lion0Speed = 20;
    private int lion1X, lion2Y;
    private double lion1Speed = 20;
    private int scoreX, scoreY;
    private int scoreSpeed = 15;
    private int score;
    private int best;
    private int lvl;
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private Boolean isPause = false;
    private FrameLayout frameLayout;
    private MusicService mServ;;
    private boolean mIsBound = false;

    private boolean action_flg = false;
    private boolean start_flg = false;
    private int lives = 3;
    private  boolean flag;
    private int musicFlag;
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.antelope_game);
        AnimationDuration = 10000L;
        AnimateBG();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        antelope = findViewById(R.id.antelope);
        s1 = findViewById(R.id.shield1);
        s2 = findViewById(R.id.shield2);
        s3 = findViewById(R.id.shield3);
        lion0 = findViewById(R.id.lion0);
        lion1 = findViewById(R.id.lion);
        grassScore = findViewById(R.id.scoreball);
        startlabel = findViewById(R.id.taptostartTV);
        scorelabel = findViewById(R.id.scoreTV);
        actualscore = findViewById(R.id.realscore);
        frameLayout = findViewById(R.id.frame);
        lvlcount = findViewById(R.id.lvlTV);

        setAnim();

        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);

        musicFlag = settings.getInt("MUSIC_FLAG",1);
        flag = musicFlag == 1;
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        if(flag)
            startService(music);



        HomeWatcher mHomeWatcher;

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if(flag) {
                    if (mServ != null) {
                        mServ.pauseMusic();
                    }
                }
            }

            @Override
            public void onHomeLongPressed() {
                if(flag) {
                    if (mServ != null) {
                        mServ.pauseMusic();
                    }
                }
            }
        });
        mHomeWatcher.startWatch();

        antelope.setX(antelopeX);
        antelopeW = antelope.getWidth();
        antelopeH = antelope.getHeight();

        lvl = 1;
        lvlcount.setText(lvl + "");

        screenW = size.x;
        screenH = size.y;

        lion0.setX(screenW + 200);
        lion0.setY(-280);
        lion1.setX(screenW + 500);
        lion1.setY(-280);
        grassScore.setX(screenW + 800);
        grassScore.setY(-280);


    }

    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
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

    private void playSound(int sound) {
            MediaPlayer pressSound = MediaPlayer.create(AntelopeGame.this, sound);
            pressSound.setVolume(30, 30);
            pressSound.start();
    }


    @Override
    protected void onPause() {
        super.onPause();

        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null && flag) {
                mServ.pauseMusic();
            }
        }
        isPause = true;
        try {
            timer.cancel();
            timer = null;
        } catch (Exception e){

        }
    }

    public void gamePlay() {
        int smallastroxCeter = lion0X + lion0.getWidth() / 2;
        int smallastroycenter = lion0Y + lion0.getHeight() / 2;
        int astroxCeter = lion1X + lion1.getWidth() / 2;
        int astroycenter = lion2Y + lion1.getHeight() / 2;

        lion0X -= lion0Speed;
        if (hitCheck(smallastroxCeter, smallastroycenter)) {
            lion0X = -400;
            if (lives == 3) {
                s1.setBackground(null);
            }
            if (lives == 2) {
                s2.setBackground(null);
            }
            if (lives == 1) {
                s3.setBackground(null);
            }
            lives--;
            if(flag) {
                playSound( R.raw.lrooar);
            }

            if (lives <= 0) {
                if(flag)
                    mServ.pauseMusic();
                timer.cancel();
                timer = null;
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("SCORE", score);
                finishAffinity();
                startActivity(intent);
            }
        }
        if (lion0X < -150) {
            lion0X = screenW + 150;
            lion0Y = (int) Math.floor(Math.random() * (1080 - lion0.getHeight()));
        }
        lion0.setX(lion0X);
        lion0.setY(lion0Y);

        lion1X -= lion1Speed;
        if (hitCheck(astroxCeter, astroycenter)) {
            lion1X = -800;
            if (lives == 3) {
                s1.setBackground(null);
            }
            if (lives == 2) {
                s2.setBackground(null);
            }
            if (lives == 1) {
                s3.setBackground(null);
            }
            lives--;
            if(flag) {
                playSound( R.raw.lrooar);
            }

            if (lives <= 0) {
                if(flag)
                 mServ.pauseMusic();

                timer.cancel();
                timer = null;
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("SCORE", score);
                finishAffinity();
                startActivity(intent);
            }
        }
        if (lion1X < -300) {
            lion1X = screenW + 500;
            lion2Y = (int) Math.floor(Math.random() * (1080 - lion1.getHeight()));
        }
        lion1.setX(lion1X);
        lion1.setY(lion2Y);

        scoreX -= scoreSpeed;
        if (hitCheck(scoreX, scoreY)) {
            if(flag) {
                playSound( R.raw.eat_grass);
            }
            scoreX = -900;
            score += 100;
            if (score % 1000 == 0) {
                lion0Speed += 5;
                lion1Speed += 5;
                scoreSpeed += 5;
                shipSpeed += 2;
                lvl++;
                lvlcount.setText(lvl + "");
            }
            actualscore.setText(Integer.toString(score));
            if (score > best) {
                best = score;
            }
        }
        if (scoreX < -50) {
            scoreX = screenW + 100;
            scoreY = (int) Math.floor(Math.random() * (1080 - lion1.getHeight()));
        }
        grassScore.setX(scoreX);
        grassScore.setY(scoreY);


        if (action_flg) {
            antelopeY -= shipSpeed;
        } else {
            antelopeY += shipSpeed;
        }
        if (antelopeY < 0) antelopeY = 4;
        if (antelopeY > (frameW - 400) - antelopeY) antelopeY = (frameW - 400) - antelopeY - 1;
        antelope.setY(antelopeY);
    }


    public boolean hitCheck(int x, int y) {
        if ((antelopeX <= x) && (x <= (antelopeX + antelope.getWidth()) &&
                (antelopeY <= y) && (y <= (antelopeY + antelope.getHeight())))) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flag) {
            if (mServ != null) {
                mServ.resumeMusic();
            }
        }
        if (isPause) {
            isPause = false;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> gamePlay());
                }
            }, 0, 20);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        doUnbindService();
        if(flag) {
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            stopService(music);
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!start_flg) {

            start_flg = true;
            frameW = frameLayout.getWidth();

            antelopeY = (int) antelope.getY();
            startlabel.clearAnimation();
            startlabel.setVisibility(View.GONE);


            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(() -> gamePlay());

                }
            }, 0, 20);

            lion0X = -150;
            lion0.setX(lion0X);
            lion0.setVisibility(View.VISIBLE);
            lion1X = -400;
            lion1.setX(lion1X);
            lion1.setVisibility(View.VISIBLE);
            scoreX = -100;
            grassScore.setX(scoreX);
            grassScore.setVisibility(View.VISIBLE);
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }
        return true;
    }

    public void AnimateBG() {
        bg1 = findViewById(R.id.bg1);
        bg2 = findViewById(R.id.bg2);
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(AnimationDuration);
        animator.addUpdateListener(animation -> {
            final float prog = (float) animation.getAnimatedValue();
            final float wid = bg1.getWidth();
            final float translationX = wid * prog;
            bg1.setTranslationX(translationX);
            bg2.setTranslationX(translationX - wid);
        });
        animator.start();
    }

    public void setAnim() {
        Animation fadeIn;
        startlabel = findViewById(R.id.taptostartTV);

        fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1200);
        fadeIn.setInterpolator(new LinearInterpolator());
        fadeIn.setRepeatCount(Animation.INFINITE);
        fadeIn.setRepeatMode(Animation.REVERSE);
        startlabel.startAnimation(fadeIn);
    }

}
