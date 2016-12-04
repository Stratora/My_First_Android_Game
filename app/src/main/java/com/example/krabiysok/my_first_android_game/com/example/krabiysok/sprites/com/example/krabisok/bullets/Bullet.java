package com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabisok.bullets;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.krabiysok.my_first_android_game.GameScreen;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.GeneralAnimation;

/**
 * Created by KrabiySok on 1/11/2015.
 */
public class Bullet extends GeneralAnimation {
    private static final float ANGLES_IN_RADIAN = 57.295779513082320876798154814105f;
    private static final int BEYOND = 20; // Bullet going beyond game screen
    private int mDamage, mSpeed;
    private float mAngle, mMoveAngle;
    private GeneralAnimation mBulletBelongs;

    Bullet(int x, int y, int rows, int columns, Bitmap sprite, float spriteRatioHieght,
           int damage, int speed, float angle, GeneralAnimation bulletBelongs) {
        super(x, y, rows, columns, sprite, spriteRatioHieght);
        mDamage = damage;
        mSpeed = speed;
        mMoveAngle = angle;
        mBulletBelongs = bulletBelongs;
    }

    public int getDamage() {
        return mDamage;
    }

    public GeneralAnimation getBulletBelongs() {
        return mBulletBelongs;
    }

    // No mBullet animation
    // Rotate mBullet from last raw.
    public boolean drawMove(Canvas canva) {
        mResultOfAnimMove = true;
        if (mMoveAngle > Math.PI) mAngle = (float) (PI360 - mMoveAngle);
        else mAngle = mMoveAngle;
        if (mAngle > PI90) mAngle = (float) (Math.PI - mAngle);
        mNewYPosition = (int) (mSpeed * Math.cos(mAngle));
        mNewXPosition = (int) (mSpeed * Math.sin(mAngle));

        if (mMoveAngle > ANGLE315 || mMoveAngle <= ANGLE45) {
            mPosition.y -= mNewYPosition;
            mPosition.x += mMoveAngle > ANGLE315 ? -mNewXPosition : mNewXPosition;
        }
        else if (mMoveAngle > ANGLE45 && mMoveAngle <= ANGLE135) {
            mPosition.x += mNewXPosition;
            mPosition.y += mMoveAngle > PI90 ? mNewYPosition : -mNewYPosition;
        }
        else if (mMoveAngle > ANGLE135 && mMoveAngle <= ANGLE225) {
            mPosition.y += mNewYPosition;
            mPosition.x += mMoveAngle > Math.PI ? -mNewXPosition : mNewXPosition;
        }
        else if (mMoveAngle > ANGLE225 && mMoveAngle <= ANGLE315) {
            mPosition.x -= mNewXPosition;
            mPosition.y += mMoveAngle > PI270 ? -mNewYPosition : mNewYPosition;
        }

        // Protect from going beyond game screen
        mMaxXAnimatPos = GameScreen.getWindowSize().x - this.mSpriteResolution.x;
        if (mPosition.x < mMinXAnimatPos - BEYOND || mPosition.x > mMaxXAnimatPos + BEYOND ||
                mPosition.y > GameScreen.getWindowSize().y + BEYOND ||
                mPosition.y < mMinYAnimatPos) {
            mResultOfAnimMove = false;
        }
        mSpriteResolution.x = mSpriteNormalResolution.x *
                mPosition.y / GameScreen.getWindowSize().y;
        mSpriteResolution.y = mSpriteNormalResolution.y *
                mPosition.y / GameScreen.getWindowSize().y;
        mDst.set(mPosition.x, mPosition.y, mPosition.x + mSpriteResolution.x,
                mPosition.y + mSpriteResolution.y);

        mMatrix.setTranslate(mDst.centerX(), mDst.centerY());
        mMatrix.preRotate(mMoveAngle * ANGLES_IN_RADIAN);
        // width and height must be > 0. Need to fix. Problem in mMatrix.
        try {
            mBulletSprite = Bitmap.createBitmap(mSprite, 0, mBmpRezolution.y * (mRows - 1),
                    mBmpRezolution.x, mBmpRezolution.y, mMatrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mBulletSprite != null)
            canva.drawBitmap(mBulletSprite, mSrc, mDst, null);
        return mResultOfAnimMove;
    }
}
