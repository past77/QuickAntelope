package com.golden.antelope;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.golden.antelope.adapter.AdapterScore;

import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity {

    private int temp,lastScore, best1, best2, best3, best4, best5, best6,best7, best8,best9,best10;
    private ListView listView;
    private AdapterScore adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        SharedPreferences pref = getSharedPreferences("GAME_DATA",MODE_PRIVATE);
        lastScore = pref.getInt("LAST_SCORE",0);
        SharedPreferences.Editor zero  =  pref.edit();
        zero.putInt("LAST_SCORE", 0);
        zero.apply();

        best1=pref.getInt("best1",0);
        best2=pref.getInt("best2",0);
        best3=pref.getInt("best3",0);
        best4=pref.getInt("best4",0);
        best5=pref.getInt("best5",0);
        best6=pref.getInt("best6",0);
        best7=pref.getInt("best7",0);
        best8=pref.getInt("best8",0);
        best9=pref.getInt("best9",0);
        best10=pref.getInt("best10",0);


        if(lastScore>best10){
            best10=lastScore;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("best10", best10);
            editor.apply();
        }
        if(lastScore>best9){
            temp=best9;
            best9=lastScore;
            best10=temp;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("best9", best9);
            editor.putInt("best10", best10);
            editor.apply();
        }
        if(lastScore>best8){
            temp=best8;
            best8=lastScore;
            best9=temp;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("best9", best9);
            editor.putInt("best8", best8);
            editor.apply();
        }
        if(lastScore>best7){
            temp=best7;
            best7=lastScore;
            best8=temp;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("best7", best7);
            editor.putInt("best8", best8);
            editor.apply();
        }
        if(lastScore>best6){
            temp=best6;
            best6=lastScore;
            best7=temp;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("best7", best7);
            editor.putInt("best6", best6);
            editor.apply();
        }
        if (lastScore > best5) {
            temp=best5;
            best5=lastScore;
            best6 = temp;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("best5", best5);
            editor.putInt("best6", best6);
            editor.apply();
        }
        if (lastScore > best4) {
            temp = best4;
            best4 = lastScore;
            best5 = temp;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("best4", best4);
            editor.putInt("best5", best5);
            editor.apply();
        }
        if (lastScore > best3) {
            temp = best3;
            best3 = lastScore;
            best4 = temp;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("best3", best3);
            editor.putInt("best4", best4);
            editor.apply();
        }
        if (lastScore > best2) {
            temp = best2;
            best2 = lastScore;
            best3 = temp;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("best2", best2);
            editor.putInt("best3", best3);
            editor.apply();
        }
        if (lastScore > best1) {
            temp = best1;
            best1 = lastScore;
            best2 = temp;
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("best1", best1);
            editor.putInt("best2", best2);
            editor.apply();
        }


        int i=0;
        listView = findViewById(R.id.highscoreLV);
        ArrayList<Score> scorerList = new ArrayList<>();
        scorerList.add(new Score(++i,best1+""));
        scorerList.add(new Score(++i,best2+""));
        scorerList.add(new Score(++i,best3+""));
        scorerList.add(new Score(++i,best4+""));
        scorerList.add(new Score(++i,best5+""));
        scorerList.add(new Score(++i,best6+""));
        scorerList.add(new Score(++i,best7+""));
        scorerList.add(new Score(++i,best8+""));
        scorerList.add(new Score(++i,best9+""));
        scorerList.add(new Score(++i,best10+""));


        SharedPreferences settings= getSharedPreferences("GAME_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("LOW_SCORE",best10);
        editor.commit();

        adapter = new AdapterScore(this,scorerList);
        listView.setAdapter(adapter);
    }

    public void backHome(View view){
        Intent intent = new Intent(HighScoreActivity.this,AntelopeRunActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HighScoreActivity.this,AntelopeRunActivity.class);
        startActivity(intent);
        finish();
    }
}
