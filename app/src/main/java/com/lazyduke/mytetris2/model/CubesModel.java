package com.lazyduke.mytetris2.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

/**
 * Created by Lenovo on 2016/8/15.
 */
public class CubesModel {
    // 方块
    public Point[] cubes;

    // 方块类型
    public int cubeType;

    // 方块大小
    public int cubeSize;

    // 初始化方块画笔
    public Paint cubePaint;

    //下一块方块
    public Point[] nextCube;

    //下一块方块的类型
    public int nextCubeType;

    //下一块方块大小
    public int nextCubeSize;

    public  CubesModel(int cubeSize){
        // 初始化方块画笔
        this.cubeSize=cubeSize;
        cubePaint = new Paint();
        cubePaint.setColor(0xff000000);
        cubePaint.setAntiAlias(true);
    }
    /**
     * 增加方块
     */
    public void newCubes() {
        //如果下一块为空
        if (nextCube==null) {
            //生成下一块
            newNextCube();
        }
        //把下一块赋给当前方块
        //当前方块=下一块
        cubes=nextCube;

        //当前方块类型=下一块方块类型
        cubeType=nextCubeType;

        //生产下一块方块
        newNextCube();

    }

    //生产下一块方块
    public void newNextCube(){
        //随机生产一个新的方块
        Random random = new Random();
        nextCubeType = random.nextInt(Config.TYPENUM);
        switch (nextCubeType) {

            // 田
            case 0:
                nextCube = new Point[]{new Point(4, 0), new Point(5, 0), new Point(4, 1), new Point(5, 1)};
                break;

            // 土
            case 1:
                nextCube = new Point[]{new Point(5, 1), new Point(4, 1), new Point(6, 1), new Point(5, 0)};
                break;

            // L
            case 2:
                nextCube = new Point[]{new Point(5, 1), new Point(6, 0), new Point(4, 1), new Point(6, 1)};
                break;

            // 反L
            case 3:
                nextCube = new Point[]{new Point(5, 1), new Point(4, 0), new Point(4, 1), new Point(6, 1)};
                break;

            // z
            case 4:
                nextCube = new Point[]{new Point(4, 1), new Point(5, 0), new Point(4, 2), new Point(5, 1)};
                break;

            // 反z
            case 5:
                nextCube = new Point[]{new Point(4, 1), new Point(4, 0), new Point(5, 1), new Point(5, 2)};
                break;

            // 一
            case 6:
                nextCube = new Point[]{new Point(4, 0), new Point(3, 0), new Point(5, 0),new Point(6, 0) };
                break;
            default:
                break;
        }
    }

    ///绘制下一块预览区
    public void drawNext(Canvas canvas, int width) {
        if (nextCube!=null){
            if (nextCubeSize==0){
                nextCubeSize=width/6;
            }
            for(int i=0;i<nextCube.length;i++){
                canvas.drawRect(
                        (nextCube[i].x-3)*nextCubeSize,
                        (nextCube[i].y+1)*nextCubeSize,
                        (nextCube[i].x-3)*nextCubeSize+nextCubeSize,
                        (nextCube[i].y+1)*nextCubeSize+nextCubeSize,
                        cubePaint);
            }

        }
    }

    /** 移动 */
    public boolean move(int x, int y, com.lazyduke.mytetris2.model.MapsModel mapsModel) {

        for (int i = 0; i < cubes.length; i++) {
            // 边界判定
            if (checkBoundary(cubes[i].x + x, cubes[i].y + y,mapsModel)) {
                return false;
            }
        }

        // 使方块数组的x,y加上偏移量
        for (int i = 0; i < cubes.length; i++) {
            cubes[i].x += x;
            cubes[i].y += y;
        }
        return true;
    }

    /** 旋转 */
    public boolean rotate(com.lazyduke.mytetris2.model.MapsModel mapsModel) {
        // 遍历方块数组 每一个都绕着中心点（数组的第一个元素）顺时针旋转90度
        if (cubeType == 0)
            return false;
        for (int i = 0; i < cubes.length; i++) {
            // 边界判定
            int checkX = -cubes[i].y + cubes[0].y + cubes[0].x;
            int checkY = cubes[i].x - cubes[0].x + cubes[0].y;
            if (checkBoundary(checkX, checkY,mapsModel)) {
                return false;
            }
        }

        for (int i = 0; i < cubes.length; i++) {
            // 旋转算法（笛卡尔公式）
            int checkX = -cubes[i].y + cubes[0].y + cubes[0].x;
            int checkY = cubes[i].x - cubes[0].x + cubes[0].y;
            cubes[i].x = checkX;
            cubes[i].y = checkY;
        }
        return true;
    }

    /**
     * 边界判定 传入x y 判定是否在边界外
     */
    public boolean checkBoundary(int x, int y, com.lazyduke.mytetris2.model.MapsModel mapsModel) {
        return (x < 0 || y < 0 || x >= mapsModel.maps.length || y >= mapsModel.maps[0].length || mapsModel.maps[x][y]);
    }

    // 方块绘制
    public void drawCubes(Canvas canvas) {
        if (cubes != null) {
            for (int i = 0; i < cubes.length; i++) {
                // canvas.drawRect(left, top, right, bottom, paint);
                canvas.drawRect(cubes[i].x * cubeSize, cubes[i].y * cubeSize, cubes[i].x * cubeSize + cubeSize,
                        cubes[i].y * cubeSize + cubeSize, cubePaint);
            }
        }
    }
}