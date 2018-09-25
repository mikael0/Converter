package ru.mikael0.revoluttest.model;

import android.support.annotation.NonNull;

public class Currency {
    @NonNull
    private String name;
    
    private double amount;
    
    public Currency(@NonNull String name,
                    double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
