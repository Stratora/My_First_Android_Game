package com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.com.example.krabiysok.presents;

import android.graphics.BitmapFactory;

import com.example.krabiysok.my_first_android_game.MainActivity;
import com.example.krabiysok.my_first_android_game.R;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.Player;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.weapons.SpecialWeapon;

/**
 * Created by KrabiySok on 1/13/2015.
 */
public class WeaponPresent extends Present {
    public WeaponPresent(int x, int y) {
        super(BitmapFactory.decodeResource(MainActivity.getMainActivity().getResources(),
                R.drawable.special_weapon_present), x, y);
    }

    @Override
    protected void presentForPlayer(Player player) {
        player.addWeapon(new SpecialWeapon());
    }
}
