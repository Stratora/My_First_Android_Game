package com.example.krabiysok.my_first_android_game;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by KrabiySok on 1/13/2015.
 */
public class MyArrayList<E> extends ArrayList<E> {
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        if (collection != null)
            return super.addAll(collection);
        return false;
    }

    @Override
    public boolean add(E object) {
        if (object != null)
            return super.add(object);
        return false;
    }
}
