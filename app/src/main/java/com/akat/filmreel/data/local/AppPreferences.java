package com.akat.filmreel.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.akat.filmreel.di.ApplicationScope;

import java.util.Locale;

import javax.inject.Inject;

@ApplicationScope
public class AppPreferences implements Preferences {

    private static final String LAST_PAGE = "last_page_";
    private static final String TOTAL_PAGES = "total_pages_";

    private final Context context;

    @Inject
    public AppPreferences(Context context) {
        this.context = context;
    }

    @Override
    public void setPageData(int pageType, int lastPage, int totalPages) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(LAST_PAGE + pageType, lastPage);
        editor.putInt(TOTAL_PAGES + pageType, totalPages);
        editor.apply();
    }

    @Override
    public int getLastPage(int pageType) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(LAST_PAGE + pageType, 0);
    }

    @Override
    public int getTotalPages(int pageType) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(TOTAL_PAGES + pageType, 1);
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
