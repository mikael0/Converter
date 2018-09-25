package ru.mikael0.revoluttest.di.component;


import dagger.Subcomponent;
import ru.mikael0.revoluttest.adapter.CurrenciesListAdapter;
import ru.mikael0.revoluttest.annotaions.FragmentScope;
import ru.mikael0.revoluttest.di.modules.FragmentModule;
import ru.mikael0.revoluttest.ui.CurrenciesFragment;

@FragmentScope
@Subcomponent(modules = {FragmentModule.class})
public interface CurrenciesFragmentComponent {

    CurrenciesListAdapter getAdapter();

    void inject(CurrenciesFragment fragment);
}
