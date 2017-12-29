package com.princeparadoxes.watertracker.utils;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Danil on 02.03.2016.
 */
public class LayoutParamsUtils {

    public static void setHeight(View view, Object height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams(layoutParams);
    }

    public static void setMargin(View view, int margin) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) layoutParams).setMargins(margin, margin, margin, margin);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                ((FrameLayout.LayoutParams) layoutParams).setMarginStart(margin);
                ((FrameLayout.LayoutParams) layoutParams).setMarginEnd(margin);
            }
        } else if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).setMargins(margin, margin, margin, margin);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                ((LinearLayout.LayoutParams) layoutParams).setMarginStart(margin);
                ((LinearLayout.LayoutParams) layoutParams).setMarginEnd(margin);
            }
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).setMargins(margin, margin, margin, margin);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                ((RelativeLayout.LayoutParams) layoutParams).setMarginStart(margin);
                ((RelativeLayout.LayoutParams) layoutParams).setMarginEnd(margin);
            }
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setMargin(View view, int marginTopBottom, int marginLeftRight) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) layoutParams).setMargins(marginLeftRight, marginTopBottom,
                    marginLeftRight, marginTopBottom);
        } else if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).setMargins(marginLeftRight, marginTopBottom,
                    marginLeftRight, marginTopBottom);
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).setMargins(marginLeftRight,
                    marginTopBottom, marginLeftRight, marginTopBottom);
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setMarginBottom(View view, int marginBottom) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) layoutParams).setMargins(
                    ((FrameLayout.LayoutParams) layoutParams).leftMargin,
                    ((FrameLayout.LayoutParams) layoutParams).topMargin,
                    ((FrameLayout.LayoutParams) layoutParams).rightMargin,
                    marginBottom);
        } else if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).setMargins(
                    ((LinearLayout.LayoutParams) layoutParams).leftMargin,
                    ((LinearLayout.LayoutParams) layoutParams).topMargin,
                    ((LinearLayout.LayoutParams) layoutParams).rightMargin,
                    marginBottom);
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).setMargins(
                    ((RelativeLayout.LayoutParams) layoutParams).leftMargin,
                    ((RelativeLayout.LayoutParams) layoutParams).topMargin,
                    ((RelativeLayout.LayoutParams) layoutParams).rightMargin,
                    marginBottom);
        }
        view.setLayoutParams(layoutParams);
    }

    public static void applyScreenHeightToViewWidth(View view, int verticalPadding) {
        Context context = view.getContext();
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int padding = (screenWidth - screenHeight) / 2;
        view.setPadding(padding, verticalPadding, padding, verticalPadding);
    }

    public static void setMarginLeft(View view, int marginLeft) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) layoutParams).setMargins(
                    marginLeft,
                    ((FrameLayout.LayoutParams) layoutParams).topMargin,
                    ((FrameLayout.LayoutParams) layoutParams).rightMargin,
                    ((FrameLayout.LayoutParams) layoutParams).bottomMargin);
        } else if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).setMargins(
                    marginLeft,
                    ((LinearLayout.LayoutParams) layoutParams).topMargin,
                    ((LinearLayout.LayoutParams) layoutParams).rightMargin,
                    ((LinearLayout.LayoutParams) layoutParams).bottomMargin);
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).setMargins(
                    marginLeft,
                    ((RelativeLayout.LayoutParams) layoutParams).topMargin,
                    ((RelativeLayout.LayoutParams) layoutParams).rightMargin,
                    ((RelativeLayout.LayoutParams) layoutParams).bottomMargin);
        }
        view.setLayoutParams(layoutParams);
    }

    public static void setMarginRight(View view, int marginRight) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) layoutParams).setMargins(
                    ((FrameLayout.LayoutParams) layoutParams).leftMargin,
                    ((FrameLayout.LayoutParams) layoutParams).topMargin,
                    marginRight,
                    ((FrameLayout.LayoutParams) layoutParams).bottomMargin);
        } else if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).setMargins(
                    ((LinearLayout.LayoutParams) layoutParams).leftMargin,
                    ((LinearLayout.LayoutParams) layoutParams).topMargin,
                    marginRight,
                    ((LinearLayout.LayoutParams) layoutParams).bottomMargin);
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).setMargins(
                    ((RelativeLayout.LayoutParams) layoutParams).leftMargin,
                    ((RelativeLayout.LayoutParams) layoutParams).topMargin,
                    marginRight,
                    ((RelativeLayout.LayoutParams) layoutParams).bottomMargin);
        }
        view.setLayoutParams(layoutParams);
    }


    public static float getMarginBottom(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            return ((FrameLayout.LayoutParams) layoutParams).bottomMargin;
        } else if (layoutParams instanceof LinearLayout.LayoutParams) {
            return ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            return ((RelativeLayout.LayoutParams) layoutParams).bottomMargin;
        }
        return 0;
    }

    public static void setWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }

    public static void setWeight(View view, int weight) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).weight = weight;
        }
        view.setLayoutParams(layoutParams);
    }

    public static void toRightOf(View view, int id) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).addRule(RelativeLayout.RIGHT_OF, id);
        }
        view.setLayoutParams(layoutParams);
    }
}
