package com.gallopmark.commom;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

/*权限回调处理*/
final class PermissionHelper {

    static void convert(Object target, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, @Nullable OnRequestPermissionsCallback requestCallback) {
        boolean allGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }
        if (allGranted) {
            if (requestCallback != null) {
                requestCallback.onGranted(requestCode, permissions);
            }
        } else {
            boolean isProhibit = false;
            for (String permission : permissions) {
                //可以推断出用户选择了“不在提示”选项，在这种情况下需要引导用户至设置页手动授权
                if (target instanceof Fragment) {
                    Fragment fragment = (Fragment) target;
                    if (!fragment.shouldShowRequestPermissionRationale(permission)) {
                        isProhibit = true;
                        break;
                    }
                } else {
                    Activity activity = (Activity) target;
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                        isProhibit = true;
                        break;
                    }
                }
            }
            if (requestCallback != null) {
                requestCallback.onDenied(requestCode, permissions, isProhibit);
            }
        }
    }
}
