package com.scsfdev.ics;

import android.app.Application;
import android.content.res.Configuration;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
