package com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabiysok.enemies;

import android.graphics.BitmapFactory;

import com.example.krabiysok.my_first_android_game.GameProcess;
import com.example.krabiysok.my_first_android_game.MainActivity;
import com.example.krabiysok.my_first_android_game.R;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.weapons.MajorWeapon;

import java.util.ArrayList;

/**
 * Created by KrabiySok on 1/13/2015.
 */
public class Major extends Enemy {
    public Major(int x, int y) {
        super(x, y,
                BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(),
                        R.drawable.major), 100, 100 / GameProcess.FPS, 0, 0);
        mEnemyBullets = new ArrayList<>();
        mWeapon = new MajorWeapon();
    }
}
