package com.princeparadoxes.watertracker.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.princeparadoxes.watertracker.R;

/**
 * To customize the alert dialog override the next Style:
 * <p>
 * <style name="AppCompatAlertDialogStyle" parent="Theme.AppCompat.Light.Dialog.Alert">
 * <item name="colorAccent">@color/accent</item>
 * <item name="android:textColorPrimary">@color/primary</item>
 * <item name="android:background">@color/content_background</item>
 * </style>
 * <p>
 * And change Application Theme:
 * <p>
 * <item name="android:dialogTheme">@style/AppCompatAlertDialogStyle</item> // <-- TimePicker dialog
 * <item name="android:alertDialogTheme">@style/AppCompatAlertDialogStyle</item> // <-- Alert dialog
 * <p>
 * For more information see: http://takeoffandroid.com/dialog/material-alertdialog-using-appcompat-v22-1-0/
 */

public class AlertDialogTools {

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    public static class Builder {
        private Context mContext;
        private @StyleRes
        int mStyleRes = R.style.DialogTheme;
        private boolean mCancelable = false;
        private String mTitle = null;
        private String mMessage = null;
        private View mView = null;
        private int mMinWidthPx = 0;
        private @ColorInt
        int mPositiveTitleColor;
        private @ColorInt
        int mNegativeTitleColor;
        private @ColorInt
        int mNeutralTitleColor;
        private String mPositiveTitle = null;
        private String mNegativeTitle = null;
        private String mNeutralTitle = null;
        private DialogInterface.OnClickListener mPositiveListener = null;
        private DialogInterface.OnClickListener mNegativeListener = null;
        private DialogInterface.OnClickListener mNeutralListener = null;
        private DialogInterface.OnShowListener mOnShowListener = null;
        private DialogInterface.OnDismissListener mOnDismissListener = null;

        private Builder(Context context) {
            this.mContext = context;
        }

        public Builder cancelable(boolean val) {
            mCancelable = val;
            return this;
        }

        public Builder title(String val) {
            mTitle = val;
            return this;
        }

        public Builder title(@StringRes int val) {
            return title(mContext.getResources().getString(val));
        }

        public Builder message(String val) {
            mMessage = val;
            return this;
        }

        public Builder message(@StringRes int val) {
            return message(mContext.getResources().getString(val));
        }

        public Builder view(View val) {
            mView = val;
            return this;
        }

        public Builder minWidthPx(int minWidthPx) {
            mMinWidthPx = minWidthPx;
            return this;
        }

        public Builder positiveTitle(String val) {
            mPositiveTitle = val;
            return this;
        }

        public Builder positiveTitle(@StringRes int val) {
            return positiveTitle(mContext.getResources().getString(val));
        }

        public Builder negativeTitle(String val) {
            mNegativeTitle = val;
            return this;
        }

        public Builder negativeTitle(@StringRes int val) {
            return negativeTitle(mContext.getResources().getString(val));
        }

        public Builder neutralTitle(String val) {
            mNeutralTitle = val;
            return this;
        }

        public Builder neutralTitle(@StringRes int val) {
            return neutralTitle(mContext.getResources().getString(val));
        }

        public Builder setNeutralTitleColor(@ColorInt int val) {
            mNeutralTitleColor = val;
            return this;
        }

        public Builder setNeutralTitleColorRes(@ColorRes int val) {
            mNeutralTitleColor = DrawableTools.getColor(mContext, val);
            return this;
        }

        public Builder setNegativeTitleColor(@ColorInt int val) {
            mNegativeTitleColor = val;
            return this;
        }

        public Builder setNegativeTitleColorRes(@ColorRes int val) {
            mNegativeTitleColor = DrawableTools.getColor(mContext, val);
            return this;
        }

        public Builder setPositiveTitleColor(@ColorInt int val) {
            mPositiveTitleColor = val;
            return this;
        }

        public Builder setPositiveTitleColorRes(@ColorRes int val) {
            mPositiveTitleColor = DrawableTools.getColor(mContext, val);
            return this;
        }

        public Builder positiveListener(DialogInterface.OnClickListener val) {
            mPositiveListener = val;
            return this;
        }

        public Builder negativeListener(DialogInterface.OnClickListener val) {
            mNegativeListener = val;
            return this;
        }

        public Builder neutralListener(DialogInterface.OnClickListener val) {
            mNeutralListener = val;
            return this;
        }

        public Builder dismissListener(DialogInterface.OnDismissListener val) {
            mOnDismissListener = val;
            return this;
        }

        public Builder showListener(DialogInterface.OnShowListener val) {
            mOnShowListener = val;
            return this;
        }

        public Builder styleRes(@StyleRes int styleRes) {
            mStyleRes = styleRes;
            return this;
        }

        public void build() {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, mStyleRes);
            builder.setCancelable(mCancelable);
            if (!Strings.isBlank(mTitle)) {
                builder.setTitle(mTitle);
            }
            if (!Strings.isBlank(mMessage)) {
                builder.setMessage(mMessage);
            }
            if (!Strings.isBlank(mPositiveTitle)) {
                builder.setPositiveButton(mPositiveTitle, mPositiveListener);
            }
            if (!Strings.isBlank(mNeutralTitle)) {
                builder.setNeutralButton(mNeutralTitle, mNeutralListener);
            }
            if (!Strings.isBlank(mNegativeTitle)) {
                builder.setNegativeButton(mNegativeTitle, mNegativeListener);
            }
            if (mOnDismissListener != null) {
                builder.setOnDismissListener(mOnDismissListener);
            }
            if (mView != null) {
                builder.setView(mView);
            }

            AlertDialog alertDialog = builder.create();

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    AlertDialog ad = (AlertDialog) dialog;
                    if (mPositiveTitleColor != 0) {
                        Button positiveButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
                        positiveButton.setTextColor(mPositiveTitleColor);
                    }
                    if (mNegativeTitleColor != 0) {
                        Button negativeButton = ad.getButton(DialogInterface.BUTTON_NEGATIVE);
                        negativeButton.setTextColor(mNegativeTitleColor);
                    }
                    if (mNeutralTitleColor != 0) {
                        Button neutralButton = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
                        neutralButton.setTextColor(mNeutralTitleColor);
                    }

                    if (mOnShowListener != null) {
                        mOnShowListener.onShow(dialog);
                    }
                }
            });

            if (mMinWidthPx > 0) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                if (lp.width < mMinWidthPx) {
                    lp.width = mMinWidthPx;
                }
                window.setAttributes(lp);
            }
            alertDialog.show();

        }
    }
}
