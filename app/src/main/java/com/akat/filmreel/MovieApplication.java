package com.akat.filmreel;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.akat.filmreel.data.network.PeriodicSyncWorker;
import com.akat.filmreel.di.AppComponent;
import com.akat.filmreel.di.DaggerAppComponent;

import java.util.concurrent.TimeUnit;

public class MovieApplication extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .factory()
                .create(getApplicationContext());

        createNotificationChannels();
        scheduleSync();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    private void scheduleSync() {
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicRequest =
                new PeriodicWorkRequest.Builder(PeriodicSyncWorker.class,
                        1, TimeUnit.DAYS, 12, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .build();
        workManager.enqueueUniquePeriodicWork("PeriodicSyncWorker",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest);
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                // Default channel
                notificationManager.createNotificationChannel(
                        new NotificationChannel(
                                getString(R.string.default_notification_channel_id),
                                getString(R.string.default_notification_channel_name),
                                NotificationManager.IMPORTANCE_HIGH)
                );

                // Sync channel
                notificationManager.createNotificationChannel(
                        new NotificationChannel(
                                getString(R.string.sync_notification_channel_id),
                                getString(R.string.sync_notification_channel_name),
                                NotificationManager.IMPORTANCE_LOW)
                );
            }
        }
    }
}
