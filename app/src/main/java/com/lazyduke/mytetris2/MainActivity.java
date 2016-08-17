package com.lazyduke.mytetris2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lazyduke.mytetris2.control.GameControl;

public class MainActivity extends Activity implements OnClickListener, OnLongClickListener {

    //view没有变量、数据
    // 游戏区域控件
    View gamePanel;

    //当前分数控件
    public TextView currSco;

    //最高分数控件
    public TextView bestSco;

    //游戏控制器
    GameControl gameControl;

    //下一块预览区
    public View nextBlock;

    //实例化handler
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String type=(String )msg.obj;
            if(type==null)
                return;
            if(type.equals("invalidate")){
                gamePanel.invalidate();
                nextBlock.invalidate();
                //刷新分数
                gameControl.scoreModel.showCurrSco(currSco);
                gameControl.scoreModel.showBestSco(MainActivity.this,bestSco);
            }
            else if (type.equals("pause"))
                ((Button)findViewById(R.id.btnPause)).setText("Pause");
            else if(type.equals("continue"))
                ((Button)findViewById(R.id.btnPause)).setText("Continue");


        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //实例化游戏控制器
        gameControl=new GameControl(handler,this);
        initView();
        initListener();

        //加载本地储存数据
        gameControl.scoreModel.showBestSco(this,bestSco);
    }

    /** 初始化视图 */
    private void initView() {


        // 2.实例化游戏区域
        gamePanel = new View(this) {
            // 重写游戏区域绘制
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);

                //绘制
                gameControl.drawGame(canvas);

            }

        };
        // 3.设置游戏区域大小
        gamePanel.setLayoutParams(new LayoutParams(Config.XWIDTH, Config.YHEIGHT));

        // 设置背景颜色
        gamePanel.setBackgroundColor(0x10000000);

        // 4.添加到父容器
        FrameLayout layoutGame = (FrameLayout) findViewById(R.id.layoutGame);
        layoutGame.setPadding(Config.PADDING,Config.PADDING,Config.PADDING,Config.PADDING);
        layoutGame.addView(gamePanel);

        //设置信息区域
        LinearLayout layoutInfo = (LinearLayout) findViewById(R.id.layoutInfo);
        layoutInfo.setPadding(0,Config.PADDING,Config.PADDING,Config.PADDING);


        //实例化下一快预览区域
        nextBlock=new View(this){
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                gameControl.drawNext(canvas,nextBlock.getWidth());
            }
        };

        //设置大小
        nextBlock.setLayoutParams(new LayoutParams(-1,dp2px(this,160)));

        //设置背景颜色
        nextBlock.setBackgroundColor(0x20000000);

        //添加进父类
        FrameLayout layoutNext=(FrameLayout)findViewById(R.id.layoutNext);
        layoutNext.addView(nextBlock);

        currSco=(TextView)findViewById(R.id.currSco);
        bestSco=(TextView)findViewById(R.id.bestSco);
    }

    /** 捕捉点击事件 */
    @Override
    public void onClick(View v) {
        gameControl.onClick(v.getId());
        // 刷新视图
        gamePanel.invalidate();
        nextBlock.invalidate();
    }

    /** 捕捉长点击事件 */
    @Override
    public boolean onLongClick(View v) {
        return (gameControl.onLongClick());
    }

    /** 初始化监听器 */
    private void initListener() {

        findViewById(R.id.btnLeft).setOnClickListener(this);

        findViewById(R.id.btnDown).setOnClickListener(this);
        findViewById(R.id.btnDown).setOnLongClickListener(this);

        findViewById(R.id.btnRight).setOnClickListener(this);

        findViewById(R.id.btnRotate).setOnClickListener(this);

        findViewById(R.id.btnStart).setOnClickListener(this);

        findViewById(R.id.btnPause).setOnClickListener(this);
    }

    /**按两下back退出*/
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - Config.EXITTIME) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                Config.EXITTIME = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    //将dp转换为px
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
