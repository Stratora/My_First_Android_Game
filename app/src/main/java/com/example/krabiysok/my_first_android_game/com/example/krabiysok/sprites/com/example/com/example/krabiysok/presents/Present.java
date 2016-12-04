package com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.com.example.krabiysok.presents;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.krabiysok.my_first_android_game.GameScreen;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.Player;

/**
 * Created by KrabiySok on 1/13/2015.
 */
abstract public class Present {
    private static final float RATIO = 0.1f;
    private Bitmap mImage;
    private Point mSize, mPosition;
    private Rect mDst;
    private int mPresentX0, mPresentX1, mPresentY0, mPresentY1, mPlayerX0, mPlayerX1,
            mPlayerY0, mPlayerY1;
    Present(Bitmap image, int x, int y) {
        mImage = image;
        mPosition = new Point(x, y);
        float p = GameScreen.getWindowSize().y * RATIO;
        mSize = new Point((int) ((image.getWidth() * p) /
                image.getHeight()), (int) p);
        mDst = new Rect(x, y, (int) (x + mSize.x * mPosition.y /
                        GameScreen.getWindowSize().y), (int) (y + mSize.y *
                mPosition.y / GameScreen.getWindowSize().y));
    }
    
    public boolean takes(Player player) {
        mPresentX0 = mPosition.x;
        mPresentY0 = mPosition.y;
        mPlayerX0 = player.getPosition().x;
        mPlayerY0 = player.getPosition().y;
        mPresentX1 = mPresentX0 + mSize.x;
        mPresentY1 = mPresentY0 + mSize.y;
        mPlayerX1 = mPlayerX0 + player.getSpriteResolution().x;
        mPlayerY1 = mPlayerY0 + player.getSpriteResolution().y;
        if ((mPlayerX0 <= mPresentX0 && mPresentX0 <= mPlayerX1 &&
                mPlayerY0 <= mPresentY0 && mPresentY0 <= mPlayerY1) ||
                (mPlayerX0 <= mPresentX1 && mPresentX1 <= mPlayerX1 &&
                        mPlayerY0 <= mPresentY1 && mPresentY1 <= mPlayerY1)) {
            presentForPlayer(player);
            return true;
        }
        return false;
    }

    abstract protected void presentForPlayer(Player player);
    
    public void draw(Canvas canva) {
        canva.drawBitmap(mImage, null, mDst, null);
    }
}
