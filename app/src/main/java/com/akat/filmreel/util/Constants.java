package com.akat.filmreel.util;

public class Constants {

    public static final class HTTP {
        public static final String BASE_URL = "https://api.themoviedb.org/";
        private static final String IMAGE_URL = "https://image.tmdb.org/t/p/";
        public static final String POSTER_URL = IMAGE_URL + "w154";
        public static final String SMALL_POSTER_URL = IMAGE_URL + "w92";
        public static final String BACKDROP_URL = IMAGE_URL + "w500";
        public static final String DATE_FORMAT = "yyyy-MM-dd";

        public static final String PLACES_URL = "https://maps.googleapis.com/";
        public static final String PLACES_STATUS_OK = "OK";
        public static final String PLACES_STATUS_OVER = "OVER_QUERY_LIMIT";
        public static final String PLACES_STATUS_DENIED = "REQUEST_DENIED";
    }

    public static final class PARAM {
        private static final String PACKAGE = "com.akat.filmreel.";
        public static final String MOVIE_ID = PACKAGE + "movieId";
        public static final String MOVIE_TITLE = PACKAGE + "movieTitle";

        public static final String CINEMA_NAME = PACKAGE + "cinemaName";
        public static final String CINEMA_LAT = PACKAGE + "cinemaLat";
        public static final String CINEMA_LNG = PACKAGE + "cinemaLng";
        public static final String CURRENT_LAT = PACKAGE + "currentLat";
        public static final String CURRENT_LNG = PACKAGE + "currentLng";

        public static final String PAGER_POSITION = PACKAGE + "currentPager";
    }

    public static final class PAGER {
        public static final int NOW_PLAYING = 0;
        public static final int TOP_RATED = 1;
        public static final int POPULAR = 2;
        public static final int UPCOMING = 3;
    }
}
