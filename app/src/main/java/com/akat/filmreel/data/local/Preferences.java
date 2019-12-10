package com.akat.filmreel.data.local;

public interface Preferences {
    void setPageData(int lastPage, int totalPages);

    int getLastPage();

    int getTotalPages();

    String getLocale();
}
