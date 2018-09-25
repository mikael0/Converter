package ru.mikael0.revoluttest.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.mikael0.revoluttest.R;
import ru.mikael0.revoluttest.di.component.CurrenciesActivityComponent;
import ru.mikael0.revoluttest.di.component.DaggerCurrenciesActivityComponent;
import ru.mikael0.revoluttest.di.modules.ContextModule;
import ru.mikael0.revoluttest.di.modules.CurrenciesModule;
import ru.mikael0.revoluttest.di.modules.OkHttpClientModule;
import ru.mikael0.revoluttest.di.modules.PicassoModule;

public class MainActivity extends AppCompatActivity {

    @NonNull
    private CurrenciesActivityComponent appComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appComponent = DaggerCurrenciesActivityComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .currenciesModule(new CurrenciesModule())
                .okHttpClientModule(new OkHttpClientModule())
                .picassoModule(new PicassoModule())
                .build();

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new CurrenciesFragment())
                .commit();
    }

    public CurrenciesActivityComponent getAppComponent() {
        return appComponent;
    }
}
