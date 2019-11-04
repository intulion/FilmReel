package com.akat.filmreel.util;

import android.content.Context;

import com.akat.filmreel.data.Repository;
import com.akat.filmreel.data.db.AppDatabase;
import com.akat.filmreel.data.network.ApiManager;
import com.akat.filmreel.data.network.NetworkDataSource;
import com.akat.filmreel.ui.topRated.TopRatedViewModelFactory;

public class InjectorUtils {

    private static Repository provideTopRatedRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        ApiManager manager = ApiManager.getInstance();
        NetworkDataSource networkDataSource =
                NetworkDataSource.getInstance(executors, manager);
        return Repository.getInstance(database.topRatedDao(), networkDataSource, executors);
    }

    public static TopRatedViewModelFactory provideTopRatedViewModelFactory(Context context) {
        Repository repository = provideTopRatedRepository(context.getApplicationContext());
        return new TopRatedViewModelFactory(repository);
    }

}