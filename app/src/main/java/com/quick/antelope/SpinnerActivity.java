package com.golden.antelope;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SpinnerActivity extends AppCompatActivity {
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerTask.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SpinnerActivity.this, AntelopeRunActivity.class);
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                startActivity(intent);
            }
        };
        new Timer().schedule(timerTask, 3000);
    }

}
