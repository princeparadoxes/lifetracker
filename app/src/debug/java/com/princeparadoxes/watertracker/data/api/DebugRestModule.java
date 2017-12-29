package com.princeparadoxes.watertracker.data.api;

import com.princeparadoxes.watertracker.ApplicationScope;
import com.princeparadoxes.watertracker.data.consts.Consts;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import timber.log.Timber;

@Module(includes = RestModule.class)
public final class DebugRestModule {

    @Provides
    @ApplicationScope
    @Named("EndPoint")
    String provideEndpoint() {
        return Consts.REST_DEBUG_API_ENDPOINT;
    }

    @Provides
    @ApplicationScope
    HttpUrl provideHttpUrl(@Named("EndPoint") String endpoint) {
        return HttpUrl.parse(endpoint);
    }

    @Provides
    @ApplicationScope
    CurlLoggingInterceptor provideCurlLoggingInterceptor() {
        return new CurlLoggingInterceptor(message -> Timber.tag("Curl").v(message));
    }

    @Provides
    @ApplicationScope
    HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").v(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @ApplicationScope
    @Named("Api")
    OkHttpClient provideApiClient(OkHttpClient client,
                                  HttpLoggingInterceptor httpLoggingInterceptor,
                                  CurlLoggingInterceptor curlLoggingInterceptor) {
        return RestModule.createApiClient(client)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(curlLoggingInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    @Provides
    @ApplicationScope
    RestService provideRestService(Retrofit retrofit) {
        return retrofit.create(RestService.class);
    }

}
