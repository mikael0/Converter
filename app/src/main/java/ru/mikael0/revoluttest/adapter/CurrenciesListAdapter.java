package ru.mikael0.revoluttest.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;

import ru.mikael0.revoluttest.R;
import ru.mikael0.revoluttest.annotaions.FragmentScope;
import ru.mikael0.revoluttest.model.Currency;
import ru.mikael0.revoluttest.model.CurrencyList;
import ru.mikael0.revoluttest.model.RatesSnapshot;
import ru.mikael0.revoluttest.ui.picasso.CircleTransform;
import ru.mikael0.revoluttest.ui.view.WatchableEditText;

@FragmentScope
public class CurrenciesListAdapter extends RecyclerView.Adapter<CurrenciesListAdapter.CurrencyViewHolder> {

    private static final String BASE_FLAG_URL = "https://raw.githubusercontent.com/transferwise/currency-flags/master/src/flags/";

    @NonNull
    private CurrencyList data = new CurrencyList();

    @Nullable
    private RatesSnapshot actualRates;

    @NonNull
    private Context context;
    @NonNull
    private Picasso picasso;
    @NonNull
    private CurrenciesLoader loader;
    @NonNull
    private HashMap<String, String> currencyNameMap;
    @NonNull
    private Handler mainHandler;
    @NonNull
    private Executor workerExecutor;
    @Nullable
    private RecyclerView.ItemAnimator itemAnimator;

    @Inject
    public CurrenciesListAdapter(@NonNull @Named("activity_context") Context context,
                                 @NonNull Picasso picasso,
                                 @NonNull CurrenciesLoader loader,
                                 @NonNull Executor workerExecutor) {
        this.context = context;
        this.picasso = picasso;
        this.loader = loader;
        this.workerExecutor = workerExecutor;
        currencyNameMap = parseCurrencies();

        loader.setRefreshListener(refreshListener);
        loader.start();

        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void destroy() {
        loader.stop();
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.currency_item, parent, false);
        return new CurrencyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setItemAnimator(@NonNull RecyclerView.ItemAnimator itemAnimator) {
        this.itemAnimator = itemAnimator;
    }

    public class CurrencyViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private ImageView logo;
        @NonNull
        private TextView name;
        @NonNull
        private TextView longName;
        @NonNull
        private WatchableEditText amount;

        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);

            logo = itemView.findViewById(R.id.currency_logo);
            name = itemView.findViewById(R.id.currency_name);
            longName = itemView.findViewById(R.id.currency_long_name);
            amount = itemView.findViewById(R.id.currency_amount);
        }

        public void bind(@NonNull Currency item) {
            if (actualRates == null) {
                return;
            }

            Uri flagUri = Uri.parse(BASE_FLAG_URL)
                    .buildUpon()
                    .appendPath(item.getName().toLowerCase() + ".png")
                    .build();
            int logoSize = context.getResources().getDimensionPixelSize(R.dimen.logo_size);
            picasso.load(flagUri)
                    .resize(logoSize, 0)
                    .transform(new CircleTransform())
                    .into(logo);
            name.setText(item.getName());
            longName.setText(currencyNameMap.get(item.getName()));
            for (TextWatcher watcher : amount.getWatchers()) {
                amount.removeTextChangedListener(watcher);
            }
            amount.setText(String.format("%.3f", item.getAmount()));
            amount.setOnFocusChangeListener((v, focus) -> {
                //to allow animation start if scheduled
                mainHandler.post(() -> {
                    if (itemAnimator != null) {
                        itemAnimator.isRunning(() -> {
                            loader.restart(item.getName());
                        });
                    } else {
                        loader.restart(item.getName());
                    }
                });
            });
            amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    amount.removeTextChangedListener(this);
                    String str = s.toString().replace(",", ".");
                    workerExecutor.execute(() -> {
                        item.setAmount(str.isEmpty() ? 0 : Double.parseDouble(str));
                        data.update(actualRates, item, updatedDelegate);
                    });
                    amount.addTextChangedListener(this);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            itemView.setOnClickListener( v -> {
                int oldPos = data.move(item, 0);
                notifyItemMoved(oldPos, 0);
                amount.requestFocus();
            });
        }
    }

    private HashMap<String, String> parseCurrencies() {
        String[] stringArray = context.getResources().getStringArray(R.array.currency_names);
        HashMap<String, String> outputMap = new HashMap<>(stringArray.length);
        for (String entry : stringArray) {
            String[] splitResult = entry.split("\\|", 2);
            outputMap.put(splitResult[0], splitResult[1]);
        }
        return outputMap;
    }

    private CurrencyList.ItemUpdatedDelegate updatedDelegate = position -> {
        mainHandler.post(() -> {
            notifyItemChanged(position);
        });
    };

    private CurrenciesLoader.Listener refreshListener = rates -> {
        actualRates = rates;
        data.updateFromSnapshot(actualRates, updatedDelegate);
    };
}
