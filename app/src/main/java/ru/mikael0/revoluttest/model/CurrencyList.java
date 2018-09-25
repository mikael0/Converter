package ru.mikael0.revoluttest.model;

import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CurrencyList {

    @NonNull
    private ArrayList<Currency> list;
    @NonNull
    private HashMap<String, Currency> nameMap;

    public CurrencyList() {
       list = new ArrayList<>();
       nameMap = new HashMap<>();
    }

    @WorkerThread
    public synchronized void updateFromSnapshot(@NonNull RatesSnapshot snapshot,
                                      @Nullable ItemUpdatedDelegate updatedDelegate) {

        Currency base;
        if (nameMap.get(snapshot.getBase()) != null) {
            base = nameMap.get(snapshot.getBase());
        } else {
            base = new Currency(snapshot.getBase(), 1);
            add(base);
            if (updatedDelegate != null) {
                updatedDelegate.onItemUpdated(list.size() - 1);
            }
        }
        for (Map.Entry<String, Double> entry : snapshot.getRates().entrySet()) {
            if (nameMap.get(entry.getKey()) != null) {
                Currency cur = nameMap.get(entry.getKey());
                double newAmount = base.getAmount() * entry.getValue();
                if (newAmount != cur.getAmount()) {
                    cur.setAmount(newAmount);
                    if (updatedDelegate != null) {
                        updatedDelegate.onItemUpdated(list.indexOf(cur));
                    }
                }
            } else {
                add(new Currency(entry.getKey(), entry.getValue()));
                if (updatedDelegate != null) {
                    updatedDelegate.onItemUpdated(list.size() - 1);
                }
            }
        }
    }

    @WorkerThread
    public synchronized void update(@NonNull RatesSnapshot snapshot,
                          @NonNull Currency masterCurrency,
                          @Nullable ItemUpdatedDelegate updatedDelegate) {

        for (Currency currency : list) {
            if (currency.getName().equals(masterCurrency.getName())) {
                continue;
            }

            double factor = 1;
            if (!currency.getName().equals(snapshot.getBase())) {
                factor = snapshot.getRates().get(currency.getName());
            }
            double baseFactor = 1;
            if (!masterCurrency.getName().equals(snapshot.getBase())) {
                baseFactor = 1 / snapshot.getRates().get(masterCurrency.getName()) ;
            }

            double newAmount = masterCurrency.getAmount() * baseFactor * factor;
            if (currency.getAmount() != newAmount) {
                currency.setAmount(newAmount);
                if (updatedDelegate != null) {
                    updatedDelegate.onItemUpdated(list.indexOf(currency));
                }
            }
        }
    }

    @AnyThread
    public synchronized void add(@NonNull Currency currency) {
        if (nameMap.get(currency.getName()) != null) {
            return;
        }

        list.add(currency);
        nameMap.put(currency.getName(), currency);
    }

    @AnyThread
    public synchronized void add(int position, @NonNull Currency currency) {
        if (nameMap.get(currency.getName()) != null) {
            return;
        }

        list.add(position, currency);
        nameMap.put(currency.getName(), currency);
    }

    @AnyThread
    public synchronized Currency get(int position) {
        return list.get(position);
    }

    @AnyThread
    public synchronized int move(@NonNull Currency currency, int toPosition) {
        if (nameMap.get(currency.getName()) == null) {
            return -1;
        }

        int oldPos = list.indexOf(currency);
        list.remove(oldPos);
        list.add(toPosition, currency);

        return oldPos;
    }

    @AnyThread
    public synchronized int size() {
        return list.size();
    }

    public interface ItemUpdatedDelegate {
        void onItemUpdated(int position);
    }
}
