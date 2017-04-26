package com.lazyduke.mytetris2.model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Lenovo on 2016/8/15.
 */
public class MapsModel {

    // 地图
    public boolean[][] maps;

    //地图宽度
    int xWidth;

    //地图高度
    int yHeight;

    //方块大小
    public int cubeSize;

    // 初始化视图画笔
    public Paint mapPaint;

    // 初始化辅助线画笔
    public Paint linePaint;

    //初始化状态画笔
    public Paint statePaint;

    public MapsModel(int xWidth, int yHeight,int cubeSize) {

        //初始化地图
        this.xWidth = xWidth;
        this.yHeight = yHeight;
        this.cubeSize = cubeSize;
        maps = new boolean[Config.MAPX][Config.MAPY];

        // 初始化视图画笔
        mapPaint = new Paint();
        mapPaint.setColor(0x50000000);
        mapPaint.setAntiAlias(true);

        // 初始化辅助线画笔
        linePaint = new Paint();
        linePaint.setColor(0xff666666);
        linePaint.setAntiAlias(true);

        //初始化状态画笔
        statePaint = new Paint();
        statePaint.setColor(0xffff0000);
        statePaint.setAntiAlias(true);
        statePaint.setTextSize(150);
    }

    //视图绘制
    public void drawMaps(Canvas canvas) {
        {

            for (int x=0;x<maps.length;x++) {
                for (int y = 0; y < maps[x].length; y++) {
                    if (maps[x][y]) {
                        // canvas.drawRect(left, top, right, bottom, paint);
                        canvas.drawRect(x * cubeSize, y * cubeSize, x * cubeSize + cubeSize,
                                y * cubeSize + cubeSize, mapPaint);
                    }
                }
            }
        }
    }

    //视图辅助线绘制
    public void drawLine(Canvas canvas) {

        // 竖线
        for (int x = 0; x <= maps.length; x++) {

            canvas.drawLine(x * cubeSize, 0, x * cubeSize,yHeight, linePaint);
        }

        // 横线
        for (int y = 0; y <= maps[0].length; y++) {

            canvas.drawLine(0, y * cubeSize, xWidth, y * cubeSize, linePaint);
        }
    }

    //画暂停状态
    public void drawPause(Canvas canvas,Boolean isPause) {
        if (isPause) {
            canvas.drawText("Paused", xWidth / 2 - statePaint.measureText("Paused") / 2, yHeight / 2, statePaint);
        }
    }

    // 清空视图
    public void cleanMaps() {
        for( int x = 0; x<maps.length;x++)    {
            for (int y = 0; y < maps[x].length; y++) {
                maps[x][y] = false;
            }
        }
    }

    /** 消行处理 */
    public int DelLine() {
        int lines=0;
        for (int y = maps[0].length - 1; y > 0; y--) {
            if (checkLine(y)) {
                // 执行消行
                delete(y);
                y++;
                lines++;
            }
        }
        return lines;
    }

    /** 消行判定 */
    public boolean checkLine(int y) {
        for (int x = 0; x < maps.length; x++) {
            if (!maps[x][y]) {
                return false;
            }
        }
        return true;
    }

    /** 执行消行 */
    public void delete(int dy) {
        for (int y = dy; y > 0; y--) {
            for (int x = 0; x < maps.length; x++) {
                maps[x][y] = maps[x][y - 1];
            }
        }
    }

}
