package com.example.krabiysok.my_first_android_game;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    public static final int HEALTH_CHANGED = 0, WEAPON_CHANGED = 1, AMMO_CHANGED = 2,
            SCORE_CHANGED = 3, SET_BUTTONS_VISIBLE = 4, SET_BEST_SCORE_VISIBLE = 5;
    private static MainActivity mMainActivity;
    private static Handler mHandler;
    private GameScreen mGameScreen;
    private Joystick mJoystick;
    private GameProcess mGameProcess;
    private SurfaceView mRightStick, mLeftStick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.main_layout);
            mGameScreen = new GameScreen((SurfaceView) findViewById(R.id.gameScreen), this);
            mJoystick = new Joystick((SurfaceView) findViewById(R.id.joystickRight),
                    (SurfaceView) findViewById(R.id.joystickLeft),
                    (SurfaceView) findViewById(R.id.aimField));
            mRightStick = (SurfaceView) findViewById(R.id.joystickRight);
            mLeftStick = (SurfaceView) findViewById(R.id.joystickLeft);

            mMainActivity = this;
            mHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case HEALTH_CHANGED:
                            ((ProgressBar) MainActivity.getMainActivity().findViewById(R.id.health)).
                                    setProgress(msg.arg1);
                            ((ProgressBar) MainActivity.getMainActivity().findViewById(R.id.energe)).
                                    setProgress(msg.arg2);
                            break;
                        case WEAPON_CHANGED:
                            ((ImageView) MainActivity.getMainActivity().findViewById(R.id.weapons)).
                                    setImageResource(msg.arg1);
                            if (msg.arg2 > 0)
                                ((TextView) MainActivity.getMainActivity().
                                        findViewById(R.id.weaponAmmo)).
                                        setText("Ammo " + String.valueOf(msg.arg2));
                            break;
                        case AMMO_CHANGED:
                            ((TextView) MainActivity.getMainActivity().findViewById(R.id.weaponAmmo)).
                                    setText("Ammo " + String.valueOf(msg.arg1));
                            break;
                        case SCORE_CHANGED:
                            ((TextView) MainActivity.getMainActivity().findViewById(R.id.gameScore)).
                                    setText("Score " + String.valueOf(msg.arg1));
                            break;
                        case SET_BUTTONS_VISIBLE:
                            ((Button) (MainActivity.getMainActivity().
                                    findViewById(R.id.buttonClose))).setVisibility(View.VISIBLE);
                            ((Button) (MainActivity.getMainActivity().
                                    findViewById(R.id.buttonRestart))).setVisibility(View.VISIBLE);
                        break;
                        case SET_BEST_SCORE_VISIBLE:
                            ((TextView) (MainActivity.getMainActivity().
                                    findViewById(R.id.best_score))).setVisibility(View.VISIBLE);
                            ((TextView) (MainActivity.getMainActivity().
                                    findViewById(R.id.best_score))).setText("Best Score " + msg.arg1);
                    }
                };
            };
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            Log.d("LogApp", "active");
            if (mGameProcess != null && mGameProcess.isGameStop()) {
                mGameProcess = GameProcess.getNewGameProcess(mGameScreen, mJoystick);
            } else mGameProcess = GameProcess.getGameProcess(mGameScreen, mJoystick);
            if (mGameProcess.isGameSleep() && ((Button)
                    (MainActivity.getMainActivity().findViewById(R.id.buttonRestart))).
                    getVisibility() != View.VISIBLE) {
                //mGameProcess.drawLastFrame();
                ((Button) (MainActivity.getMainActivity().
                        findViewById(R.id.buttonContinue))).setVisibility(View.VISIBLE);
            } else if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE) {
                mGameProcess.startGame();
            }
        } else {
            Log.d("LogApp", "disabled");
            mGameProcess.sleepGame();
        }
    }

    // This method doesn't call after finishing application!!!!(need to repair)
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LogApp", "Destroy");
        if (mGameProcess != null) {
            mGameProcess.stopGame();
        }
    }

    public static MainActivity getMainActivity() {
        return mMainActivity;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public void onCloseGame(View v) {
        if (mGameProcess != null) {
            mGameProcess.stopGame();
        }
        finish();
    }

    public void onRestartGame(View v) {
        GameProcess.getGameProcess().restart();
        ((Button) (MainActivity.getMainActivity().
                findViewById(R.id.buttonClose))).setVisibility(View.GONE);
        ((Button) (MainActivity.getMainActivity().
                findViewById(R.id.buttonRestart))).setVisibility(View.GONE);
        ((Button) (MainActivity.getMainActivity().
                findViewById(R.id.buttonContinue))).setVisibility(View.GONE);
        ((TextView) (MainActivity.getMainActivity().
                findViewById(R.id.best_score))).setVisibility(View.GONE);
    }

    public void onContinue(View v) {
        mGameProcess.startGame();
        ((Button) (MainActivity.getMainActivity().
                findViewById(R.id.buttonContinue))).setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(getResources().getString(R.string.close_game));
        menu.add(getResources().getString(R.string.restart_game));
        menu.add(getResources().getString(R.string.change_left_stick_position));
        menu.add(getResources().getString(R.string.change_right_stick_position));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getResources().getString(R.string.close_game))) {
            if (mGameProcess != null) {
                mGameProcess.stopGame();
            }
            finish();
        }
        if (item.getTitle().equals(getResources().getString(R.string.restart_game))) {
            onRestartGame(null);
        }
        if (item.getTitle().equals(getResources().
                getString(R.string.change_right_stick_position))) {
            changeStickPosition(mRightStick);
        }
        if (item.getTitle().equals(getResources().
                getString(R.string.change_left_stick_position))) {
            changeStickPosition(mLeftStick);
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void changeStickPosition(SurfaceView surfaceView) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                surfaceView.getLayoutParams();
        if (surfaceView.getTop() == 0) {
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        }
        surfaceView.setLayoutParams(params); //causes layout update
    }
}
