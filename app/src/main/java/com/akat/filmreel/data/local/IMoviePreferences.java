package com.akat.filmreel.data.local;

public interface IMoviePreferences {
    void setPageData(int pageType, int lastPage, int totalPages);

    int getLastPage(int pageType);

    int getTotalPages(int pageType);

    String getLocale();
}
