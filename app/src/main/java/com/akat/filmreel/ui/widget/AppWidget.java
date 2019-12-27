package com.akat.filmreel.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.navigation.NavDeepLinkBuilder;

import com.akat.filmreel.MovieApplication;
import com.akat.filmreel.R;
import com.akat.filmreel.data.domain.IMovieRepository;
import com.akat.filmreel.data.model.Movie;
import com.akat.filmreel.util.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AppWidget extends AppWidgetProvider {

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    IMovieRepository repository;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId, Movie movie) {

        if (movie == null || movie.getBackdropPath() == null) return;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.appwidget_text, movie.getTitle());
        views.setTextViewText(R.id.appwidget_rating,
                context.getString(R.string.movie_rating_format,
                        movie.getVoteAverage(),
                        movie.getVoteCount())
        );

        showBackdropImg(context, appWidgetId, views, movie.getBackdropPath());

        // Open movie details screen
        Bundle args = new Bundle();
        args.putLong(Constants.PARAM.MOVIE_ID, movie.getId());

        PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.fragment_movie_detail)
                .setArguments(args)
                .createPendingIntent();
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void showBackdropImg(Context context, int appWidgetId, RemoteViews views, String backdropPath) {
        AppWidgetTarget widgetTarget = new AppWidgetTarget(context, R.id.appwidget_img, views, appWidgetId) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };

        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(Constants.HTTP.BACKDROP_URL + backdropPath)
                .into(widgetTarget);
    }

    public AppWidget() {
        super();
        MovieApplication.getAppComponent().inject(this);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // Set most popular movie from "Now playing" list
            disposable.add(repository.getNowPlayingMovies()
                    .subscribeOn(Schedulers.io())
                    .firstOrError()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(values ->
                            updateAppWidget(context, appWidgetManager, appWidgetId, values.get(0)))
            );
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        disposable.clear();
    }
}
