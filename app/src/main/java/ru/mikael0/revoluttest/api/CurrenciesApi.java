package ru.mikael0.revoluttest.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.mikael0.revoluttest.model.RatesSnapshot;

public interface CurrenciesApi {
    @GET("latest")
    Call<RatesSnapshot> getRates(@Query("base") String baseName);
}