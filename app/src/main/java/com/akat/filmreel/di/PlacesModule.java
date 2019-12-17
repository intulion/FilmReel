package com.akat.filmreel.di;

import android.util.Base64;

import com.akat.filmreel.BuildConfig;
import com.akat.filmreel.places.PlacesApiService;
import com.akat.filmreel.util.Constants;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class PlacesModule {

    @Provides
    @ApplicationScope
    @Named("places")
    OkHttpClient provideOkHttp() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    byte[] apiKey_64 = Base64.decode(BuildConfig.PlacesApiKey, Base64.DEFAULT);
                    String apiKey = new String(apiKey_64);

                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter("key", apiKey)
                            .build();

                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();
    }

    @Provides
    @ApplicationScope
    @Named("places")
    Retrofit provideRetrofit(@Named("places") OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Constants.HTTP.PLACES_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @ApplicationScope
    PlacesApiService providePlacesApi(@Named("places") Retrofit retrofit) {
        return retrofit.create(PlacesApiService.class);
    }
}
