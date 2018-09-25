package ru.mikael0.revoluttest.di.modules;

import android.app.Activity;
import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.mikael0.revoluttest.annotaions.ApplicationScope;

@Module
public class ActivityModule {

    private final Context context;

    ActivityModule(Activity context){
        this.context = context;
    }

    @Named("activity_context")
    @ApplicationScope
    @Provides
    public Context context(){
        return context;
    }
}