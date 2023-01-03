package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

class Person extends RectF {
    private static final int BMP_COLUMNS = 4;
    private static final int BMP_ROWS = 4;
    private static final int DOWN = 0, LEFT = 1, RIGHT = 2, UP = 3;
    private double maxSpeed;
    private Bitmap bitmap;
    private int currentFrame = 0, iconWidth, iconHeight,animationDelay = 20;
    private double dX = 0.0, dY = 0.0;
    private int previous = 3;
    private float tempLeft,tempRight,tempTop;
    private double tempMaxSpeed;

    public Person() {
        this(400.0);
    }

    public Person(double speedPixels) {
        this(1, 1, 100, 100, speedPixels);
    }

    public Person(float left, float top, float right, float bottom) {
        this(left, top, right, bottom, 400.0);
    }

    public Person(float left, float top, float right, float bottom, double speedPixels) {
        super(left, top, right, bottom);
        tempLeft = left;
        tempRight = right;
        tempTop = top;
        this.maxSpeed = speedPixels / DrawView.FPS;
    }

    public void update(Canvas canvas, Joystick joystick) {
        tempMaxSpeed = maxSpeed;
        double ratio = bottom/canvas.getHeight();
        ratio = (ratio * (1-1.0/3)) + 1.0/3;
        tempMaxSpeed *= ratio;

        dX = joystick.getActuatorX() * tempMaxSpeed;
        dY = joystick.getActuatorY() * tempMaxSpeed;
        if (!(dX == 0.0 && dY == 0.0)) {
            float offdX = (float)dX;
            float offdY = (float)dY;
            if (tempLeft + dX < 0 && dX < 0)//if next step hits boundary
                offdX = 0;
            else if (tempRight + dX > canvas.getWidth() && dX > 0) {
                offdX = 0;
            }
            if (tempTop + dY < 0 || bottom + dY > canvas.getHeight())//if next step hits boundary
                offdY = 0;
            offset(offdX, offdY);//moves dX to the right and dY downwards
            if(animationDelay--<0) {//increment to next sprite image after delay
                currentFrame = ++currentFrame % BMP_COLUMNS;//cycles current image with boundary protection
                animationDelay=20;//arbitrary delay before cycling to next image
            }
        }
        //Scale size based on place
        tempLeft = left;
        tempRight = right;
        tempTop = top;
        left = (float) (left + (right-left) * (1-ratio)/2);
        right = (float) (right - (right-left) * (1-ratio)/2);
        top = (float) (top + (bottom-top) * (1-ratio));

    }

    public void draw(Canvas canvas) {
        if (bitmap == null) {//if no bitmap exists draw a red circle
            Paint paint = new Paint();
            paint.setColor(Color.RED);//sets its color
            canvas.drawCircle(centerX(), centerY(), width() / 2, paint);//draws circle
        } else {
            if (dX == 0.0 && dY == 0.0) {
                iconWidth = bitmap.getWidth() / BMP_COLUMNS;//calculate width of 1 image
                iconHeight = bitmap.getHeight() / BMP_ROWS; //calculate height of 1 image
                int srcX = currentFrame * iconWidth;       //set x of source rectangle inside of bitmap based on current frame
                int srcY = previous * iconHeight;
                Rect src = new Rect(srcX, srcY, srcX + iconWidth, srcY + iconHeight);  //defines the rectangle inside of heroBmp to displayed
                canvas.drawBitmap(bitmap, src, this, null); //draw an image
            }else {
                iconWidth = bitmap.getWidth() / BMP_COLUMNS;//calculate width of 1 image
                iconHeight = bitmap.getHeight() / BMP_ROWS; //calculate height of 1 image
                int srcX = currentFrame * iconWidth;       //set x of source rectangle inside of bitmap based on current frame
                previous = getAnimationRow();
                int srcY = previous * iconHeight; //set y to row of bitmap based on direction
                Rect src = new Rect(srcX, srcY, srcX + iconWidth, srcY + iconHeight);  //defines the rectangle inside of heroBmp to displayed
                canvas.drawBitmap(bitmap, src, this, null); //draw an image

            }
        }
        float temp2Left = left;
        float temp2Right = right;
        float temp2Top = top;
        left = tempLeft;
        right = tempRight;
        top = tempTop;
        tempLeft = temp2Left;
        tempRight = temp2Right;
        tempTop = temp2Top;


    }

    private int getAnimationRow() {
        if (Math.abs(dX) > Math.abs(dY)) {         //if magnitude of x is bigger than magnitude y
            if (Math.abs(dX) == dX) return RIGHT;  //if x is positive return row 2 for right
            else return LEFT;                          //if x is negative return row 1 for left
        } else if (Math.abs(dY) == dY) return DOWN;      //if y is positive return row 0 for up
        else return UP;                                 //if y is positive return row 3 for up

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public double getdX() {
        return dX;
    }
    public double getdY() {
        return dY;
    }
}