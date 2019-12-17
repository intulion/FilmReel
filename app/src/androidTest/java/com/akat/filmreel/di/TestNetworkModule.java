package com.akat.filmreel.di;

import com.akat.filmreel.data.network.ApiService;
import com.akat.filmreel.util.NetworkUtils;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class TestNetworkModule {

    private HttpUrl baseUrl;

    public TestNetworkModule(HttpUrl baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(NetworkUtils.getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    ApiService provideApi(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}