package com.akat.filmreel.data.network;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.InjectorUtils;

public class MovieSyncService extends IntentService {

    private static final String TAG = MovieSyncService.class.getSimpleName();

    public MovieSyncService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;

        int currentPage = intent.getIntExtra(Constants.PARAM.CURRENT_PAGE, 0);

        NetworkDataSource networkDataSource = InjectorUtils.provideNetworkDataSource(getApplicationContext());
        networkDataSource.fetchTopRatedMovies(currentPage);
    }

}
