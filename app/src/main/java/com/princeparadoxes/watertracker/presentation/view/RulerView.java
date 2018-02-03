package com.princeparadoxes.watertracker.presentation.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.princeparadoxes.watertracker.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class RulerView extends FrameLayout {

    private LinearLayout mViewContainer;
    private Map<Integer, Integer> mOffsetValueMap = new HashMap<>();

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
        mOffsetValueMap.put(0, 0);
        for (int i = 50; i <= 500; i += 50) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.ruler_item_view, null);
            View line = view.findViewById(R.id.ruler_item_line);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) line.getLayoutParams();
            if (i % 100 == 0) {
                ((TextView) view.findViewById(R.id.ruler_item_text)).setText(i + " ml");
                params.width = getContext().getResources().getDimensionPixelOffset(R.dimen.ruler_long_line);
//                params.height = (int) DimenTools.pxFromDp(getContext(), 2);
            } else {
                params.width = getContext().getResources().getDimensionPixelOffset(R.dimen.ruler_short_line);
//                params.height = (int) DimenTools.pxFromDp(getContext(), 1);
            }
            line.setLayoutParams(params);
            mViewContainer.addView(view);
            int finalI = i;
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    mOffsetValueMap.put((int) view.getY() + (view.getHeight() / 2), finalI);
                    return false;
                }
            });
        }
    }

    public int getNearestValue(int offset) {

        Iterator<Integer> iterator = mOffsetValueMap.keySet().iterator();
        int nearest = 0;
        for (; iterator.hasNext(); ) {
            int mapOffset = iterator.next();
            if (mapOffset < offset && mapOffset > nearest) {
                nearest = mapOffset;
            }
        }
//        int nearest = iterator.next();
//        float delta = Math.abs(nearest - offset);
//        for (; iterator.hasNext(); ) {
//            int mapOffset = iterator.next();
//            float tempDelta = Math.abs(mapOffset - offset);
//            if (tempDelta < delta && mapOffset > offset) {
//                nearest = mapOffset;
//                delta = tempDelta;
//            }
//        }
        return mOffsetValueMap.get(nearest);
    }

}
