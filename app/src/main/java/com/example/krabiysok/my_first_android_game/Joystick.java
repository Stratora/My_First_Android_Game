package com.example.krabiysok.my_first_android_game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.GeneralAnimation;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.Player;

/**
 * Created by KrabiySok on 1/8/2015.
 */
public class Joystick  {
    private LeftStick mLeftStick;
    private Stick mRightStick;
    private SurfaceView mRightStickSurfaceV, mLeftStickSurfaceV, mAimFieldSrfaceV;

    private class LeftStick extends Stick {
        private SurfaceView mAimField;
        private Paint mPaintAim;
        private Player mPlayer;
        private float mAimX, mAimY, mRAim, mRAimDistance, mAimAngle;
        private Float mNewAimAngle;
        private Canvas mAimFieldCanvas;

        LeftStick(SurfaceView stickSurfaceView, SurfaceView aimField) {
            super(stickSurfaceView);
            mAimField = aimField;
            aimField.setZOrderOnTop(true);    // necessary
            aimField.getHolder().setFormat(PixelFormat.TRANSPARENT);
            mPaintAim = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaintAim.setColor(Color.RED);
            mPaintAim.setAlpha(150);
            mPaintAim.setStyle(Paint.Style.STROKE);
            mPaintAim.setStrokeWidth(GameScreen.getWindowSize().y / 100);
            mRAimDistance = (int) (GameScreen.getWindowSize().y / 3);
            mRAim = (int) (GameScreen.getWindowSize().y / 70);
            drawAim(GameScreen.getWindowSize().x / 2,
                    GameScreen.getWindowSize().y / 2 + mRAimDistance);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event != null && event.getAction() != MotionEvent.ACTION_UP &&
                    event.getAction() != MotionEvent.ACTION_CANCEL) {
                mXDotStick = event.getX();
                mYDotStick = event.getY();
            } else mXDotStick = mYDotStick = null;
            Integer stickInField = stickInField();
            Canvas canvas = mStickSurfaceView.getHolder().lockCanvas();
            // Clean canvas
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawCircle(mX, mY, mRBig, mPaint);
            canvas.drawCircle(mX, mY, mJumpR, mPaint);
            if (stickInField == null || stickInField == -1) {
                canvas.drawCircle(mX, mY, mRSmall, mPaint);
                mXDotStick = mYDotStick = null;
            }
            else {
                Float xDot = mXDotStick, yDot = mYDotStick;
                if (stickInField == 1) {
                    float dist = (float) (Math.pow(Math.pow(mXDotStick - mX, 2) +
                            Math.pow(mYDotStick - mY, 2), 0.5));
                    xDot = mX + mMaxDotDistance * (mXDotStick - mX) / dist;
                    yDot = mY + mMaxDotDistance * (mYDotStick - mY) / dist;
                }
                canvas.drawCircle(xDot, yDot, mRSmall, mPaint);

            }
            mPaint.setPathEffect(mDashPath);
            canvas.drawCircle(mX, mY, mSpecialR, mPaint);
            mPaint.setPathEffect(null);
            mStickSurfaceView.getHolder().unlockCanvasAndPost(canvas);
            moveAim();
            return true;
        }

        private void moveAim() {
            if (mPlayer != null) {
                if((mNewAimAngle = getDirectionStick()) != null)
                    mAimAngle = mNewAimAngle;
                mAimX = (int) (mPlayer.getPosition().x + (Math.cos(mAimAngle -
                        GeneralAnimation.PI90) * mRAimDistance) +
                        mPlayer.getSpriteResolution().x / 2);
                mAimY = (int) (mPlayer.getPosition().y + (Math.sin(mAimAngle -
                        GeneralAnimation.PI90) * mRAimDistance) +
                        mPlayer.getSpriteResolution().y / 2);
                drawAim(mAimX, mAimY);
            }
        }
        private void drawAim(float aimX, float aimY) {
            mAimFieldCanvas = mAimField.getHolder().lockCanvas();
            if (mAimFieldCanvas != null) {
                mAimFieldCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                mAimFieldCanvas.drawCircle(aimX, aimY, mRAim, mPaintAim);
                mAimField.getHolder().unlockCanvasAndPost(mAimFieldCanvas);
            }
        }

        public void setPlayer(Player player) {
            this.mPlayer = player;
        }
    }

    private class Stick implements View.OnTouchListener {
        protected Paint mPaint;
        protected Float mXDotStick, mYDotStick; // Small circke I cold dot
        // Joystick circle radius
        protected float mRBig, mRSmall, mSpecialR, mJumpR, mMaxDotDistance;
        protected float mRBigD, mJumpRD; // mRBigD = mRBig^2; mJumpRD =
                                        // (stickSurfaceView.getLayoutParams().height / 2)^2
        protected float mX, mY;
        protected DashPathEffect mDashPath;
        protected SurfaceView mStickSurfaceView;

        Stick(final SurfaceView stickSurfaceView) {
            // Make surfaceView transparent
            this.mStickSurfaceView = stickSurfaceView;
            stickSurfaceView.setZOrderOnTop(true);    // necessary
            stickSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
            stickSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if (GameScreen.getWindowSize().y > 0) {
                        stickSurfaceView.getLayoutParams().height =
                                stickSurfaceView.getLayoutParams().width =
                                        (int) (GameScreen.getWindowSize().y / 2);
                    }
                    else {
                        stickSurfaceView.getLayoutParams().height =
                                stickSurfaceView.getLayoutParams().width = 200;
                    }
                    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    mPaint.setColor(Color.BLUE);
                    mPaint.setAlpha(100);
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setStrokeWidth(stickSurfaceView.getLayoutParams().height / 40);
                    mDashPath = new DashPathEffect(new float[] { 5, 10, 5, 5 }, 1);
                    mX = mY = mJumpRD = stickSurfaceView.getLayoutParams().height / 2;
                    mJumpR = mJumpRD / 1.8f;
                    mJumpRD *= mJumpRD;
                    mJumpR -= mPaint.getStrokeWidth() / 2;
                    mRBig = mJumpR / 1.5f;
                    mRSmall = mRBig / 2.5f;
                    mSpecialR = mRBig - mPaint.getStrokeWidth() / 1.5f;
                    mMaxDotDistance = mRBig * 0.8f;
                    mRBigD = mRBig * mRBig;
                    onTouch(null, null);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format,
                                           int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event != null && event.getAction() != MotionEvent.ACTION_UP &&
                    event.getAction() != MotionEvent.ACTION_CANCEL) {
                mXDotStick = event.getX();
                mYDotStick = event.getY();
            } else mXDotStick = mYDotStick = null;
            Integer stickInField = stickInField();
            Canvas canvas = mStickSurfaceView.getHolder().lockCanvas();
            // Clean canvas
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawCircle(mX, mY, mRBig, mPaint);
            canvas.drawCircle(mX, mY, mJumpR, mPaint);
            if (stickInField == null || stickInField == -1) {
                canvas.drawCircle(mX, mY, mRSmall, mPaint);
                mXDotStick = mYDotStick = null;
            }
            else {
                Float xDot = mXDotStick, yDot = mYDotStick;
                if (stickInField == 1) {
                    float dist = (float) (Math.pow(Math.pow(mXDotStick - mX, 2) +
                            Math.pow(mYDotStick - mY, 2), 0.5));
                    xDot = mX + mMaxDotDistance * (mXDotStick - mX) / dist;
                    yDot = mY + mMaxDotDistance * (mYDotStick - mY) / dist;
                }
                canvas.drawCircle(xDot, yDot, mRSmall, mPaint);
            }
            mPaint.setPathEffect(mDashPath);
            canvas.drawCircle(mX, mY, mSpecialR, mPaint);
            mPaint.setPathEffect(null);
            mStickSurfaceView.getHolder().unlockCanvasAndPost(canvas);
            return true;
        }

        //-1 dot is not in circle 0 in circle 1 on circle
        public synchronized Integer stickInField() {
            if (mXDotStick == null || mYDotStick == null)
                return null;
            // Exception NullPointer ? Bad synchronization?
            float pos;
            try {
                pos = (mX - mXDotStick) * (mX - mXDotStick) + (mY - mYDotStick) *
                        (mY - mYDotStick);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            if (pos < mRBigD)
                return 0;
            if (pos < mJumpRD)
                return 1;
            return -1;
        }

        public Float getDirectionStick() {
            if (mXDotStick == null || mYDotStick == null)
                return null;
            double a, c, result;
            // Exception NullPointer ?
            try {
                a = Math.pow(Math.pow(mX - mXDotStick, 2) + Math.pow(mY - mYDotStick, 2), 0.5);
                c = Math.pow(Math.pow(mX - mXDotStick, 2) + Math.pow(mY - mRBig - mYDotStick, 2), 0.5);
                result = mXDotStick < mX ? GeneralAnimation.FLOAT_PI2 -
                        Math.acos((a * a + mRBig * mRBig - c * c) / (2 * a * mRBig)) :
                        Math.acos((a * a + mRBig * mRBig - c * c) / (2 * a * mRBig));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return (float) result;
        }

        public void cleanStickPosition() {
            mXDotStick = mYDotStick = null;
        }

    }

    Joystick(SurfaceView rightStickSurfaceV, SurfaceView leftStickSurfaceV,
             SurfaceView aimFieldSrfaceV) {
        mLeftStickSurfaceV = leftStickSurfaceV;
        mRightStickSurfaceV = rightStickSurfaceV;
        mAimFieldSrfaceV = aimFieldSrfaceV;
        if (leftStickSurfaceV != null) {
            mLeftStick = new LeftStick(leftStickSurfaceV, aimFieldSrfaceV);
            leftStickSurfaceV.setOnTouchListener(mLeftStick);
        }
        if (rightStickSurfaceV != null) {
            mRightStick = new Stick(rightStickSurfaceV);
            rightStickSurfaceV.setOnTouchListener(mRightStick);
        }
    }

    public Float getDirectionStick() {
        return mRightStick.getDirectionStick();
    }

    public Integer stickInField() {
        return mRightStick.stickInField();
    }

    public Float getAimAngle() {
        return mLeftStick.getDirectionStick();
    }

    public Integer isAimFire() {
        mLeftStick.moveAim();
        return mLeftStick.stickInField();
    }

    public static Float getAngleBetween(Point src, Point target) {
        if (target == null || src == null) return null;
        double a = Math.pow(Math.pow(src.x - target.x, 2) +
                Math.pow(src.y - target.y, 2), 0.5),
                c = Math.pow(Math.pow(src.x - target.x, 2) + Math.pow(src.y -
                        10 - target.y, 2), 0.5);
        Double result = target.x < src.x ? GeneralAnimation.FLOAT_PI2 -
                Math.acos((a * a + 10 * 10 - c * c) / (2 * a * 10)) :
                Math.acos((a * a + 10 * 10 - c * c) / (2 * a * 10));
        return result.floatValue();
    }

    public void setPlayer(Player player) {
        mLeftStick.setPlayer(player);
    }
}