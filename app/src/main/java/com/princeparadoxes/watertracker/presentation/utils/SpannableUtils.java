package com.princeparadoxes.watertracker.presentation.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;

/**
 * Created by Danil on 25.02.2016.
 */
public class SpannableUtils {

    public static Spannable getBoldString(String string) {
        Spannable spannable = new SpannableString(string);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, string.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static Spannable getColorString(String string, @ColorInt int color) {
        return getColorString(string, color, 0, string.length());
    }


    public static Spannable getColorString(String string, @ColorInt int color, int start, int end) {
        Spannable spannable = new SpannableString(string);
        spannable.setSpan(new ForegroundColorSpan(color), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static Spannable getHalfBoldColorString(String string, int color, int start, int end) {
        Spannable spannable = new SpannableString(string);
        spannable.setSpan(new ForegroundColorSpan(color), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static Spannable getStrikethroughString(String string) {
        Spannable spannable = new SpannableString(string);
        spannable.setSpan(new StrikethroughSpan(), 0, string.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static Spannable getAbsoluteSizeSpan(Context context, String string, int dimenInSp) {
        SpannableString spannable = new SpannableString(string);
        spannable.setSpan(new AbsoluteSizeSpan((int) DimenTools.pxFromSp(context, dimenInSp)),
                0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static void addBoldSpan(Spannable spannable) {
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, spannable.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

//    public static void addColorSpanRes(Spannable spannable, @ColorRes int color, Context context) {
//        if (spannable == null || color == 0) return;
//        int intColor = CompatUtils.getColor(context, color);
//        spannable.setSpan(new ForegroundColorSpan(intColor), 0, spannable.length(),
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//    }

    public static void addColorSpanInt(Spannable spannable, @ColorInt int color) {
        if (spannable == null) return;
        spannable.setSpan(new ForegroundColorSpan(color), 0, spannable.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void addStrikethroughSpan(Spannable spannable) {
        spannable.setSpan(new StrikethroughSpan(), 0, spannable.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void addSizeSpan(Spannable spannable, int size) {
        spannable.setSpan(new AbsoluteSizeSpan(size), 0, spannable.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
