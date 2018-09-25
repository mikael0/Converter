package ru.mikael0.revoluttest.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import ru.mikael0.revoluttest.R;
import ru.mikael0.revoluttest.adapter.CurrenciesListAdapter;
import ru.mikael0.revoluttest.di.modules.FragmentModule;

public class CurrenciesFragment extends Fragment {

    @Nullable
    private RecyclerView list;
    @NonNull
    @Inject
    CurrenciesListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity)getActivity()).getAppComponent()
                .getFragmentComponent(new FragmentModule(this))
                .inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_currencies, container, false);
        list = root.findViewById(R.id.list);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerView.ItemAnimator itemAnimator = list.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        adapter.setItemAnimator(itemAnimator);
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        adapter.destroy();
    }
}
