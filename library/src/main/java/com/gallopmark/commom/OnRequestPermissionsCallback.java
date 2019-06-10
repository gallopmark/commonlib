package com.gallopmark.commom;

import android.support.annotation.NonNull;

public interface OnRequestPermissionsCallback {
    void onGranted(int requestCode, @NonNull String[] permissions);

    void onDenied(int requestCode, @NonNull String[] permissions, boolean isProhibit);
}
