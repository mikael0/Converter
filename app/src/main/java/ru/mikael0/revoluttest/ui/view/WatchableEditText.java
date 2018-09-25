package ru.mikael0.revoluttest.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.ArrayList;

public class WatchableEditText extends EditText {

    private final ArrayList<TextWatcher> watchers = new ArrayList<>();

    public WatchableEditText(Context context) {
        super(context);
    }

    public WatchableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WatchableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addTextChangedListener(@NonNull TextWatcher watcher) {
        super.addTextChangedListener(watcher);
        watchers.add(watcher);
    }

    @Override
    public void removeTextChangedListener(@NonNull TextWatcher watcher) {
        super.removeTextChangedListener(watcher);
        watchers.remove(watcher);
    }

    public ArrayList<TextWatcher> getWatchers() {
        return watchers;
    }
}
