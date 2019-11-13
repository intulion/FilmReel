package com.akat.filmreel.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    private static final String LAST_PAGE = "last_page";
    private static final String TOTAL_PAGES = "total_pages";

    public static void setPageData(Context context, int lastPage, int totalPages) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(LAST_PAGE, lastPage);
        editor.putInt(TOTAL_PAGES, totalPages);
        editor.apply();
    }

    public static int getLastPage(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(LAST_PAGE, 0);
    }

    public static int getTotalPages(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(TOTAL_PAGES, 1);
    }
}
