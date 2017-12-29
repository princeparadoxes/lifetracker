package com.princeparadoxes.watertracker.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

import java.io.File;
import java.lang.ref.WeakReference;

public class ApplicationSwitcher {
    public static final int CAMERA_REQUEST = 1678;
    public static final int GALLERY_REQUEST = 1786;
    public static final int FILE_REQUEST = 1763;

    public static ApplicationSwitcherBuilder start(Activity activity){
        return new ApplicationSwitcherBuilder(activity);
    }

    public static ApplicationSwitcherBuilder start(Application application){
        return new ApplicationSwitcherBuilder(application);
    }

    public static ApplicationSwitcherBuilder start(Context context){
        return new ApplicationSwitcherBuilder(context);
    }

    public static class ApplicationSwitcherBuilder {
        private WeakReference<Context> mContextReference;

        ////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////  INIT  //////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        ApplicationSwitcherBuilder(Context context) {
            mContextReference = new WeakReference<>(context);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////  ACTIONS  ///////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        public void openEmailApplication(String email) {
            openEmailApplication(email, "");
        }

        public void openEmailApplication(String email, String subject) {
            openEmailApplication(email, subject, "");
        }

        public void openEmailApplication(String email, String subject, String text) {
            checkContext();
            final Context context = mContextReference.get();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }

        public void openGooglePlayAppPage() {
            checkContext();
            final Context context = mContextReference.get();
            final String appPackageName = context.getPackageName();
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

        public Uri openCameraForResult() {
            return openCameraForResult(getFileForPhoto());
        }

        public Uri openCameraForResult(File file) {
            checkContext();
            checkActivity();
            final Activity activity = (Activity) mContextReference.get();
            Uri photoPath = Uri.fromFile(file);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);
            activity.startActivityForResult(intent, CAMERA_REQUEST);
            return photoPath;
        }

        public File getFileForPhoto() {
            final Context context = mContextReference.get();

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

        public void openGalleryForResult() {
            checkContext();
            checkActivity();
            final Activity activity = (Activity) mContextReference.get();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(intent, GALLERY_REQUEST);
        }

        public boolean fileManagerExist() {
            checkContext();
            final Context context = mContextReference.get();
            Intent intent = new Intent();
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            return intent.resolveActivity(context.getPackageManager()) != null;
        }

        public void openFileManagerForResult() {
            checkContext();
            checkActivity();
            final Activity activity = (Activity) mContextReference.get();
            Intent intent = new Intent();
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(Intent.createChooser(intent, ""), FILE_REQUEST);
        }

        public void openBrowser(String path) {
            checkContext();
            final Context context = mContextReference.get();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            context.startActivity(browserIntent);
        }

        public void openAppSettings(){
            checkContext();
            final Context context = mContextReference.get();
            final Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////  ERRORS  ////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        private void checkContext() {
            if (mContextReference.get() == null) {
                throw new RuntimeException("Context must be not null");
            }
        }

        private void checkActivity() {
            if (!(mContextReference.get() instanceof Activity)) {
                throw new RuntimeException("The method should be invoked only with Activity");
            }
        }
    }
}