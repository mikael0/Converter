package ru.mikael0.revoluttest.di.component;


import android.app.Activity;

import com.squareup.picasso.Picasso;

import dagger.Component;
import dagger.Provides;
import dagger.Subcomponent;
import ru.mikael0.revoluttest.adapter.CurrenciesListAdapter;
import ru.mikael0.revoluttest.annotaions.ApplicationScope;
import ru.mikael0.revoluttest.api.CurrenciesApi;
import ru.mikael0.revoluttest.di.modules.CurrenciesModule;
import ru.mikael0.revoluttest.di.modules.FragmentModule;
import ru.mikael0.revoluttest.di.modules.PicassoModule;
import ru.mikael0.revoluttest.ui.CurrenciesFragment;

@ApplicationScope
@Component(modules = {CurrenciesModule.class, PicassoModule.class})
public interface CurrenciesActivityComponent {
    CurrenciesApi getCurrenciesService();
    Picasso getPicasso();

    CurrenciesFragmentComponent getFragmentComponent(FragmentModule fragmentModule);

    void inject(Activity activity);
}