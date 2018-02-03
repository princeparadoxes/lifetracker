package com.princeparadoxes.watertracker.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.princeparadoxes.watertracker.BuildConfig;

import java.util.List;

public class ServiceUtils {
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        String name = serviceClass.getName() + "+" + BuildConfig.APPLICATION_ID;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            String runningServiceName = service.service.getClassName() + "+" + service.service.getPackageName();
            if (name.equals(runningServiceName)) {
                return true;
            }
        }
        return false;
    }
}
