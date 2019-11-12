package com.akat.filmreel.data.network;

import com.akat.filmreel.BuildConfig;
import com.akat.filmreel.util.Constants;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacesApiManager {

    private static final Object LOCK = new Object();
    private static PlacesApiManager sInstance;
    private final PlacesApiService apiService;

    private PlacesApiManager(PlacesApiService service) {
        this.apiService = service;
    }

    public static PlacesApiManager getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {

                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .addInterceptor(chain -> {
                            Request original = chain.request();
                            HttpUrl originalHttpUrl = original.url();

                            HttpUrl url = originalHttpUrl.newBuilder()
                                    .addQueryParameter("key", BuildConfig.PlacesApiKey)
                                    .build();

                            Request.Builder requestBuilder = original.newBuilder()
                                    .url(url);

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        })
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.HTTP.PLACES_URL)
                        .client(httpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                sInstance = new PlacesApiManager(retrofit.create(PlacesApiService.class));
            }
        }
        return sInstance;
    }

    public PlacesApiService getApiService() {
        return apiService;
    }

}
