package com.soccer.whosin.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.soccer.whosin.R;
import com.soccer.whosin.interfaces.PermissionsCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 19/11/16.
 **/
public class AndroidPermissionsHelper {

    private static String[] getPendingPermissions(Context context, int requestCode) {
        List<String> pendingPermissions = new ArrayList<>();
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST_CODE && PackageManager.PERMISSION_GRANTED
                != ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            pendingPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        } else if (requestCode == Constants.CALL_PERMISSION_REQUEST_CODE && PackageManager.PERMISSION_GRANTED
                != ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)) {
            pendingPermissions.add(Manifest.permission.CALL_PHONE);
        }
        return pendingPermissions.toArray(new String[pendingPermissions.size()]);
    }

    public static void requestPermissions(Fragment fragment, int requestCode) {
        String[] pendingPermissions = getPendingPermissions(fragment.getActivity(), requestCode);
        if (pendingPermissions.length > 0) {
            fragment.requestPermissions(pendingPermissions, requestCode);
            return;
        }
        ((PermissionsCallback) fragment).onAndroidPermissionsGranted(requestCode);
    }

    public static void requestPermissions(Activity activity, int requestCode) {
        String[] pendingPermissions = getPendingPermissions(activity, requestCode);
        if (pendingPermissions.length > 0) {
            ActivityCompat.requestPermissions(activity, pendingPermissions, requestCode);
            return;
        }
        ((PermissionsCallback) activity).onAndroidPermissionsGranted(requestCode);
    }

    /**
     * Check if all permissions were granted
     * @param grantResults permissions result
     * @return true if all permissions were granted or false in the other case
     */
    private static boolean allPermissionsGranted(int[] grantResults) {
        boolean permissionsGranted = true;
        for (int i = 0; i < grantResults.length && permissionsGranted; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                permissionsGranted = false;
        }
        return permissionsGranted;
    }

    /**
     * Check if some permissions were disabled with the option "Never ask me again". In that case
     * the user must enable permissions in phone app setting
     * @param activity Activity were the permissions were requested
     * @param permissions Permissions requested
     * @param grantResults Permissions result
     * @return true if there are permissions disabled or true in the other case
     */
    private static boolean somePermissionsDisabled(Activity activity, String[] permissions, int[] grantResults) {
        boolean permissionsDisabled = false;
        for (int i = 0; i < permissions.length && !permissionsDisabled; i++) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])
                    && grantResults[i] != PackageManager.PERMISSION_GRANTED)
                permissionsDisabled = true;
        }
        return permissionsDisabled;
    }

    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length <= 0)
            return;
        if (allPermissionsGranted(grantResults))
            ((PermissionsCallback) fragment).onAndroidPermissionsGranted(requestCode);
        else {
            if (somePermissionsDisabled(fragment.getActivity(), permissions, grantResults))
                Toast.makeText(fragment.getActivity(), R.string.some_permissions_disabled, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(fragment.getActivity(), R.string.all_permissions_required, Toast.LENGTH_LONG).show();
        }
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length <= 0)
            return;
        if (allPermissionsGranted(grantResults))
            ((PermissionsCallback) activity).onAndroidPermissionsGranted(requestCode);
        else {
            if (somePermissionsDisabled(activity, permissions, grantResults))
                Toast.makeText(activity, R.string.some_permissions_disabled, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(activity, R.string.all_permissions_required, Toast.LENGTH_LONG).show();
        }
    }
}
