package com.princeparadoxes.watertracker.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.StringRes;
import android.util.Property;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Danil on 02.03.2016.
 */
public class AnimatorUtils {
    private static final int DEFAULT_DURATION = 300;
    private static float preveous;

    private AnimatorUtils() {
    }

    public static Animator createChangeTextAnimatorSet(TextView textView,
                                                       boolean withDelay,
                                                       @StringRes int... texts) {
        AnimatorSet set = new AnimatorSet();
        Animator[] animators = new Animator[texts.length];
        for (int i = 0; i < texts.length; i++) {
            animators[i] = createChangeTextAnimator(textView, withDelay, texts[i]);
        }
        set.playSequentially(animators);
        return set;
    }

    public static Animator createChangeTextAnimator(TextView textView,
                                                    boolean withDelay,
                                                    @StringRes int text) {
        AnimatorSet set = new AnimatorSet();
        Animator out = AnimatorUtils.alphaAnimator(1, 0, textView, 1000, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setText(text);
            }
        });
        if (withDelay) {
            out.setStartDelay(1000);
        }
        out.setInterpolator(new AccelerateInterpolator());
        Animator in = AnimatorUtils.alphaAnimator(0, 1, textView, 1000);
        in.setInterpolator(new DecelerateInterpolator());
        set.playSequentially(out, in);
        return set;
    }

    public static Animator translationYAnimator(int end, final View view) {
        return translationYAnimator(view.getTranslationY(), end, view, DEFAULT_DURATION);
    }

    public static Animator translationYAnimator(final float start, int end, final View view, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, end);
        animator.setDuration(duration);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setTranslationY(start);
            }
        });
        return animator;
    }

    public static Animator heightAnimator(int start, int end, View view) {
        return heightAnimator(start, end, view, DEFAULT_DURATION);
    }

    public static Animator heightAnimator(int start, int end, final View view, int duration) {
        ValueAnimator heightAnimator = ValueAnimator.ofInt(start, end);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LayoutParamsUtils.setHeight(view, valueAnimator.getAnimatedValue());
            }
        });
        heightAnimator.setDuration(duration);
        heightAnimator.addListener(getLayerTypeListener(view));
        return heightAnimator;
    }

    public static Animator scrollToAnimator(int start, int end, View view) {
        return scrollToAnimator(start, end, view, DEFAULT_DURATION);
    }

    public static Animator scrollToAnimator(int start, int end, final View view, int duration) {
        ValueAnimator scrollAnimator = ValueAnimator.ofInt(start, end);
        scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.scrollTo(0, (int) valueAnimator.getAnimatedValue());
            }
        });
        scrollAnimator.setDuration(duration);
        scrollAnimator.addListener(getLayerTypeListener(view));
        return scrollAnimator;
    }

    public static Animator scrollByAnimator(int end, View view) {
        return scrollByAnimator(end, view, DEFAULT_DURATION);
    }

    public static Animator scrollByAnimator(int end, final View view, int duration) {
        ValueAnimator scrollAnimator = ValueAnimator.ofFloat(0, end);
        preveous = 0;
        scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                view.scrollBy(0, (int) (value - preveous));
                preveous = value;
            }
        });
        scrollAnimator.setDuration(duration);
        scrollAnimator.addListener(getLayerTypeListener(view));
        return scrollAnimator;
    }

    public static Animator alphaAnimator(int start, int end, View view) {
        return alphaAnimator(start, end, view, DEFAULT_DURATION, null);
    }

    public static Animator alphaAnimator(int start, int end, View view,
                                         AnimatorListenerAdapter listener) {
        return alphaAnimator(start, end, view, DEFAULT_DURATION, listener);
    }

    public static Animator alphaAnimator(int start, int end, View view, int duration) {
        return alphaAnimator(start, end, view, duration, null);
    }

    public static Animator alphaAnimator(int start, int end, View view, int duration,
                                         AnimatorListenerAdapter listener) {
        ValueAnimator alphaAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, start, end);
        alphaAnimator.setDuration(duration);
        alphaAnimator.addListener(getLayerTypeListener(view));
        if (listener != null) alphaAnimator.addListener(listener);
        return alphaAnimator;
    }

    public static ValueAnimator fluctuationAnimator(int orientation, int value, View view,
                                                    int duration) {
        Property property;
        switch (orientation) {
            case LinearLayout.HORIZONTAL:
                property = View.TRANSLATION_X;
                break;
            case LinearLayout.VERTICAL:
            default:
                property = View.TRANSLATION_Y;
                break;
        }
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(view, property.getName(), -value,
                value);
        valueAnimator.setInterpolator(new CycleInterpolator(4));
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animation.removeListener(this);
                ((ValueAnimator) animation).reverse();
            }
        });
        valueAnimator.setDuration(duration);
        return valueAnimator;
    }

    private static Animator.AnimatorListener getLayerTypeListener(final View view) {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setLayerType(View.LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                view.setLayerType(View.LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Animator animateRevealColorFromCoordinates(ViewGroup viewRoot, int x, int y) {
        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
        anim.setDuration(DEFAULT_DURATION);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        return anim;
    }
}
