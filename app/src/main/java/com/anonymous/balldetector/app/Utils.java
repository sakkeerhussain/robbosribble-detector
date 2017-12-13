package com.anonymous.balldetector.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by sakkeer on 13/12/17.
 */

public class Utils {

    public static void requestPermissions(Activity activity) {
        if (!hasPermissions(activity)){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    public static boolean hasPermissions(Context context) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
    }

}
