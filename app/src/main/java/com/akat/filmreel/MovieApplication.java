package com.akat.filmreel;

import android.app.Application;

import com.akat.filmreel.di.AppComponent;
import com.akat.filmreel.di.DaggerAppComponent;

public class MovieApplication extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .factory()
                .create(getApplicationContext());
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

}
