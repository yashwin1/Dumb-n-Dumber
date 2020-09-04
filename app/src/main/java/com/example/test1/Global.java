package com.example.test1;

import android.app.Application;
import android.content.SharedPreferences;

public class Global extends Application {

    public int level = 0;

    public int music_state = 1;

//    public static final String LEVEL = " ";
//
//    public static String getLEVEL() {
//        return LEVEL;
//    }
//
//    public static String setLEVEL(){
//        return LEVEL;
//    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public int getMusic_state() {
        return music_state;
    }

    public void setMusic_state(int music_state) {
        this.music_state = music_state;
    }
}
