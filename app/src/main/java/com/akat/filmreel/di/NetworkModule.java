package com.akat.filmreel.di;

import com.akat.filmreel.BuildConfig;
import com.akat.filmreel.data.network.ApiService;
import com.akat.filmreel.util.Constants;
import com.akat.filmreel.util.NetworkUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class NetworkModule {

    @Singleton
    @Provides
    OkHttpClient provideOkHttp() {
        return new OkHttpClient.Builder()
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
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Constants.HTTP.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(NetworkUtils.getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    ApiService provideApi(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
