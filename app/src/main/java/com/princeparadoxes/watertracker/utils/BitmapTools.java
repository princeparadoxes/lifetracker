package com.princeparadoxes.watertracker.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapTools {

    public static Bitmap changeImageColor(Bitmap sourceBitmap, int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        p.setColorFilter(filter);

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
        return resultBitmap;
    }


    public static Drawable covertBitmapToDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static Bitmap convertDrawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static boolean resizeImage(@NonNull String pathToFile, long size) {
        boolean isResize = false;
        Bitmap bitmap = decodeSampledBitmapFile(pathToFile, size);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(pathToFile);
            isResize = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);   // bmp is your Bitmap instance
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isResize;
    }

    /**
     * Bitmap will be scaled in proportion to opposite side
     * if one of sizes is more than according max value;
     */
    public static Bitmap scaleIfNeed(Bitmap bitmap, int maxWidth, int maxHeight) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        boolean flag = false;

        if (height > maxHeight) {
            width = width * maxHeight / height;
            height = maxHeight;
            flag = true;
        }

        if (width > maxWidth) {
            height = height * maxWidth / width;
            width = maxWidth;
            flag = true;
        }

        return flag ? Bitmap.createScaledBitmap(bitmap, width, height, false) : bitmap;
    }

    public static Bitmap cropCenterBitmap(Bitmap bitmap) {
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            return Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );
        } else {
            return Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, false);
        return resizeBitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        return calculateInSampleSize(options.outWidth, options.outHeight, reqWidth, reqHeight);
    }

    public static int calculateInSampleSize(int currentWidth, int currentHeight, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = currentHeight;
        final int width = currentWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            int scale = 1;
            while (width / scale / 2 >= reqWidth && height / scale / 2 >= reqHeight) {
                scale *= 2;
            }
            inSampleSize = scale;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFile(String pathToFile, long size) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToFile, options);

        double heapSize = options.outWidth * options.outHeight * 4;
        double quality = 1.00d;

        while (heapSize >= size) {
            quality = quality - 0.01d;
            heapSize = options.outWidth * options.outHeight * 4 * quality * quality;
        }

        int reqWidth = (int) (options.outWidth * quality);
        int reqHeight = (int) (options.outHeight * quality);

        // Calculate inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        return BitmapFactory.decodeFile(pathToFile, o2);
    }

    public static Bitmap getBitmapFromFile(@NonNull File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static Bitmap compressBitmap(@NonNull Bitmap bitmap, int quality, int requiredSize) {
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream1);
        byte[] byteArray1 = stream1.toByteArray();

        BitmapFactory.Options options1 = new BitmapFactory.Options();
        options1.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length, options1);
        int scale1 = 1;
        while (options1.outWidth / scale1 / 2 >= requiredSize
                && options1.outHeight / scale1 / 2 >= requiredSize)
            scale1 *= 2;
        options1.inSampleSize = scale1;
        options1.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.length, options1);
        return bitmap;
    }

    public static Bitmap drawCircleWithTextByRes(Context context,
                                                 @DimenRes int sizeResId,
                                                 @ColorRes int backgroundColorId,
                                                 String text,
                                                 @ColorRes int textColorId) {

        Resources resources = context.getResources();
        int backgroundColor = DrawableTools.getColor(context, backgroundColorId);
        int size = resources.getDimensionPixelOffset(sizeResId);
        int textColor = DrawableTools.getColor(context, textColorId);

        return drawCircleWithTextByValues(context, size, backgroundColor, text, textColor);
    }

    public static Bitmap drawCircleWithTextByValues(Context context,
                                                    int size,
                                                    @ColorInt int backgroundColor,
                                                    String text,
                                                    @ColorInt int textColor) {

        Resources resources = context.getResources();

        float textSize = size * 0.5f;
//        Drawable drawable = DrawableTools.getDrawable(context, resId);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

//        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
//                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        canvas.setBitmap(bitmap);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        drawable.onDraw(canvas);

//        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
//        Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);

//        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
//        // set default bitmap config if none
//        if(bitmapConfig == null) {
//            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
//        }
//        // resource bitmaps are imutable,
//        // so we need to convert it to mutable one
//        bitmap = bitmap.copy(bitmapConfig, true);
//
//        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // text color - #3D3D3D
        paint.setColor(backgroundColor);

        canvas.drawCircle(size / 2, size / 2, size / 2, paint);


        // text color - #3D3D3D
        paint.setColor(textColor);
        // text size in pixels
        paint.setTextSize(textSize);
//        // text shadow
//        paint.setShadowLayer(1f, 0f, 1f, color.WHITE);

        // onDraw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;

        canvas.drawText(text, x, y, paint);

        return bitmap;
    }

    /**
     * Add more variable params later on;
     * @param context
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundedBitmapWithStroke(Context context, Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = Color.WHITE;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;

        float strokeWidth = DimenTools.pxFromDp(context, 2);
        paint.setXfermode(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);

        canvas.drawCircle(width / 2, height / 2, width / 2 - strokeWidth / 2, paint);

        return output;
    }

    public static Bitmap drawTextToDrawable(Context context,
                                            @DrawableRes int resId,
                                            String text,
                                            int textSize,
                                            @ColorRes int textColorId) {

        Resources resources = context.getResources();

        Drawable drawable = DrawableTools.getDrawable(context, resId);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

//        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
//        Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);

//        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
//        // set default bitmap config if none
//        if(bitmapConfig == null) {
//            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
//        }
//        // resource bitmaps are imutable,
//        // so we need to convert it to mutable one
//        bitmap = bitmap.copy(bitmapConfig, true);
//
//        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(DrawableTools.getColor(context, textColorId));
        // text size in pixels
        paint.setTextSize((int) (textSize * scale));
//        // text shadow
//        paint.setShadowLayer(1f, 0f, 1f, color.WHITE);

        // onDraw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;

        canvas.drawText(text, x, y, paint);

        return bitmap;
    }

    /**
     * onDraw the badge with number at the top|right corner of drawable.
     * Thx for idea: http://www.skoumal.net/en/android-how-draw-text-bitmap/
     * @param context
     * @param drawable origin drawable.
     * @param count the number witch will be drawn.
     * @return new drawable with drawn badge.
     */
    public static Drawable drawBadge(Context context, Drawable drawable,
                                     int radius, int paddingX, int paddingY, int count,
                                     @ColorInt int backgroundColor, @ColorInt int textColor) {
        Bitmap bitmap = convertDrawableToBitmap(drawable);
        bitmap = drawBadge(context, bitmap, radius, paddingX, paddingY, count, backgroundColor, textColor);
        return covertBitmapToDrawable(context, bitmap);
    }

    public static Bitmap drawBadge(Context context, Bitmap bitmap,
                                   int radius, int paddingX, int paddingY, int count,
                                   @ColorInt int backgroundColor, @ColorInt int textColor) {
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

//        int size = (int) context.getResources().getDimension(R.dimen.badge_size);
        int size = 2 * radius;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(backgroundColor);

        canvas.drawCircle(width - paddingX - size / 2, paddingY + size / 2, size / 2, paint);

//        float textSize = context.getResources().getDimensionPixelSize(R.dimen.badge_text_size);
        float textSize = size * 0.7f;
        paint.setColor(textColor);
//        float scale = context.getResources().getDisplayMetrics().density;
//        paint.setTextSize((int) (textSize * scale));
        paint.setTextSize(textSize);

        String gText;
        if (count < 100) {
            gText = String.valueOf(count);
        } else {
            gText = "99+";
        }

        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = width - bounds.width() - (size - bounds.width()) / 2 - paddingX;
        int y = (size + bounds.height()) / 2 + paddingY;

        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }

    public static Bitmap getBitmapFromResources(Context context, @DrawableRes int drawableRes) {
        return BitmapFactory.decodeResource(context.getResources(), drawableRes);
    }
}
