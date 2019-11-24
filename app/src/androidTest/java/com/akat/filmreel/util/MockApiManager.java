package com.akat.filmreel.util;

import com.akat.filmreel.data.network.ApiManager;
import com.akat.filmreel.data.network.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MockApiManager extends ApiManager {

    private static Gson gson = new GsonBuilder()
            .setDateFormat(Constants.HTTP.DATE_FORMAT)
            .create();

    public MockApiManager(HttpUrl baseUrl) {
        super(new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService.class)
        );
    }
}
