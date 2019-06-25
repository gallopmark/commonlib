package com.gallopmark.commom;

import androidx.annotation.NonNull;

public interface OnRequestPermissionsCallback {
    void onGranted(int requestCode, @NonNull String[] permissions);

    void onDenied(int requestCode, @NonNull String[] permissions, boolean isProhibit);
}
