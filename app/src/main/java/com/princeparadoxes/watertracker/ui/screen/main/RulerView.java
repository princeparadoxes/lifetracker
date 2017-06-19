package com.princeparadoxes.watertracker.ui.screen.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.princeparadoxes.watertracker.R;


public class RulerView extends FrameLayout {

    private LinearLayout mViewContainer;

    public RulerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.ruler_view, this);
        findViews();
        initViews();
    }

    private void findViews() {
        mViewContainer = (LinearLayout) findViewById(R.id.ruler_view_container);
    }

    private void initViews() {
        for (int i = 50; i <= 500; i += 50) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.ruler_item_view, null);
            ((TextView) view.findViewById(R.id.ruler_item_text)).setText(i + " ml");
            View line = view.findViewById(R.id.ruler_item_line);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) line.getLayoutParams();
            int id = i % 100 == 0 ? R.dimen.ruler_long_line : R.dimen.ruler_short_line;
            int width = getContext().getResources().getDimensionPixelOffset(id);
            params.width = width;
            line.setLayoutParams(params);
            mViewContainer.addView(view);
        }
    }

}
