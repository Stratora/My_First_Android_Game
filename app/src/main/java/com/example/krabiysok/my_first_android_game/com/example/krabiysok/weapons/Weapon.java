package com.example.krabiysok.my_first_android_game.com.example.krabiysok.weapons;

import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.GeneralAnimation;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabisok.bullets.Bullet;

/**
 * Created by KrabiySok on 1/11/2015.
 */
public abstract class Weapon {
    private int mWeaponImageID;
    private int mReloadTime, mAmmo;
    public int mBulletReload;

    protected Weapon(int bulletReloud, int ammo, int weapon) {
        mBulletReload = bulletReloud;
        mReloadTime = bulletReloud;
        mAmmo = ammo;
        mWeaponImageID = weapon;
    }

    public void startReload() {
        mAmmo--;
        mReloadTime = 0;
    }

    public void reload() {
        if (mReloadTime < mBulletReload) {
            mReloadTime++;
        }
    }

    public int getWeapon() {
        return mWeaponImageID;
    }

    public int getReloadTime() {
        return mReloadTime;
    }

    public int getAmmo() {
        return mAmmo;
    }

    public abstract Bullet getBullet(int x, int y,
                                     GeneralAnimation bulletBelongs, float angle);

    public int getBulletReload() {
        return mBulletReload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weapon weapon = (Weapon) o;

        if (mAmmo != weapon.mAmmo) return false;
        if (mBulletReload != weapon.mBulletReload) return false;
        if (mReloadTime != weapon.mReloadTime) return false;
        if (mWeaponImageID != weapon.mWeaponImageID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mWeaponImageID;
        result = 31 * result + mReloadTime;
        result = 31 * result + mAmmo;
        result = 31 * result + mBulletReload;
        return result;
    }
}
