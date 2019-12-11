package com.akat.filmreel;

import android.app.Application;

import com.akat.filmreel.di.AppComponent;
import com.akat.filmreel.di.DaggerAppComponent;
import com.akat.filmreel.di.PlacesComponent;

public class MovieApplication extends Application {

    private static AppComponent appComponent;
    private static PlacesComponent placesComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .factory()
                .create(getApplicationContext());

        placesComponent = appComponent.placesComponent().create();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static PlacesComponent getPlacesComponent() {
        return placesComponent;
    }
}
