package com.akat.filmreel.data.network;

import com.akat.filmreel.BuildConfig;
import com.akat.filmreel.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static final Object LOCK = new Object();
    private static ApiManager sInstance;
    private final ApiService apiService;

    protected ApiManager(ApiService service) {
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

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.HTTP.BASE_URL)
                        .client(httpClient)
                        .addConverterFactory(GsonConverterFactory.create(getGson()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

                sInstance = new ApiManager(retrofit.create(ApiService.class));
            }
        }
        return sInstance;
    }

    private static Gson getGson() {
        JsonDeserializer<Date> deserializer = new JsonDeserializer<Date>() {
            SimpleDateFormat format = new SimpleDateFormat(Constants.HTTP.DATE_FORMAT, Locale.getDefault());

            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json == null || json.getAsString().isEmpty()) {
                    return null;
                }

                try {
                    return format.parse(json.getAsString());
                } catch (ParseException e) {
                    return null;
                }
            }
        };

        return new GsonBuilder()
                .registerTypeAdapter(Date.class, deserializer)
                .create();
    }

    ApiService getApiService() {
        return apiService;
    }
}
