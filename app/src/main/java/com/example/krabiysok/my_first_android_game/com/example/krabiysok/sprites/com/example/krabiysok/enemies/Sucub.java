package com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabiysok.enemies;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.krabiysok.my_first_android_game.GameProcess;
import com.example.krabiysok.my_first_android_game.Joystick;
import com.example.krabiysok.my_first_android_game.MainActivity;
import com.example.krabiysok.my_first_android_game.R;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.Player;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabisok.bullets.Bullet;

import java.util.ArrayList;

/**
 * Created by KrabiySok on 1/13/2015.
 */
public class Sucub extends Enemie {
    private static final int DAMAGE = 3;
    private int sucubX0, sucubX1, sucubY0, sucubY1, playerX0, playerX1, playerY0, playerY1;

    public Sucub(int x, int y) {
        super(x, y,
                BitmapFactory.decodeResource(MainActivity.getContext().getResources(),
                        R.drawable.sucub), 200, (int) (1000 / GameProcess.fps),
                5000 / GameProcess.fps, 3);
    }
    
    @Override
    public ArrayList<Bullet> action(Canvas canva, Player player) {
        mEnemieAngle = Joystick.getAngleBetween(getPosition(), player.getPosition());
        mMoveSpeed = mSpeed;
        if (mEnemieAngle != null && mAccelerTime > 0) {
            mMoveSpeed *= mAcceleration;
            mAccelerTime--;
        }
        drawMove(canva, mEnemieAngle, mMoveSpeed);
        sucubX0 = getPosition().x;
        sucubY0 = getPosition().y;
        playerX0 = player.getPosition().x;
        playerY0 = player.getPosition().y;
        sucubX1 = sucubX0 + getSpriteResolution().x;
        sucubY1 = sucubY0 + getSpriteResolution().y;
        playerX1 = playerX0 + player.getSpriteResolution().x;
        playerY1 = playerY0 + player.getSpriteResolution().y;
        if ((playerX0 <= sucubX0 && sucubX0 <= playerX1 &&
                playerY0 <= sucubY0 && sucubY0 <= playerY1) ||
                (playerX0 <= sucubX1 && sucubX1 <= playerX1 &&
                        playerY0 <= sucubY1 && sucubY1 <= playerY1))
            player.takeHealth(DAMAGE);
        return mEnemieBullets;
    }
}
