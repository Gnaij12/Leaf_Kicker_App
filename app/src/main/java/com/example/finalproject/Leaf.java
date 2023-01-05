package com.example.finalproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Leaf extends RectF {
    private float dx;
    private float dy;
    private int color;
    private final int bottree;
    private final float xMovePastSpeed = 6;
    private final float maxYSpeed = 5;
    private final float gravity = 3;
    private final float xGravity = 1.5f;
    private boolean kickedUp, kickedDown, kickedRight, kickedLeft = false;
    private boolean onLeft,onRight = false;
    public Leaf(float left, float top, float right, float bottom, float dx, float dy, int color,int bottree) {
        super(left, top, right, bottom);
        this.dx = dx;
        this.dy = dy;
        this.color = color;
        this.bottree = bottree;
    }

    public Leaf(float left, float top, float right, float bottom) {
        this(left, top, right, bottom,0,5, Color.MAGENTA,-1);
    }

    public Leaf(int dx, int dy, int color) {
        this(1,1,111,111,dx,dy,color,-1);
    }

    public Leaf() {
        this(0,5,Color.GREEN);
    }

    public void update() {
        if (!getKicked()) {
            if (onLeft && onRight) {
                dx = 0;
                dy = 0;
            }
            if (!onLeft && !onRight) {
                dx = 0;
            }
            if (centerY() + width()/2 + dy <=bottree) {
                offset(dx,dy);
            }
        }else {
            offset(dx,dy);
        }

        if (kickedUp && dy + gravity > 0) {
            kickedUp = false;
        }
        if (kickedDown && dy - gravity < maxYSpeed) {
            kickedDown = false;
        }
        if (kickedRight && dx -xGravity < 0) {
            kickedRight = false;
        }
        if (kickedLeft && dx + xGravity > 0) {
            kickedLeft = false;
        }
        if (dx-xGravity > 0) {
            dx-=xGravity;
        }
        if (dx+xGravity < 0) {
            dx+=xGravity;
        }
        if (dy + gravity <= maxYSpeed) {
            dy += gravity;
        }else if (dy - gravity >= maxYSpeed) {
            dy-=gravity;
        }
        //ToDo: implement downward kicking
        onLeft = onRight = false;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle(centerX(),centerY(),width()/2,paint);
    }
    public boolean onTop(Leaf leaf) {
        double dis = Math.sqrt(Math.pow(centerX()-leaf.centerX(),2)*2 + Math.pow(centerY()-leaf.centerY(),2)*6);
        if (centerY() < leaf.centerY() && (dis < Math.abs(width()/2 -leaf.width()/2) || dis < width()/2 + leaf.width()/2)) {
            if (centerX() >= leaf.centerX()) {
                dx = xMovePastSpeed;
                onLeft = true;
            }else {
                dx = -xMovePastSpeed;
                onRight = true;
            }
            return true;
        }
        return false;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setKickedUp(boolean kickedUp) {
        this.kickedUp = kickedUp;
    }
    public void setKickedDown(boolean kickedDown) {
        this.kickedDown = kickedDown;
    }
    public void setKickedLeft(boolean kickedLeft) {
        this.kickedLeft = kickedLeft;
    }
    public void setKickedRight(boolean kickedRight) {
        this.kickedRight = kickedRight;
    }
    public boolean getKickedUp() {
        return kickedUp;
    }
    public boolean getKickedDown() {
        return kickedDown;
    }
    public boolean getKickedLeft() {
        return kickedLeft;
    }
    public boolean getKickedRight() {
        return kickedRight;
    }
    public boolean getKicked() {
        return kickedUp || kickedDown || kickedLeft || kickedRight;
    }

}
