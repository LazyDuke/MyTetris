package com.lazyduke.mytetris2.model;

import android.content.Context;
import android.widget.TextView;

import com.lazyduke.mytetris2.SPUtil.SPUtils;

/**
 * Created by Lenovo on 2016/8/16.
 */
public class ScoreModel {

    //当前分数
    public int score;

    //最高分数
    public int bestScore;

    //根据消去行数加分
    public void addScore(int lines) {
        if(lines==0)
            return;
        int add=(lines+(lines-1))*100;
        score+=add;
    }

    //更新最高分

    public void updatebestScore(Context context){

        if (bestScore==0){
            bestScore=Integer.parseInt(String.valueOf(SPUtils.get(context,"bestScore",0))) ;
        }
        if (score>bestScore){
            bestScore=score;
            SPUtils.put(context,"bestScore",bestScore);
        }
    }

    //显示当前分数
    public void showCurrSco(TextView currSco){
        if (currSco!=null){
            currSco.setText(String.valueOf(score));
        }
    }
    //显示最高分数
    public void showBestSco(Context context,TextView bestSco){
        if (bestSco!=null){
            updatebestScore(context);
            bestSco.setText(String.valueOf(bestScore));
        }
    }

}
