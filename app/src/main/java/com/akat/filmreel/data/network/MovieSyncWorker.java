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
import com.akat.filmreel.data.local.LocalDataSource;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.ui.MainActivity;
import com.akat.filmreel.util.InjectorUtils;

import java.util.List;

public class MovieSyncWorker extends Worker {

    private Context context;

    public MovieSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        LocalDataSource localDataSource = InjectorUtils.provideLocalDataSource(context);
        NetworkDataSource networkDataSource = InjectorUtils.provideNetworkDataSource(context);

        // Make request - update first page only
        List<Movie> movieList = networkDataSource.getTopRatedMovies(1);

        if (!movieList.isEmpty()) {
            localDataSource.addMovies(movieList);

            sendNotification(context.getString(R.string.sync_title),
                    context.getString(R.string.sync_desc));

            return Result.success();
        }

        return Result.failure();
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
