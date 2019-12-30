package com.akat.filmreel.data.network;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import com.akat.filmreel.MovieApplication;
import com.akat.filmreel.R;
import com.akat.filmreel.data.domain.IMovieRepository;
import com.akat.filmreel.ui.MainActivity;

import javax.inject.Inject;

import io.reactivex.Single;

public class PeriodicSyncWorker extends RxWorker {
    private static final String TAG = "PeriodicSyncWorker";

    @Inject
    IMovieRepository repository;
    private Context context;

    public PeriodicSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        MovieApplication.getAppComponent().inject(this);
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        return repository.fetchNowPlayingMovies(true)
                .doOnSuccess(apiResponse -> {
                    Log.d(TAG, "Successful sync.");
                    repository.saveNowPlayingMovies(apiResponse);
                    sendNotification(context.getString(R.string.sync_title),
                            context.getString(R.string.sync_desc));
                })
                .map(apiResponse -> Result.success())
                .onErrorReturn(throwable -> Result.failure());
    }

    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = context.getString(R.string.sync_notification_channel_id);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_movies_border)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}