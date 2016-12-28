package com.princeparadoxes.watertracker.utils;

import android.app.Activity;
import android.content.Intent;

public class ShareTools {

    public static void shareText(Activity activity, String text) {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        myIntent.putExtra(Intent.EXTRA_TEXT, text);//
        activity.startActivity(Intent.createChooser(myIntent, "Share with"));
    }
}
