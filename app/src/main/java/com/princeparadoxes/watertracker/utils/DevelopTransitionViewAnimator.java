package com.princeparadoxes.watertracker.utils;

import android.content.Context;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;

import com.livetyping.library.interfaces.CannyTransition;

/**
 * Created by Danil on 16.07.2016.
 */
public class DevelopTransitionViewAnimator extends DevelopViewAnimator {
    private CannyTransition cannyTransition;
    private Transition transition;

    public DevelopTransitionViewAnimator(Context context) {
        super(context);
    }

    public DevelopTransitionViewAnimator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void changeVisibility(View inChild, View outChild, boolean withAnim) {
        if (withAnim) {
            prepareTransition(inChild, outChild);
            startTransition();
        }
        super.changeVisibility(inChild, outChild, withAnim);
    }

    protected void prepareTransition(View inChild, View outChild) {
        if (cannyTransition != null) {
            transition = cannyTransition.getTransition(inChild, outChild);
        }
    }

    public void startTransition() {
        if (transition != null) {
            TransitionManager.beginDelayedTransition(this, transition);
        }
    }

    public void setCannyTransition(CannyTransition cannyTransition) {
        this.cannyTransition = cannyTransition;
    }
}
