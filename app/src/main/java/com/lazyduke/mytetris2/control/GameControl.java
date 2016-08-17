package com.lazyduke.mytetris2.control;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.lazyduke.mytetris2.Config;
import com.lazyduke.mytetris2.R;
import com.lazyduke.mytetris2.model.CubesModel;
import com.lazyduke.mytetris2.model.MapsModel;
import com.lazyduke.mytetris2.model.ScoreModel;

/**
 * Created by Lenovo on 2016/8/16.
 */
public class GameControl {
    //context
    Context context;
    //handler
    Handler handler;

    // 暂停状态
    public boolean isPause = false;

    // 结束状态
    public boolean isOver = false;

    //地图模型
    MapsModel mapsModel;

    //方块模型
    CubesModel cubesModel;

    //分数模型
    public ScoreModel scoreModel;

    // 自动下落线程
    public Thread downThread;



    public GameControl(Handler handler,Context context) {
        this.handler=handler;
        this.context=context;
        initData();
    }

    /** 初始化数据 */
    private void initData() {

        // 获得屏幕宽度
        int width = getScreenWidth(context);

        int cubeSize;

        // 设置游戏区域宽度=屏幕宽度*2/3
        Config.XWIDTH= width * 2 / 3;

        // 游戏区域高度=宽度*2
        Config.YHEIGHT = Config.XWIDTH * 2;

        // 初始化方块大小=游戏区域宽度/10
        cubeSize = Config.XWIDTH / Config.MAPX;

        //初始化间距
        Config.PADDING=width/25;


        // 实例化地图模型
        mapsModel = new MapsModel(Config.XWIDTH,Config.YHEIGHT,cubeSize);

        //实例化方块
        cubesModel = new CubesModel(cubeSize);

        //实例化分数模型

        scoreModel=new ScoreModel();


    }

    // 开始游戏
    private void startGame() {
        if (downThread == null) {
            downThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) { // 休眠
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 判定是否处于结束状态 // 判定是否处于暂停状态
                        if (isPause || isOver) {
                            // 继续循环
                            continue;
                        }
                        // 下落
                        fallDown();
                        // 通知主线程刷新view
                        Message msg=new Message();
                        msg.obj="invalidate";
                        handler.sendMessage(msg);
                    }
                }
            };
            downThread.start();
        }
        //清空视图
        mapsModel.cleanMaps();
        cubesModel.newCubes();
        //重置游戏状态
        isOver=false;
        setPause(true);

    }


    /** 下落 */
    public boolean fallDown() {
        // 1.移动成功，不做处理
        if (cubesModel.move(0, 1,mapsModel))
            return true;
        // 2.移动失败，堆积处理
        accumulate();
        // 3.消行处理
        int lines= mapsModel.DelLine();
        //4.加分处理
        scoreModel.addScore(lines);
        // 5.生成新的方块
        cubesModel.newCubes();
        // 6.游戏结束判定
        isOver = checkOver();
        return false;

    }

    /** 结束判定 */
    public boolean checkOver() {
        for (int i = 0; i < cubesModel.cubes.length; i++) {
            if (mapsModel.maps[cubesModel.cubes[i].x][cubesModel.cubes[i].y]){
                scoreModel.score=0;
                return true;
            }

        }
        return false;
    }

    /** 堆积 */
    public void accumulate() {
        for (int i = 0; i <cubesModel.cubes.length; i++) {
            mapsModel.maps[cubesModel.cubes[i].x][cubesModel.cubes[i].y] = true;
        }
    }

    // 暂停游戏
    private void setPause(boolean reset) {
        if (reset) {
            isPause = true;
        }
        if (isPause){
            isPause=false;
            Message msg=new Message();
            msg.obj="pause";
            handler.sendMessage(msg);
        }
        else {
            isPause=true;
            Message msg=new Message();
            msg.obj="continue";
            handler.sendMessage(msg);
        }

    }

    //绘制游戏区
    public void drawGame(Canvas canvas){

        // 视图绘制
        mapsModel.drawMaps(canvas);

        // 方块绘制
        cubesModel.drawCubes(canvas);

        //视图辅助线绘制
        mapsModel.drawLine(canvas);

        //绘制暂停状态
        mapsModel.drawPause(canvas,isPause);
    }

    //绘制下一块预览区
    public void drawNext(Canvas canvas, int width) {
        cubesModel.drawNext(canvas,width);
    }

    //点击事件
    public void onClick(int id) {
        switch (id) {

            // 左
            case R.id.btnLeft:
                if (isPause || isOver)
                    return;
                cubesModel.move(-1, 0,mapsModel);
                break;

            // 右
            case R.id.btnRight:
                if (isPause || isOver)
                    return;
                cubesModel.move(1, 0,mapsModel);
                break;

            // 旋转
            case R.id.btnRotate:
                if (isPause || isOver)
                    return;
                cubesModel.rotate(mapsModel);
                break;

            // 下
            case R.id.btnDown:
                if (isPause || isOver)
                    return;
                fallDown();
                break;

            // 开始
            case R.id.btnStart:
                startGame();
                scoreModel.score=0;
                break;

            // 暂停
            case R.id.btnPause:
                if (isOver){
                    return;
                }
                else {
                    setPause(false);
                }
                break;
            default:
                break;
        }
    }

    //长点击事件
    public boolean onLongClick() {
        while (true) {
            if (!fallDown()) {
                break;
            }
        }
        return false;
    }

    /** 获得屏幕宽度 */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
