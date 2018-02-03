package com.princeparadoxes.watertracker.data.api;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.princeparadoxes.watertracker.ApplicationScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public final class RestModule {

    @Provides
    @ApplicationScope
    Retrofit provideRetrofit(HttpUrl baseUrl, @Named("Api") OkHttpClient client, Gson gson) {
        return new Retrofit.Builder() //
                .client(client) //
                .baseUrl(baseUrl) //
                .addConverterFactory(GsonConverterFactory.create(gson)) //
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //
                .build();
    }

    static OkHttpClient.Builder createApiClient(OkHttpClient client) {
        return client.newBuilder();
    }
}
