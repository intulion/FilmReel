package com.akat.filmreel.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {

    private static final String LAST_PAGE = "last_page";
    private static final String TOTAL_PAGES = "total_pages";

    private static final Object LOCK = new Object();
    private static AppPreferences sInstance;
    private final Context context;

    public AppPreferences(Context context) {
        this.context = context;
    }

    public synchronized static AppPreferences getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppPreferences(context);
            }
        }
        return sInstance;
    }

    public void setPageData(int lastPage, int totalPages) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(LAST_PAGE, lastPage);
        editor.putInt(TOTAL_PAGES, totalPages);
        editor.apply();
    }

    public int getLastPage() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(LAST_PAGE, 0);
    }

    public int getTotalPages() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(TOTAL_PAGES, 1);
    }
}
