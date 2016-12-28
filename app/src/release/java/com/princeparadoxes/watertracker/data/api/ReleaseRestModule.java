package com.princeparadoxes.watertracker.data.api;

import com.princeparadoxes.watertracker.ApplicationScope;
import com.princeparadoxes.watertracker.data.consts.Consts;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import timber.log.Timber;

@Module(includes = RestModule.class)
public final class ReleaseRestModule {

    @Provides
    @ApplicationScope
    @Named("EndPoint")
    String provideEndpoint() {
        return Consts.REST_RELEASE_API_ENDPOINT;
    }

    @Provides
    @ApplicationScope
    HttpUrl provideHttpUrl(@Named("EndPoint") String endpoint) {
        return HttpUrl.parse(endpoint);
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
    OkHttpClient provideApiClient(OkHttpClient client) {
        return RestModule.createApiClient(client).build();
    }

    @Provides
    @ApplicationScope
    RestService provideRestService(Retrofit retrofit) {
        return retrofit.create(RestService.class);
    }

}
