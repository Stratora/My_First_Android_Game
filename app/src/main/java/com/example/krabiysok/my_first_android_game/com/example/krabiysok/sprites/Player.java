package com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Message;

import com.example.krabiysok.my_first_android_game.GameProcess;
import com.example.krabiysok.my_first_android_game.Joystick;
import com.example.krabiysok.my_first_android_game.MainActivity;
import com.example.krabiysok.my_first_android_game.R;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabisok.bullets.Bullet;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.weapons.RegularWeapon;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.weapons.Weapon;

import java.util.ArrayList;

/**
 * Created by KrabiySok on 1/10/2015.
 */
public class Player extends GeneralAnimation {
    private static final int HEALTH = 1000, SPEED = 500 / GameProcess.FPS,
            ACCELER_TIME = 10000 / GameProcess.FPS, ACCELERATION = 2;
    private Joystick mJoystick;
    private int mHealth = HEALTH, mAccelerTime = ACCELER_TIME, mScore;
    private ArrayList<Weapon> mWeapons;
    private ArrayList<Bullet> mPlayerBullets;
    private Weapon mWeapon;
    private Bullet mBullet;
    private Float mPlayerAngle;
    private Integer mIsPlayerFire;
    private int mMoveSpeed, mPlayerX0, mPlayerX1, mPlayerY0, mPlayerY1,
            mBulletX0, mBulletX1, mBulletY0, mBulletY1;
    private Message mMsg;

    public Player(int x, int y, Joystick joystick) {
        super(x, y, 4, 3,
                BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(),
                R.drawable.boy), 0.12);
        mPlayerBullets = new ArrayList<>();
        mJoystick = joystick;
        mWeapons = new ArrayList<>();
        mWeapons.add(new RegularWeapon());
        mWeapon = mWeapons.get(0);
        mMsg = MainActivity.getHandler().
                obtainMessage(MainActivity.SCORE_CHANGED, mScore, 0);
        MainActivity.getHandler().sendMessage(mMsg);
        mMsg = MainActivity.getHandler().
                obtainMessage(MainActivity.WEAPON_CHANGED, mWeapon.getWeapon(), mWeapon.getAmmo());
        MainActivity.getHandler().sendMessage(mMsg);
    }

    public ArrayList<Bullet> action(Canvas canvas) {
        mPlayerAngle = mJoystick.getDirectionStick();
        mMoveSpeed = SPEED;
        if (mPlayerAngle != null) {
            if (mAccelerTime > 0 && mJoystick.stickInField() == 1) {
                mMoveSpeed *= ACCELERATION;
                mAccelerTime--;
            }
        }
        drawMove(canvas, mPlayerAngle, mMoveSpeed);
        for (int i = mWeapons.size() - 1; ; i--) {
            if (mWeapons.get(i).getAmmo() <= 0 && i != 0) {
                mWeapons.remove(i);
            } else {
                if (mWeapon == null || !mWeapon.equals(mWeapons.get(i))) {
                    mWeapon = mWeapons.get(i);
                    mMsg = MainActivity.getHandler().
                         obtainMessage(MainActivity.WEAPON_CHANGED, mWeapon.getWeapon(),
                                 mWeapon.getAmmo());
                    MainActivity.getHandler().sendMessage(mMsg);
                }
                break;
            }
        }
        if (mWeapon.getReloadTime() < mWeapon.getBulletReload()) {
            mWeapon.reload();
        }
        mPlayerBullets.clear();
        mIsPlayerFire = mJoystick.isAimFire();
        if (mIsPlayerFire != null && mIsPlayerFire == 1 &&
                mWeapon.getReloadTime() == mWeapon.getBulletReload()) {
            mWeapon.startReload();
            mMsg = MainActivity.getHandler().
                    obtainMessage(MainActivity.AMMO_CHANGED, mWeapon.getAmmo(), mWeapon.getAmmo());
            MainActivity.getHandler().sendMessage(mMsg);
            mPlayerBullets.add(mWeapon.getBullet(getPosition().x, getPosition().y,
                    this, mJoystick.getAimAngle()));
        }
        return mPlayerBullets;
    }

    public boolean isAlive(ArrayList<Bullet> bullets) {
        for(int i = 0; i < bullets.size(); i++) {
            mBullet = bullets.get(i);
            if (!mBullet.getBulletBelongs().equals(this)) {
                mPlayerX0 = getPosition().x;
                mPlayerY0 = getPosition().y;
                mBulletX0 = mBullet.getPosition().x;
                mBulletY0 = mBullet.getPosition().y;
                mPlayerX1 = mPlayerX0 + getSpriteResolution().x;
                mPlayerY1 = mPlayerY0 + getSpriteResolution().y;
                mBulletX1 = mBulletX0 + mBullet.getSpriteResolution().x;
                mBulletY1 = mBulletY0 + mBullet.getSpriteResolution().y;
                if ((mBulletX0 <= mPlayerX0 && mPlayerX0 <= mBulletX1 &&
                        mBulletY0 <= mPlayerY0 && mPlayerY0 <= mBulletY1) ||
                        (mBulletX0 <= mPlayerX1 && mPlayerX1 <= mBulletX1 &&
                                mBulletY0 <= mPlayerY1 && mPlayerY1 <= mBulletY1)) {
                    mHealth -= mBullet.getDamage();
                    bullets.remove(i);
                    i--;
                }
            }
        }
        if (mHealth <= 0)
            return false;
        mMsg = MainActivity.getHandler().
                obtainMessage(MainActivity.HEALTH_CHANGED, mHealth, mAccelerTime);
        MainActivity.getHandler().sendMessage(mMsg);
        return true;
    }

    public void addWeapon(Weapon weapon) {
        mWeapons.add(weapon);
    }

    public Weapon getWeapon() {
        return mWeapon;
    }

    public int getScore100() {
        int score100 = mScore / 100;
        return score100;
    }

    public int getScore() {
        return mScore;
    }

    public void takeHealth(int take) {
        mHealth -= take;
    }

    public void addHealth(int health) {
        this.mHealth += health;
        if (this.mHealth >= HEALTH) {
            this.mHealth = HEALTH;
        }
    }

    public void addAccelerationTime(int accelerationTime) {
        this.mAccelerTime += accelerationTime;
        if (this.mAccelerTime >= ACCELER_TIME) {
            this.mAccelerTime = ACCELER_TIME;
        }
    }

    public void addScore(int points) {
        mScore += points;
        mMsg = MainActivity.getHandler().
                obtainMessage(MainActivity.SCORE_CHANGED, mScore, 0);
        MainActivity.getHandler().sendMessage(mMsg);
    }
}
