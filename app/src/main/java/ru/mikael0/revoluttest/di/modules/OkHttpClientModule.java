package ru.mikael0.revoluttest.di.modules;

import android.content.Context;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import ru.mikael0.revoluttest.annotaions.ApplicationContext;
import ru.mikael0.revoluttest.annotaions.ApplicationScope;

@Module(includes = ContextModule.class)
public class OkHttpClientModule {

    @Provides
    public OkHttpClient okHttpClient(Cache cache){
        return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .build();
    }

    @Provides
    public Cache cache(File cacheFile){
        return new Cache(cacheFile, 10 * 1000 * 1000); //10 MB
    }

    @Provides
    @ApplicationScope
    public File file(@ApplicationContext Context context){
        File file = new File(context.getCacheDir(), "HttpCache");
        file.mkdirs();
        return file;
    }
}