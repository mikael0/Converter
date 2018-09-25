package ru.mikael0.revoluttest.di.modules;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mikael0.revoluttest.annotaions.ApplicationScope;
import ru.mikael0.revoluttest.api.CurrenciesApi;

@Module(includes = OkHttpClientModule.class)
public class CurrenciesModule {

    @Provides
    @ApplicationScope
    public CurrenciesApi currenciesApi(Retrofit retrofit){
        return retrofit.create(CurrenciesApi.class);
    }

    @ApplicationScope
    @Provides
    public Retrofit retrofit(OkHttpClient okHttpClient,
                             Executor workerExecutor,
                             GsonConverterFactory gsonConverterFactory,
                             Gson gson){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://revolut.duckdns.org/")
                .callbackExecutor(workerExecutor)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }
}