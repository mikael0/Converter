package ru.mikael0.revoluttest.di.modules;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;
import ru.mikael0.revoluttest.annotaions.ApplicationContext;
import ru.mikael0.revoluttest.annotaions.ApplicationScope;

@Module
public class ContextModule {

    Context context;

    public ContextModule(Context context){
        this.context = context;
    }

    @ApplicationContext
    @ApplicationScope
    @Provides
    public Context context(){
        return context.getApplicationContext();
    }

    @ApplicationScope
    @Provides
    public Executor workerExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}