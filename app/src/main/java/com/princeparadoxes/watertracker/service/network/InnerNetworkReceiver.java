package com.princeparadoxes.watertracker.service.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.princeparadoxes.watertracker.utils.NetworkUtils;

public class InnerNetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean hasConnection = NetworkUtils.hasConnection(context);
        if (hasConnection) {
            NetworkReceiver.sendIntent(context);
        }
    }
}
