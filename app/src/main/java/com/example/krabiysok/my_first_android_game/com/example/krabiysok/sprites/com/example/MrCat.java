package com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.krabiysok.my_first_android_game.GameProcess;
import com.example.krabiysok.my_first_android_game.MainActivity;
import com.example.krabiysok.my_first_android_game.R;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.GeneralAnimation;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.com.example.krabiysok.presents.AccelerationPresent;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.com.example.krabiysok.presents.HealthPresent;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.com.example.krabiysok.presents.Present;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.com.example.krabiysok.presents.WeaponPresent;

/**
 * Created by KrabiySok on 1/13/2015.
 */
public class MrCat extends GeneralAnimation {
    private static final int SPEED = 800 / GameProcess.FPS,
            ACCELER_TIME = 0, ACCELERATION = 0;
    private static final float ANGLE15 = ANGLE45 / 3, ANGLE150 = FLOAT_PI -
            ANGLE45 * 2, ANGLE75 = ANGLE45 * 3, ANGLE195 = FLOAT_PI + ANGLE15,
            ANGLE105 = PI90 + ANGLE15, ANGLE285 = PI360 - ANGLE75;
    private float mMoveAngle;
    private boolean mActive;
    private int mPresentCount;

    public MrCat() {
        super(0, 0, 4, 3, BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(),
                R.drawable.mr_cat), 0.12f);
        mMoveAngle = PI270;
    }

    public Present action(Canvas canva, int randKey) {
        if (mActive) {
            if (mPosition.x <= mMinXAnimatPos) {
                mMoveAngle = ANGLE15 + ((ANGLE150 * 1000) % randKey) / 1000;
                mActive = false;
            }
            if (mPosition.x >= mMaxXAnimatPos) {
                mMoveAngle = ANGLE195 + ((ANGLE150 * 1000) % randKey) / 1000;
                mActive = false;
            }
            if (mPosition.y >= mMaxYAnimatPos) {
                mMoveAngle = (randKey % 2) == 1 ? ANGLE285 +
                        ((ANGLE75 * 1000) % randKey) / 1000 :
                        ((ANGLE75 * 1000) % randKey) / 1000;
                mActive = false;
            }
            if (mPosition.y <= mMinYAnimatPos) {
                mMoveAngle = ANGLE105 + ((ANGLE150 * 1000) % randKey) / 1000;
                mActive = false;
            }
        }
        if (!mActive && (randKey % 50 < 2)) {
            mActive = true;
            mPresentCount = 0;
        }
        if (mActive) {
            drawMove(canva, mMoveAngle, SPEED);
            if (mPresentCount < 5 && (randKey % 100) < 3) {
                mPresentCount++;
                switch (randKey % 3) {
                    case 0:
                        return new HealthPresent(mPosition.x, mPosition.y);
                    case 1:
                        return new WeaponPresent(mPosition.x, mPosition.y);
                    case 2:
                        return new AccelerationPresent(mPosition.x, mPosition.y);
                }
            }
        }
        return null;
    }
}
