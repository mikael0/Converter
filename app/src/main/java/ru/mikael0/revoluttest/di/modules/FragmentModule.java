package ru.mikael0.revoluttest.di.modules;

import android.support.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;

import dagger.Module;
import dagger.Provides;
import ru.mikael0.revoluttest.adapter.CurrenciesListAdapter;
import ru.mikael0.revoluttest.adapter.CurrenciesLoader;
import ru.mikael0.revoluttest.annotaions.FragmentScope;
import ru.mikael0.revoluttest.api.CurrenciesApi;
import ru.mikael0.revoluttest.ui.CurrenciesFragment;

@Module
public class FragmentModule {

    private final CurrenciesFragment fragment;

    public FragmentModule(@NonNull CurrenciesFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    public CurrenciesFragment currenciesFragment() {
        return fragment;
    }

    @Provides
    @FragmentScope
    public CurrenciesListAdapter adapter(Picasso picasso,
                                         CurrenciesLoader loader,
                                         Executor executor) {
        return new CurrenciesListAdapter(fragment.getContext(), picasso, loader, executor);
    }

    @Provides
    @FragmentScope
    public CurrenciesLoader loader(CurrenciesApi currenciesApi) {
        return new CurrenciesLoader(currenciesApi);
    }
}
