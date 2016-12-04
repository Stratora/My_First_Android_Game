package com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabiysok.enemies;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.krabiysok.my_first_android_game.Joystick;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.GeneralAnimation;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.Player;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabisok.bullets.Bullet;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.weapons.Weapon;

import java.util.ArrayList;

/**
 * Created by KrabiySok on 1/13/2015.
 */
public class Enemie extends GeneralAnimation {
    protected int mHealth, mAccelerTime, mSpeed, mAcceleration, mAcceler_time;
    protected ArrayList<Bullet> mEnemieBullets;
    protected Weapon mWeapon;
    protected Bullet mBullet;
    protected Double mEnemieAngle;
    protected int mMoveSpeed, mEnemieX0, mEnemieX1, mEnemieY0, mEnemieY1,
            mBulletX0, mBulletX1, mBulletY0, mBulletY1;

    public Enemie(int x, int y, Bitmap enemieSprite, int health,
                  int speed, int acceler_time, int acceleration) {
        super(x, y, 4, 3, enemieSprite, 0.12f);
        mHealth = health;
        mSpeed = speed;
        mAcceler_time = acceler_time;
        mAcceleration = acceleration;
    }

    public ArrayList<Bullet> action(Canvas canva, Player player) {
        mEnemieAngle = Joystick.getAngleBetween(getPosition(), player.getPosition());
        mMoveSpeed = mSpeed;
        if (mEnemieAngle != null) {
            if (mAccelerTime > 0) {
                mMoveSpeed *= mAcceleration;
                mAccelerTime--;
            }
        }
        drawMove(canva, mEnemieAngle, mMoveSpeed);
        if (mWeapon.getAmmo() > 0) {
            if (mWeapon.getReloudTime() < mWeapon.getBulletReloud()) mWeapon.reloud();
            mEnemieBullets.clear();
            if (mWeapon.getReloudTime() == mWeapon.getBulletReloud()) {
                mWeapon.startReloud();
                mEnemieBullets.add(mWeapon.getBullet(getPosition().x, getPosition().y,
                        this, mEnemieAngle));
            }
        }
        return mEnemieBullets;
    }

    public boolean isAlive(ArrayList<Bullet> bullets, Player player) {
        for(int i = 0; i < bullets.size(); i++) {
            mBullet = bullets.get(i);
            if (mBullet.getBulletBelongs().equals(player)) {
                mEnemieX0 = getPosition().x;
                mEnemieY0 = getPosition().y;
                mBulletX0 = mBullet.getPosition().x;
                mBulletY0 = mBullet.getPosition().y;
                mEnemieX1 = mEnemieX0 + getSpriteResolution().x;
                mEnemieY1 = mEnemieY0 + getSpriteResolution().y;
                mBulletX1 = mBulletX0 + mBullet.getSpriteResolution().x;
                mBulletY1 = mBulletY0 + mBullet.getSpriteResolution().y;
                if ((mBulletX0 <= mEnemieX0 && mEnemieX0 <= mBulletX1 &&
                        mBulletY0 <= mEnemieY0 && mEnemieY0 <= mBulletY1) ||
                        (mBulletX0 <= mEnemieX1 && mEnemieX1 <= mBulletX1 &&
                                mBulletY0 <= mEnemieY1 && mEnemieY1 <= mBulletY1)) {
                    mHealth -= mBullet.getDamage();
                    if (mBullet.getBulletBelongs().equals(player))
                        player.addScore(mBullet.getDamage());
                    bullets.remove(i);
                    i--;
                }
            }
        }
        if (mHealth <= 0)
            return false;
        return true;
    }

    public Weapon getmWeapon() {
        return mWeapon;
    }
}
