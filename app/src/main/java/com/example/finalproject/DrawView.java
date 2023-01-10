package com.example.finalproject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.Nullable;

public class DrawView extends SurfaceView implements Runnable{ //Maybe have leaves falling from the trees and you can control a character to move them around?
    private Tree tree1;
    private Tree tree2;
    private Tree tree3;
    private Tree tree4;
    private Path path;
    private Point[] points;
    private int height;
    private int width;
    private int delay = 2;
    private int count;
    Paint pathPaint;
    private boolean mRunning;
    private Thread mGameThread = null;
    private SurfaceHolder mSurfaceHolder;
    private Context mContext;
    private Person person;
    private Joystick joystick;
    private Paint grass;
    private int mul = 255;
    private int mulChange = -1;
    private int add = 0;
    private int addChange = 0;
    private int backgroundDelay = 5;
    private int backgroundCount = 0;
    private LightingColorFilter lcf;
    public static final int FPS = 60;

//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//
//    }
    //TODO: Add new features

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        height = getHeight();
        width = getWidth();

        tree2 = new Tree(700,900,100*9/10,400*9/10,25*9/10, 150*9/10);
        tree1 = new Tree(200,1200,100,400,25, 150);
        tree3 = new Tree(400,500,100*7/10,400*7/10,25*7/10, 150*7/10);
        tree4 = new Tree(800,100,100*4/10,400*4/10,25*4/10, 150*4/10);
        pathPaint = new Paint();
//        pathPaint.setColor(Color.rgb(240,222,192));
        path = new Path();
        count = 0;
        pathPaint.setShader(new LinearGradient(width/3, height, width/3+150, 0, Color.rgb(240,222,192), Color.rgb(155,118,83), Shader.TileMode.MIRROR));
        pathPaint.setDither(true);
        points = new Point[]{new Point(width/3+150,height),new Point(2*width/3+50,0),new Point(2*width/3,0)};
        path.moveTo(width/3,height);
        for (Point point: points) {
            path.lineTo(point.x,point.y);
        }
        person = new Person(width/2-100,height-200,width/2+100,height,800.0);
        person.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.classywalk));
        joystick = new Joystick(width-200,height-150,100,50);
        grass = new Paint();
        grass.setColor(Color.rgb(0,136,0));
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = getContext();
        mSurfaceHolder = getHolder();
    }

    private void drawMe(Canvas canvas) {
        lcf = new LightingColorFilter(Color.rgb(mul,mul,mul),Color.rgb(add/3,add/3,0));
        pathPaint.setColorFilter(lcf);
        canvas.drawPath(path,pathPaint);
        if (count >= delay) {
            tree4.update();
            tree3.update();
            tree2.update();
            tree1.update();
            count = 0;
        }
        joystick.update();
        joystick.draw(canvas);
        person.update(canvas,joystick);

        tree4.updateLeaves(person);
        tree3.updateLeaves(person);
        tree2.updateLeaves(person);
        tree1.updateLeaves(person);
        person.draw(canvas);

        tree4.draw(canvas);
        tree3.draw(canvas);
        tree2.draw(canvas);
        tree1.draw(canvas);
        count++;



//        invalidate();
    }

    @Override
    public void run() {
        Canvas canvas;
        long frameStart;
        long frameTime;
        while (mRunning) {
            if (mSurfaceHolder == null) {return;}
            if (mSurfaceHolder.getSurface().isValid()) {
                frameStart = System.nanoTime();
                canvas = mSurfaceHolder.lockCanvas();
                if (canvas != null) {
                    canvas.save();
                    lcf = new LightingColorFilter(Color.rgb(mul,mul,mul),Color.rgb(add,0,0));
                    grass.setColorFilter(lcf);
                    canvas.drawPaint(grass);
                    if (backgroundCount == backgroundDelay) {
                        if (mul == 224) {
                            mulChange = 1;
                        } else if (mul == 235) {
                            mulChange = -1;
                        }
                        mul += mulChange;
                        if ((mul == 185 && mul + mulChange == 184) || (mul == 145 && mul + mulChange == 146)) {
                            addChange = 5;
                        }else if (mul == 165) {
                            addChange = -5;
                        }
                        if ((mul == 185 && mul + mulChange == 186) || (mul == 145 && mul + mulChange == 144)) {
                            addChange = 0;
                        }
                        add += addChange;
                        System.out.println(mul);
                        System.out.println(add);
                        backgroundCount = 0;
                    }
                    backgroundCount++;
                    try {
                        drawMe(canvas);
                    }finally {
                        canvas.restore();
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                frameTime = (System.nanoTime()-frameStart)/1000000;
                if (frameTime < (1000/FPS)) {
                    try {
                        Thread.sleep((int)(1000/FPS-frameTime));
                    }catch (InterruptedException e) {

                    }
                }
            }
        }
    }
    public void pause() {
        mRunning = false;
        try {
            mGameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        mRunning = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (joystick.isPressed((double)event.getX(),(double)event.getY())) {
                    joystick.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()) {
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
                return true;
        }
        return super.onTouchEvent(event);
    }
}
