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
import com.akat.filmreel.data.db.AppDatabase;
import com.akat.filmreel.data.model.ApiResponse;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.ui.MainActivity;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class MovieSyncWorker extends Worker {

    private Context context;

    public MovieSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase database = AppDatabase.getInstance(context);
        ApiManager manager = ApiManager.getInstance();

        // Make request - update first page only
        Call<ApiResponse> call = manager.getApiService().getTopRatedMovies(1);

        try {
            Response<ApiResponse> response = call.execute();
            if (response.isSuccessful()) {
                ApiResponse apiResponse = response.body();
                if (apiResponse != null) {
                    ArrayList<Movie> result = (ArrayList<Movie>) apiResponse.getResults();
                    database.topRatedDao().bulkInsert(result);

                    sendNotification(context.getString(R.string.sync_title),
                            context.getString(R.string.sync_desc));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
