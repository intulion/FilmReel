package com.akat.filmreel.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.test.espresso.IdlingResource;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.akat.filmreel.R;
import com.akat.filmreel.data.network.PeriodicSyncWorker;
import com.akat.filmreel.util.NetworkReceiver;
import com.akat.filmreel.util.SimpleIdlingResource;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource idlingResource;

    private NetworkReceiver receiver = new NetworkReceiver();

    private NavController navController;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Sync
        createNotificationChannels();
        scheduleSync();

        // Connectivity
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(drawerLayout.getWindowToken(), 0);
        }

        return NavigationUI.navigateUp(navController, drawerLayout);
    }

    private void scheduleSync() {
        WorkManager workManager = WorkManager.getInstance(this);

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

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }
}
