package com.example.krabiysok.my_first_android_game;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.Player;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.MrCat;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.com.example.krabiysok.presents.Present;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabisok.bullets.Bullet;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabiysok.enemies.Enemy;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabiysok.enemies.Major;
import com.example.krabiysok.my_first_android_game.com.example.krabiysok.sprites.com.example.krabiysok.enemies.Succubus;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by KrabiySok on 1/10/2015.
 */
public class GameProcess implements Runnable {
    public static final int FPS = 50; // Speed of updates
    private static GameProcess sGameProcess = new GameProcess();
    private GameScreen mGameScreen;
    private Joystick mJoystick;
    private volatile boolean mStop, mSleepGame;
    private Thread mGameThread;
    private Paint mPaint;
    private Player mPlayer;
    private MyArrayList<Bullet> mBullets;
    private ArrayList<Enemy> mEnemies;
    private MrCat mMrCat;
    private MyArrayList<Present> mPresents;
    private int mCurrentFPS = FPS;
    private Random mRandom;
    private Message mMsg;

    private GameProcess() {
        mPaint = new Paint();
        mPaint.setSubpixelText(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(GameScreen.getWindowSize().y / 10);
    }

    public static GameProcess getGameProcess() {
        if (sGameProcess.mGameScreen != null) return sGameProcess;
        return null;
    }

    public static GameProcess getNewGameProcess(GameScreen gameScreen, Joystick joystick) {
        // I did so because my program crashes when a user try to run close program
        if (gameScreen != null && joystick != null) {
            sGameProcess.mGameScreen = gameScreen;
            sGameProcess.mJoystick = joystick;
            sGameProcess.mGameThread = new Thread(sGameProcess, "Game Thread");
            sGameProcess.mGameThread.setDaemon(true);
        }
        return sGameProcess;
    }

    public static GameProcess getGameProcess(GameScreen gameScreen, Joystick joystick) {
        if (sGameProcess.mGameScreen == null && gameScreen != null && joystick != null) {
            sGameProcess.mGameScreen = gameScreen;
            sGameProcess.mJoystick = joystick;
            sGameProcess.mGameThread = new Thread(sGameProcess, "Game Thread");
            sGameProcess.mGameThread.setDaemon(true);
        }
        return sGameProcess;
    }

    @Override
    public void run() {
        Present present;
        Enemy enemy;
        Bullet bullet;
        Canvas canvas;

        mPlayer = new Player(mGameScreen.getWindowSize().x / 2,
                mGameScreen.getWindowSize().y / 2, mJoystick);
        mJoystick.setPlayer(mPlayer);
        mBullets = new MyArrayList<>();
        mEnemies = new ArrayList<>();
        mPresents = new MyArrayList<>();
        mMrCat = new MrCat();
        mRandom = new Random();
        int randomInt, x, y;

        while (!mStop) {
            Log.d("LogApp", "game Go");
            if (mPlayer.getScore100() > mCurrentFPS)
                mCurrentFPS = FPS - mPlayer.getScore100();
            randomInt = mRandom.nextInt(1000);
            if ((mEnemies.size() < 7) && (mEnemies.isEmpty() ||
                    (randomInt % FPS) < mPlayer.getScore100())) {
                x = randomInt % 2 == 1 ? 0 : GameScreen.getWindowSize().x;
                y = GameScreen.sGameScreenHeightMin + randomInt %
                        (GameScreen.getWindowSize().y - GameScreen.sGameScreenHeightMin);
                switch (randomInt % 3) {
                    case 0: {
                        mEnemies.add(new Major(x, y));
                        break;
                    }
                    case 1: {
                        mEnemies.add(new Succubus(x, y));
                        break;
                    }
                    case 2: {
                        mEnemies.add(new Succubus(x, y));
                        break;
                    }
                }
            }
            canvas = mGameScreen.getCanvas();
            mBullets.addAll(mPlayer.action(canvas));
            mPresents.add(mMrCat.action(canvas, randomInt));
            for(int i = 0; i < mEnemies.size(); ++i) {
                enemy = mEnemies.get(i);
                mBullets.addAll(enemy.action(canvas, mPlayer));
                if (!enemy.isAlive(mBullets, mPlayer)) {
                    mEnemies.remove(i);
                    i--;
                }
            }
            for(int i = 0; i < mPresents.size(); ++i) {
                present = mPresents.get(i);
                if (present.takes(mPlayer)) {
                    mPresents.remove(i);
                    i--;
                }
                else present.draw(canvas);
            }
            for(int i = 0; i < mBullets.size(); ++i) {
                bullet = mBullets.get(i);
                if (!bullet.drawMove(canvas)) {
                    mBullets.remove(i);
                    i--;
                }
            }
            if (!mPlayer.isAlive(mBullets)) {
                playerDead(canvas);
            }
            mGameScreen.draw(canvas);
            try {
                if (mCurrentFPS < 20) {
                    Thread.sleep(20); // I did so specifically
                }
                else Thread.sleep(mCurrentFPS);
                if (mSleepGame) {

                    synchronized (mGameThread) {
                        mGameThread.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startGame() {
        if (mGameThread.getState() == Thread.State.NEW) {
            mStop = false;
            mGameThread.start();
        } else if (mSleepGame) {
            mSleepGame = false;
            synchronized (mGameThread) {
                mGameThread.notify();
            }
        }
    }

    private void playerDead(Canvas canvas) {
        SharedPreferences sPref;
        SharedPreferences.Editor ed;
        sPref = MainActivity.getMainActivity().getPreferences(
                MainActivity.getMainActivity().MODE_PRIVATE);
        int bestScore = sPref.getInt("BEST_SCORE", 0);
        if ( mPlayer.getScore() > bestScore) {
            bestScore = mPlayer.getScore();
                ed = sPref.edit();
            ed.putInt("BEST_SCORE", bestScore);
            ed.commit();

        }
        mMsg = MainActivity.getHandler().
                obtainMessage(MainActivity.SET_BEST_SCORE_VISIBLE, bestScore, 0);
        MainActivity.getHandler().sendMessage(mMsg);
        canvas.drawText("Game Over", (float) (GameScreen.getWindowSize().x / 2 -
                        GameScreen.getWindowSize().x / 7),
                GameScreen.getWindowSize().y / 2, mPaint);
        sleepGame(); //---
        MainActivity.getHandler().sendEmptyMessage(MainActivity.SET_BUTTONS_VISIBLE);
    }

    public void sleepGame() {
        mSleepGame = true;
    }

    public void stopGame() {
        mStop = true;
    }

    public void restart() {
        mCurrentFPS = FPS;
        mPlayer = new Player(mGameScreen.getWindowSize().x / 2,
                mGameScreen.getWindowSize().y / 2, mJoystick);
        mJoystick.setPlayer(mPlayer);
        mBullets = new MyArrayList<>();
        mEnemies = new ArrayList<>();
        mPresents = new MyArrayList<>();
        mMrCat = new MrCat();
        mRandom = new Random();
        this.startGame();
    }

    public boolean isGameSleep() {
        return mSleepGame;
    }

    public boolean isGameStop() { return mStop; }
}
