package com.example.krabiysok.my_first_android_game.com.example.krabiysok.weapons;

import com.example.krabiysok.my_first_android_game.GameProcess;
import com.example.krabiysok.my_first_android_game.R;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.GeneralAnimation;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabisok.bullets.Bullet;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabisok.bullets.SpecialBullet;

/**
 * Created by KrabiySok on 1/11/2015.
 */
public class SpecialWeapon extends Weapon {
    public SpecialWeapon() {
        super(200 / GameProcess.FPS, 40, R.drawable.special_weapon);
    }

    @Override
    public Bullet getBullet(int x, int y, GeneralAnimation bulletBelongs, float angle) {
        return new SpecialBullet(x, y, bulletBelongs, angle);
    }
}
