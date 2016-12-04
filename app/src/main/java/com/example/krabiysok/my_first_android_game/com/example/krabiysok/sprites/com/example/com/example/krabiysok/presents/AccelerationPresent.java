package com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.com.example.krabiysok.presents;

import android.graphics.BitmapFactory;

import com.example.krabiysok.my_first_android_game.GameProcess;
import com.example.krabiysok.my_first_android_game.MainActivity;
import com.example.krabiysok.my_first_android_game.R;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.Player;

/**
 * Created by KrabiySok on 1/13/2015.
 */
public class AccelerationPresent extends Present {
    private static final int ACCELERATION_TIME = 2000 / GameProcess.FPS;

    public AccelerationPresent(int x, int y) {
        super(BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(),
                R.drawable.acceleration_time_present), x, y);
    }

    @Override
    protected void presentForPlayer(Player player) {
        player.addAccelerationTime(ACCELERATION_TIME);
    }
}
