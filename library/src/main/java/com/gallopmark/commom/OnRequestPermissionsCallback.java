package com.gallopmark.commom;

public interface OnRequestPermissionsCallback {
    void onGranted();

    void onDenied(boolean isProhibit);
}
