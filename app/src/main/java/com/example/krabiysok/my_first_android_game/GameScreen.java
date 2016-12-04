package com.example.krabiysok.my_first_android_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by KrabiySok on 1/6/2015.
 */
public class GameScreen {
    private static final Point WINDOW_SIZE = new Point();
    public static int sGameScreenHeightMin, sGameScreenHeightMax;
    private SurfaceView mWindowSurface;
    private Context mContext;
    private Bitmap mBackGround;

    public GameScreen(final SurfaceView windowSurface, Context context) {
        this.mContext = context;
        this.mWindowSurface = windowSurface;
        // Just in case
        WINDOW_SIZE.x = 800;
        WINDOW_SIZE.y = 480;
        windowSurface.getHolder().addCallback(new SurfaceHolder.Callback() {
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                setBackGround(generateBackGround(R.drawable.back_ground));
                WINDOW_SIZE.x = windowSurface.getWidth();
                WINDOW_SIZE.y = windowSurface.getHeight();
                GameScreen.sGameScreenHeightMax = GameScreen.getWindowSize().y;
                GameScreen.sGameScreenHeightMin = (int) (GameScreen.getWindowSize().y * 0.25);
                draw(getCanvas());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
            }
        });
    }

    public SurfaceView getsView() {
        return mWindowSurface;
    }

    public Canvas getCanvas() {
        Canvas canvas = mWindowSurface.getHolder().lockCanvas();
        if (canvas != null) {
            if (mBackGround != null) {
                canvas.drawBitmap(mBackGround, null, new Rect(0, 0,
                        mWindowSurface.getWidth(), mWindowSurface.getHeight()), null);
            }
        }
        return canvas;
    }

    public Bitmap generateBackGround(int backGroundId) {
        return BitmapFactory.decodeResource(mContext.getResources(), backGroundId);
        //Need to write cicada method for generation unique background
    }

    public Bitmap getBackGround() {
        return mBackGround;
    }

    public void setBackGround(Bitmap backGround) {
        this.mBackGround = backGround;
    }

    public void draw(Canvas canvas) {
        if (canvas != null)
            mWindowSurface.getHolder().unlockCanvasAndPost(canvas);
    }

    public static Point getWindowSize() {
        return WINDOW_SIZE;
    }
}
