package com.golden.antelope;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    private int score,lowestScore,lvl, musicFlag;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scorelabel = findViewById(R.id.resultsTV);
        TextView bestlabel = findViewById(R.id.bestresultTV);


        score = getIntent().getIntExtra("SCORE",0);
        scorelabel.setText(score+"");
        lvl = getIntent().getIntExtra("LEVEL",0);

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE",0);

        musicFlag = settings.getInt("MUSIC_FLAG",0);
        Log.d("TAG", musicFlag + " - musicFlag");
        SharedPreferences pref = getSharedPreferences("GAME_DATA",MODE_PRIVATE);
        lowestScore=pref.getInt("best10",0);

        if (score > highScore){
            bestlabel.setText("High Score : "+score);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE",score);
            editor.commit();
        }else {
            bestlabel.setText("High Score : " + highScore);
        }
        if(score > lowestScore){
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("LAST_SCORE",score);
            editor.commit();
        }


    }

    public void retry (View v){
        finishAffinity();
        startActivity(new Intent(getApplicationContext(),AntelopeGame.class));
    }

    public void backHome(View view){
        Intent intent = new Intent(ResultActivity.this,AntelopeRunActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void highScores(View v){
        Intent intent=new Intent(ResultActivity.this,HighScoreActivity.class);
        finishAffinity();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResultActivity.this,AntelopeRunActivity.class);
        startActivity(intent);
        finishAffinity();
    }

}

