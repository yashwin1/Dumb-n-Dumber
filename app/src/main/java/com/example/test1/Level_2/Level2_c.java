package com.example.test1.Level_2;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.test1.Global;
import com.example.test1.HomeWatcher;
import com.example.test1.Level_1.Lose1;
import com.example.test1.Level_1.Win1;
import com.example.test1.MainActivity;
import com.example.test1.MusicService;
import com.example.test1.R;

public class Level2_c extends AppCompatActivity {

    HomeWatcher mHomeWatcher;

    static final String STATUS_ON = "Airplane Mode : ON";
    static final String STATUS_OFF = "Airplane Mode : OFF";

    static final String TURN_ON = "Turn ON";
    static final String TURN_OFF = "Turn OFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_level2_c);

        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();

    }

    public void mainMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void pauseMenu(View view){
        Intent intent = new Intent(this, Pause2_c.class);
        startActivity(intent);
    }

    public void hint(View view){
        Intent intent = new Intent(this, Hint2_c.class);
        startActivity(intent);
    }

    public  void option1(View view){
        Intent intent = new Intent(this, Lose2_c.class);
        startActivity(intent);
    }

    public void option2(View view){
        boolean state = isAirplaneMode();
        option2AirplaneMode(state);
        updateUI(!state);
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void option2AirplaneMode(boolean state){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            Settings.System.putInt(this.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, state? 0:1);
        }
        else{
            Settings.System.putInt(this.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, state? 0:1);
        }
    }

    public void updateUI(boolean state){
        if(state){
            Intent intent = new Intent(this, Lose2_c.class);
            startActivity(intent);
        }
        else{
            Global global = (Global) getApplicationContext();
            global.setLevel(2);

            Intent intent = new Intent(this, Win2.class);
            startActivity(intent);
        }

    }
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean isAirplaneMode(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            return Settings.System.getInt(this.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
        }
        else{
            return Settings.System.getInt(this.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
        }
    }

    @Override
    public void onBackPressed() {    }

    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);

    }
}
