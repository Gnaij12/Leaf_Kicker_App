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
    private int maxSpeed;
    private Bitmap bitmap;
    private int currentFrame = 0, iconWidth, iconHeight,animationDelay = 20;
    private int dX = -5, dY = -5;

    public Person() {
        this(5);
    }

    public Person(int maxSpeed) {
        this(1, 1, 100, 100, maxSpeed);
    }

    public Person(float left, float top, float right, float bottom) {
        this(left, top, right, bottom, 5);
    }

    public Person(float left, float top, float right, float bottom, int maxSpeed) {
        super(left, top, right, bottom);
        this.maxSpeed = maxSpeed;
    }

    public void update(Canvas canvas) {
//        dX = 5;
//        dY = 5;
        int offdX = dX;
        int offdY = dY;
        if (left + dX < 0 || right + dX > canvas.getWidth())//if next step hits boundary
            offdX = 0;
        if (top + dY < 0 || bottom + dY > canvas.getHeight())//if next step hits boundary
            offdY = 0;
        offset(offdX, offdY);//moves dX to the right and dY downwards
        if(animationDelay--<0) {//increment to next sprite image after delay
            currentFrame = ++currentFrame % BMP_COLUMNS;//cycles current image with boundary protection
            animationDelay=20;//arbitrary delay before cycling to next image
        }
    }

    public void draw(Canvas canvas) {
        if (bitmap == null) {//if no bitmap exists draw a red circle
            Paint paint = new Paint();
            paint.setColor(Color.RED);//sets its color
            canvas.drawCircle(centerX(), centerY(), width() / 2, paint);//draws circle
        } else {
            iconWidth = bitmap.getWidth() / BMP_COLUMNS;//calculate width of 1 image
            iconHeight = bitmap.getHeight() / BMP_ROWS; //calculate height of 1 image
            int srcX = currentFrame * iconWidth;       //set x of source rectangle inside of bitmap based on current frame
            int srcY = getAnimationRow() * iconHeight; //set y to row of bitmap based on direction
            Rect src = new Rect(srcX, srcY, srcX + iconWidth, srcY + iconHeight);  //defines the rectangle inside of heroBmp to displayed
            canvas.drawBitmap(bitmap, src, this, null); //draw an image
        }

    }

    private int getAnimationRow() {
        if (Math.abs(dX) > Math.abs(dY)) {         //if magnitude of x is bigger than magnitude y
            if (Math.abs(dX) == dX) return RIGHT;  //if x is positive return row 2 for right
            else return LEFT;                          //if x is negative return row 1 for left
        } else if (Math.abs(dY) == dY) return DOWN;      //if y is positive return row 0 for up
        else return UP;                                 //if y is positive return row 3 for up

    }

    public int getdX() {
        return dX;
    }

    public void setdX(int dX) {
        this.dX = dX;
    }

    public int getdY() {
        return dY;
    }

    public void setdY(int dY) {
        this.dY = dY;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}