package com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.krabiysok.my_first_android_game.GameScreen;

/**
 * Created by KrabiySok on 12/30/2014.
 */
public class GeneralAnimation {
    public static final float FLOAT_PI = (float) Math.PI, FLOAT_PI2 = FLOAT_PI * 2,
            ANGLE45 = FLOAT_PI / 4, ANGLE135 = FLOAT_PI - ANGLE45,
            ANGLE225 = FLOAT_PI + ANGLE45, ANGLE315 = FLOAT_PI + 2 * ANGLE45,
            PI360 = FLOAT_PI * 2, PI90 = FLOAT_PI / 2, PI270 = PI90 + FLOAT_PI;
    protected int mRows, mColumns, mNewXPosition, mNewYPosition, mMaxXAnimatPos, mMaxYAnimatPos,
            mMinXAnimatPos, mMinYAnimatPos;
    protected Bitmap mSprite, mBulletSprite;
    protected Rect mSrc, mDst;
    protected Point mBmpRezolution, mSpriteResolution, mPosition, mSpriteNormalResolution;
    private float mAngle;
    private byte mForward, mBack, mLeft, mRight;
    protected boolean mResultOfAnimMove;
    protected Matrix mMatrix;

    protected GeneralAnimation(int x, int y, int rows, int columns, Bitmap sprite,
                               double spriteRatioHieght) {
        mRows = rows;
        mColumns = columns;
        mSprite = sprite;
        mBmpRezolution = new Point(sprite.getWidth() / columns,
                sprite.getHeight() / rows);
        mPosition = new Point(x, y);
        mSpriteNormalResolution = new Point();
        // Gets resolution of sprite on game screen
        mSpriteNormalResolution.y = (int) (GameScreen.getWindowSize().y * spriteRatioHieght);
        mSpriteNormalResolution.x =  mBmpRezolution.x *
                this.mSpriteNormalResolution.y / mBmpRezolution.y;
        mSrc = new Rect(0, 0, mBmpRezolution.x, mBmpRezolution.y);
        mDst = new Rect(mPosition.x, mPosition.y, mPosition.x + mSpriteNormalResolution.x,
                mPosition.y + mSpriteNormalResolution.y);
        mMatrix = new Matrix();
        mForward = mBack = mLeft = mRight = 0;
        mMaxXAnimatPos = GameScreen.getWindowSize().x - mSpriteNormalResolution.x;
        mMaxYAnimatPos = GameScreen.sGameScreenHeightMax - mSpriteNormalResolution.y;
        mMinYAnimatPos = GameScreen.sGameScreenHeightMin;
        mMinXAnimatPos = 0;
        mSpriteResolution = new Point(mSpriteNormalResolution);
    }

    public boolean drawMove(Canvas canvas, Float moveAngle, int distance) {
        mResultOfAnimMove = true;
        if (moveAngle != null) {
            if (moveAngle > FLOAT_PI) mAngle = PI360 - moveAngle;
            else mAngle = moveAngle;
            if (mAngle > PI90)
                mAngle = FLOAT_PI - mAngle;
            mNewYPosition = (int) (distance * Math.cos(mAngle));
            mNewXPosition = (int) (distance * Math.sin(mAngle));

            if (moveAngle > ANGLE315 || moveAngle <= ANGLE45) {
                mPosition.y -= mNewYPosition;
                mPosition.x += moveAngle > ANGLE315 ? -mNewXPosition : mNewXPosition;
                moveBack();
            }
            else if (moveAngle > ANGLE45 && moveAngle <= ANGLE135) {
                    mPosition.x += mNewXPosition;
                    mPosition.y += moveAngle > PI90 ? mNewYPosition : -mNewYPosition;
                    moveRight();
                }
                else if (moveAngle > ANGLE135 && moveAngle <= ANGLE225) {
                        mPosition.y += mNewYPosition;
                        mPosition.x += moveAngle > FLOAT_PI ? -mNewXPosition : mNewXPosition;
                        moveForward();
                    }
                    else if (moveAngle > ANGLE225 && moveAngle <= ANGLE315) {
                        mPosition.x -= mNewXPosition;
                        mPosition.y += moveAngle > PI270 ? -mNewYPosition : mNewYPosition;
                        moveLeft();
                    }
        } else {
            mForward = 0;
            moveForward();
        }
        // Protect from going beyond game screen
        mMaxXAnimatPos = GameScreen.getWindowSize().x - this.mSpriteResolution.x;
        if (mPosition.x < mMinXAnimatPos) {
            mPosition.x = mMinXAnimatPos;
            mResultOfAnimMove = false;
        }
        if (mPosition.x > mMaxXAnimatPos) {
            mPosition.x = mMaxXAnimatPos;
            mResultOfAnimMove = false;
        }
        if (mPosition.y > mMaxYAnimatPos) {
            mPosition.y = mMaxYAnimatPos;
            mResultOfAnimMove = false;
        }
        if (mPosition.y < mMinYAnimatPos) {
            mPosition.y = mMinYAnimatPos;
            mResultOfAnimMove = false;
        }
        mSpriteResolution.x = (int) (mSpriteNormalResolution.x *
                mPosition.y / GameScreen.getWindowSize().y);
        mSpriteResolution.y = (int) (mSpriteNormalResolution.y *
                mPosition.y / GameScreen.getWindowSize().y);
        mDst.set(mPosition.x, mPosition.y, mPosition.x + mSpriteResolution.x,
                mPosition.y + mSpriteResolution.y);
        canvas.drawBitmap(mSprite, mSrc, this.mDst, null);
        return mResultOfAnimMove;
    }

   private void moveForward() {
        mBack = mLeft = mRight = 0;
        if ( mForward >= mColumns) {
            mForward = 0;
        }
        mSrc.set(mForward * mBmpRezolution.x, 0,
                (mForward + 1) * mBmpRezolution.x, mBmpRezolution.y);
        mForward++;
    }

    private void moveLeft() {
        mBack = mForward = mRight = 0;
        if ( mLeft >= mColumns) {
            mLeft = 0;
        }
        mSrc.set(mLeft * mBmpRezolution.x, mBmpRezolution.y,
                (mLeft + 1) * mBmpRezolution.x, mBmpRezolution.y * 2);
        mLeft++;
    }

    private void moveRight() {
        mBack = mForward = mLeft = 0;
        if ( mRight >= mColumns) {
            mRight = 0;
        }
        mSrc.set(mRight * mBmpRezolution.x, mBmpRezolution.y * 2,
                (mRight + 1) * mBmpRezolution.x, mBmpRezolution.y * 3);
        mRight++;
    }

    private void moveBack() {
        mRight = mForward = mLeft = 0;
        if ( mBack >= mColumns) {
            mBack = 0;
        }
        mSrc.set(mBack * mBmpRezolution.x, mBmpRezolution.y * 3,
                (mBack + 1) * mBmpRezolution.x, mBmpRezolution.y * 4);
        mBack++;
    }

    public Point getPosition() {
        return new Point(mPosition);
    }

    public Point getSpriteResolution() {
        return new Point(mSpriteResolution);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneralAnimation that = (GeneralAnimation) o;

        if (Double.compare(that.mAngle, mAngle) != 0) return false;
        if (mBack != that.mBack) return false;
        if (mColumns != that.mColumns) return false;
        if (mForward != that.mForward) return false;
        if (mLeft != that.mLeft) return false;
        if (mResultOfAnimMove != that.mResultOfAnimMove) return false;
        if (mRight != that.mRight) return false;
        if (mRows != that.mRows) return false;
        if (!mBmpRezolution.equals(that.mBmpRezolution)) return false;
        if (!mBulletSprite.equals(that.mBulletSprite)) return false;
        if (!mDst.equals(that.mDst)) return false;
        if (mMatrix != null ? !mMatrix.equals(that.mMatrix) : that.mMatrix != null) return false;
        if (!mSprite.equals(that.mSprite)) return false;
        if (mSpriteNormalResolution != null ? !mSpriteNormalResolution.equals(that.mSpriteNormalResolution) : that.mSpriteNormalResolution != null)
            return false;
        if (mSpriteResolution != null ? !mSpriteResolution.equals(that.mSpriteResolution) : that.mSpriteResolution != null)
            return false;
        if (!mSrc.equals(that.mSrc)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mRows;
        result = 31 * result + mColumns;
        result = 31 * result + mSprite.hashCode();
        result = 31 * result + mBulletSprite.hashCode();
        result = 31 * result + mSrc.hashCode();
        result = 31 * result + mDst.hashCode();
        result = 31 * result + mBmpRezolution.hashCode();
        result = 31 * result + (mSpriteResolution != null ? mSpriteResolution.hashCode() : 0);
        result = 31 * result + (mSpriteNormalResolution != null ? mSpriteNormalResolution.hashCode() : 0);
        temp = Double.doubleToLongBits(mAngle);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) mForward;
        result = 31 * result + (int) mBack;
        result = 31 * result + (int) mLeft;
        result = 31 * result + (int) mRight;
        result = 31 * result + (mResultOfAnimMove ? 1 : 0);
        result = 31 * result + (mMatrix != null ? mMatrix.hashCode() : 0);
        return result;
    }
}
