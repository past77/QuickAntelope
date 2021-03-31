package com.golden.antelope.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.golden.antelope.R;
import com.golden.antelope.Score;

import java.util.ArrayList;
import java.util.List;

public class AdapterScore extends ArrayAdapter<Score> {

    private Context mContext;
    private List<Score> highscores=new ArrayList<>();

    public AdapterScore(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Score> list){
        super(context,0,list);
        mContext =context;
        highscores=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;
        if(listItem==null)
            listItem= LayoutInflater.from(mContext).inflate(R.layout.custon_row,parent,false);


        Score currentScorer = highscores.get(position);

        TextView scoreRank = listItem.findViewById(R.id.positionscoreTV);
        scoreRank.setTextSize(55);
        scoreRank.setTextColor(Color.WHITE);
        scoreRank.setText(Integer.toString(currentScorer.getRank()));

        TextView scorr = listItem.findViewById(R.id.highscoreTV);
        scorr.setText(currentScorer.getScore());



        return listItem;
    }

}
