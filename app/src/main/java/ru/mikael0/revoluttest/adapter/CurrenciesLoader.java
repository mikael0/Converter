package ru.mikael0.revoluttest.adapter;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.os.Handler;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mikael0.revoluttest.api.CurrenciesApi;
import ru.mikael0.revoluttest.model.RatesSnapshot;

public class CurrenciesLoader {

    private static final int REFRESH_INTERVAL_MS = 1000;

    @NonNull
    private CurrenciesApi currenciesApi;
    @NonNull
    private Runnable refreshRunnable = this::refresh;
    @NonNull
    private Handler mainHandler;
    @Nullable
    private Listener listener;
    @NonNull
    private String base;
    @Nullable
    Call<RatesSnapshot> call;

    @Inject
    public CurrenciesLoader(
            @NonNull CurrenciesApi currenciesApi) {
        this.currenciesApi = currenciesApi;
        mainHandler = new Handler(Looper.getMainLooper());
        base = "EUR";
    }

    public void setRefreshListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    public void start() {
        mainHandler.post(refreshRunnable);
    }

    public void restart(@NonNull String base) {
        stop();
        this.base = base;
        start();
    }

    private void refresh() {
        call = currenciesApi.getRates(base);
        call.enqueue(new Callback<RatesSnapshot>() {
            @Override
            public void onResponse(Call<RatesSnapshot> call, Response<RatesSnapshot> response) {
                if (listener != null) {
                    listener.onRefresh(response.body());
                }
                mainHandler.postDelayed(refreshRunnable, REFRESH_INTERVAL_MS);
            }

            @Override
            public void onFailure(Call<RatesSnapshot> call, Throwable t) {
                mainHandler.postDelayed(refreshRunnable, REFRESH_INTERVAL_MS);
            }
        });
    }

    public void stop() {
        mainHandler.removeCallbacks(refreshRunnable);
        if (call != null) {
            call.cancel();
            call = null;
        }
    }

    public interface Listener {
        void onRefresh(@NonNull RatesSnapshot rates);
    }
}
