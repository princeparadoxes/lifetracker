package com.princeparadoxes.watertracker.presentation.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.lang.ref.WeakReference;

public class ApplicationSwitcher {

    ///////////////////////////////////////////////////////////////////////////
    // CONSTANTS
    ///////////////////////////////////////////////////////////////////////////

    public static final int CAMERA_REQUEST = 1678;
    public static final int GALLERY_REQUEST = 1786;
    public static final int FILE_REQUEST = 1763;

    public static ApplicationSwitcherBuilder start(Context context) {
        return new ApplicationSwitcherBuilder(context);
    }

    public static class ApplicationSwitcherBuilder {
        private WeakReference<Context> mContextReference;

        ///////////////////////////////////////////////////////////////////////////
        // CONSTRUCTORS
        ///////////////////////////////////////////////////////////////////////////

        ApplicationSwitcherBuilder(Context context) {
            mContextReference = new WeakReference<>(context);
        }

        ///////////////////////////////////////////////////////////////////////////
        // EMAIL
        ///////////////////////////////////////////////////////////////////////////

        public void openEmailApplication(String email) {
            openEmailApplication(email, "");
        }

        public void openEmailApplication(String email, String subject) {
            openEmailApplication(email, subject, "");
        }

        public void openEmailApplication(String email, String subject, String text) {
            final Context context = getContextWithCheck();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }

        ///////////////////////////////////////////////////////////////////////////
        // GOOGLE PLAY
        ///////////////////////////////////////////////////////////////////////////

        public void openGooglePlayAppPage() {
            final Context context = getContextWithCheck();
            final String appPackageName = context.getPackageName();
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

        ///////////////////////////////////////////////////////////////////////////
        // CAMERA
        ///////////////////////////////////////////////////////////////////////////

        public Uri openCameraForResult() {
            return openCameraForResult(getFileForPhoto());
        }

        public Uri openCameraForResult(File file) {
            final Activity activity = getActivityWithCheck();
            Uri photoPath = Uri.fromFile(file);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);
            activity.startActivityForResult(intent, CAMERA_REQUEST);
            return photoPath;
        }

        public File getFileForPhoto() {
            final Context context = getContextWithCheck();

            StringBuilder pathBuilder = new StringBuilder();
            pathBuilder.append(context.getExternalCacheDir().getAbsoluteFile());
            File directory = new File(pathBuilder.toString());
            if (!directory.exists()) {
                directory.mkdirs();
            }
            pathBuilder.append("/pic")
                    .append(System.currentTimeMillis())
                    .append(".png");
            return new File(pathBuilder.toString());
        }

        ///////////////////////////////////////////////////////////////////////////
        // GALLERY
        ///////////////////////////////////////////////////////////////////////////

        public boolean openGalleryForResult() {
            final Activity activity = getActivityWithCheck();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            return startActivityForResult(intent, GALLERY_REQUEST);
        }

        ///////////////////////////////////////////////////////////////////////////
        // FILE MANAGER
        ///////////////////////////////////////////////////////////////////////////

        public boolean openFileManagerForResult() {
            final Activity activity = getActivityWithCheck();
            Intent intent = new Intent();
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            if (!fileManagerExist()) return false;
            activity.startActivityForResult(Intent.createChooser(intent, ""), FILE_REQUEST);
            return true;
        }

        public boolean fileManagerExist() {
            final Context context = getContextWithCheck();
            Intent intent = new Intent();
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            return intent.resolveActivity(context.getPackageManager()) != null;
        }

        ///////////////////////////////////////////////////////////////////////////
        // BROWSER
        ///////////////////////////////////////////////////////////////////////////

        public boolean openBrowser(String path) {
            return startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(path)));
        }

        ///////////////////////////////////////////////////////////////////////////
        // APP SETTINGS
        ///////////////////////////////////////////////////////////////////////////

        public boolean openAppSettings() {
            final Context context = getContextWithCheck();
            final Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            return startActivity(intent);
        }

        ///////////////////////////////////////////////////////////////////////////
        // NAVIGATOR
        ///////////////////////////////////////////////////////////////////////////

        public boolean openNavigator(@NonNull Double latitude,
                                     @NonNull Double longitude,
                                     @Nullable String address) {
            String request = "geo:" + latitude + "," + longitude;
            if (address != null) request += "?q=" + address;

            return startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(request)));
        }

        ///////////////////////////////////////////////////////////////////////////
        // DIAL
        ///////////////////////////////////////////////////////////////////////////

        public boolean openDial(@NonNull String phoneNumber) {
            return startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
        }

        ///////////////////////////////////////////////////////////////////////////
        // COMMON
        ///////////////////////////////////////////////////////////////////////////

        private boolean startActivity(Intent intent) {
            final Context context = getContextWithCheck();
            if (intent.resolveActivity(context.getPackageManager()) == null) {
                return false;
            }
            context.startActivity(intent);
            return true;
        }

        private boolean startActivityForResult(Intent intent, int code) {
            final Activity activity = getActivityWithCheck();
            if (intent.resolveActivity(activity.getPackageManager()) == null) {
                return false;
            }
            activity.startActivityForResult(intent, code);
            return true;
        }

        ///////////////////////////////////////////////////////////////////////////
        // CHECK
        ///////////////////////////////////////////////////////////////////////////

        @NonNull
        private Activity getActivityWithCheck() {
            Context context = getContextWithCheck();
            if (!(context instanceof Activity)) {
                throw new RuntimeException("The method should be invoked only with Activity");
            }
            return (Activity) context;
        }

        @NonNull
        private Context getContextWithCheck() {
            if (mContextReference == null) {
                throw new IllegalStateException("Context must be not null");
            }
            final Context context = mContextReference.get();
            if (context == null) {
                throw new IllegalStateException("Context must be not null");
            }
            return context;
        }

    }
}
