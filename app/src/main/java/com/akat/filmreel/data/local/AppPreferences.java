package com.akat.filmreel.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppPreferences implements Preferences {

    private static final String LAST_PAGE = "last_page";
    private static final String TOTAL_PAGES = "total_pages";

    private final Context context;

    @Inject
    public AppPreferences(Context context) {
        this.context = context;
    }

    @Override
    public void setPageData(int lastPage, int totalPages) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(LAST_PAGE, lastPage);
        editor.putInt(TOTAL_PAGES, totalPages);
        editor.apply();
    }

    @Override
    public int getLastPage() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(LAST_PAGE, 0);
    }

    @Override
    public int getTotalPages() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(TOTAL_PAGES, 1);
    }

    @Override
    public String getLocale() {
        Locale currentLocale = getCurrentLocale(context);
        if (currentLocale.getLanguage().equals("ru")) {
            return "ru-RU";
        }
        return "en-US";
    }

    private Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }
}
