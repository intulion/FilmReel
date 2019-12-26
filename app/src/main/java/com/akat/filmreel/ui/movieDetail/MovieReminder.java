package com.akat.filmreel.ui.movieDetail;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.akat.filmreel.R;
import com.akat.filmreel.util.Constants;

public class MovieReminder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotification(context,
                context.getString(R.string.reminder_title),
                intent.getStringExtra(Constants.PARAM.MOVIE_TITLE),
                intent.getLongExtra(Constants.PARAM.MOVIE_ID, -1)
        );
    }

    private void sendNotification(Context context, String messageTitle, String messageBody, long movieId) {
        if (movieId == -1) return;

        Bundle args = new Bundle();
        args.putLong(Constants.PARAM.MOVIE_ID, movieId);

        PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.fragment_movie_detail)
                .setArguments(args)
                .createPendingIntent();

        String channelId = context.getString(R.string.default_notification_channel_id);
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
