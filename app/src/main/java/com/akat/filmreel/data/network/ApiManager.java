package com.akat.filmreel.data.network;

import com.akat.filmreel.BuildConfig;
import com.akat.filmreel.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static final Object LOCK = new Object();
    private static ApiManager sInstance;
    private final ApiService apiService;

    private ApiManager(ApiService service) {
        this.apiService = service;
    }

    public static ApiManager getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {

                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .addInterceptor(chain -> {
                            Request original = chain.request();
                            HttpUrl originalHttpUrl = original.url();

                            HttpUrl url = originalHttpUrl.newBuilder()
                                    .addQueryParameter("api_key", BuildConfig.ApiKey)
                                    .build();

                            Request.Builder requestBuilder = original.newBuilder()
                                    .url(url);

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        })
                        .build();

                Gson gson = new GsonBuilder()
                        .setDateFormat(Constants.HTTP.DATE_FORMAT)
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.HTTP.BASE_URL)
                        .client(httpClient)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                sInstance = new ApiManager(retrofit.create(ApiService.class));
            }
        }
        return sInstance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
