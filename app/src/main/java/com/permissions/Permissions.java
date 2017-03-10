package com.permissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by nguyenlinh on 10/03/2017.
 */

public class Permissions {
    private static final int PERMISSION_REQUEST_ID = 0x01;

    @TargetApi(23)
    public static boolean hasRWPermission(Context context) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ||
                context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED &&
                        context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                PackageManager.PERMISSION_GRANTED);
    }

    @TargetApi(23)
    public static void requestRWPermission(Activity activity){
        activity.requestPermissions(
                new String[]{
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_ID);
    }
}
