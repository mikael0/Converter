package ru.mikael0.revoluttest.model;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.HashMap;

public class RatesSnapshot {

    @NonNull
    private String base;
    @NonNull
    private Date date;
    @NonNull
    private HashMap<String, Double> rates;

    public RatesSnapshot(@NonNull String base,
                         @NonNull Date date,
                         @NonNull HashMap<String, Double> rates) {
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    @NonNull
    public String getBase() {
        return base;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    @NonNull
    public HashMap<String, Double> getRates() {
        return rates;
    }
}
