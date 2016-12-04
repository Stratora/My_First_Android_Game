package com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabisok.bullets;

import android.graphics.BitmapFactory;

import com.example.krabiysok.my_first_android_game.GameProcess;
import com.example.krabiysok.my_first_android_game.MainActivity;
import com.example.krabiysok.my_first_android_game.R;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.GeneralAnimation;

/**
 * Created by KrabiySok on 1/11/2015.
 */
public class EnemieBullet extends Bullet {
    public EnemieBullet(int x, int y, GeneralAnimation bulletBelongs, float angle) {
        super(x, y, 4, 4,
                BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(),
                        R.drawable.major_bullet), 0.14f, 40, 1800 / GameProcess.FPS,
                angle, bulletBelongs);
    }
}
