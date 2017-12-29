package com.princeparadoxes.watertracker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class DrawableTools {

    public static void setColorFilterRes(View view, @ColorRes int color) {
        setColorFilter(view, getColor(view.getContext(), color));
    }

    public static void setColorFilter(View view, @ColorInt int color) {

//        if (view instanceof TextView) {
//            ((TextView) view).setTextColor(getColor(context, color));
//            return;
//        }

        Drawable drawable;
        if (view instanceof ImageView) {
            drawable = ((ImageView) view).getDrawable();
        } else {
            drawable = view.getBackground().mutate();
        }
        setColorFilter(drawable, color);
        setDrawable(view, drawable);
    }

    public static void setColorFilterRes(Context context, Drawable drawable, @ColorRes int color) {
        setColorFilter(drawable, getColor(context, color));
    }

    public static void setColorFilter(Drawable drawable, @ColorInt int color) {

        if (drawable == null) return;

        if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable) drawable).getPaint().setColor(color);
        } else if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setColor(color);
        } else if (drawable instanceof ColorDrawable) {
            ((ColorDrawable) drawable).setColor(color);
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, bmp1.getWidth() - bmp2.getWidth(), 0, null);
        return bmOverlay;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static void setDrawable(View view, Drawable drawable) {

        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(drawable);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static void setDrawable(View view, @DrawableRes int drawableRes) {
        setDrawable(view, getDrawable(view.getContext(), drawableRes));
    }

    public static void setDrawable(View view, Drawable drawable, @ColorRes int colorRes) {
        setColorFilterRes(view.getContext(), drawable, colorRes);
        setDrawable(view, drawable);
    }

    public static void setDrawable(View view, @DrawableRes int drawableRes, @ColorRes int colorRes) {
        Drawable drawable = getDrawable(view.getContext(), drawableRes, colorRes);
        setDrawable(view, drawable);
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableRes) {
        return ContextCompat.getDrawable(context, drawableRes);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            return context.getResources().getDrawable(drawableRes, context.getTheme());
//        } else {
//            return context.getResources().getDrawable(drawableRes);
//        }
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableRes, @ColorRes int colorRes) {
        Drawable drawable = getDrawable(context, drawableRes);
        setColorFilterRes(context, drawable, colorRes);
        return drawable;
    }

    @ColorInt
    public static int getColor(Context context, @ColorRes int colorRes) {
        return ContextCompat.getColor(context, colorRes);
    }

    public static @ColorInt
    int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

//    public static @ColorInt
//    int getRandomDarkColor(Context context) {
//        String[] colorArray = context.getResources().getStringArray(R.array.default_color_choice_values);
//        return color.parseColor(colorArray[new Random().nextInt(colorArray.length)]);
//    }

//    public static @ColorInt
//    int getRandomDarkColor(Context context, long id) {
//        String[] colorArray = context.getResources().getStringArray(R.array.default_color_choice_values);
//        int length = colorArray.length - 1;
//        while (id % length != 0 && length > 0) {
//            length--;
//        }
//        return color.parseColor(colorArray[length]);
//    }
}
