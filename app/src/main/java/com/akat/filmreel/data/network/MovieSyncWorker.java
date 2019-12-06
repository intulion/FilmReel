package com.akat.filmreel.data.network;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.akat.filmreel.R;
import com.akat.filmreel.data.domain.MovieRepository;
import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.ui.MainActivity;
import com.akat.filmreel.util.InjectorUtils;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieSyncWorker extends Worker {

    private Context context;

    public MovieSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        MovieRepository repository = InjectorUtils.provideRepository(context);

        // Make request - force update now playing movies
        repository.fetchNowPlayingMovies(true)
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        repository.saveMovies(apiResponse);

                        sendNotification(context.getString(R.string.sync_title),
                                context.getString(R.string.sync_desc));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

        return Result.success();
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
