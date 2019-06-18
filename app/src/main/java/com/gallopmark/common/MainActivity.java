package com.gallopmark.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gallopmark.commom.CommonActivity;
import com.gallopmark.commom.OnRequestPermissionsCallback;


public class MainActivity extends CommonActivity implements OnRequestPermissionsCallback {

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialize(@Nullable Bundle savedInstanceState) {
//        setTransparentStatusBar(false);
//        addMarginTopEqualStatusBarHeight(findViewById(R.id.toolbar));
//        addChildFragment(R.id.mContainer, new TestFragment1());
        addFragment(R.id.mContainer, new TestFragment1());
//        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE}, 2, this);
//        new CommonLoadingDialog(this).setMessage("loading...").show();
//        new IosLoadingDialog(this).setMessage("ios loading...").show();
//        showLoadingDialog("loading...");
    }

    @Override
    public void onGranted(int requestCode, @NonNull String[] permissions) {
        Log.e("granted", String.valueOf(requestCode));
    }

    @Override
    public void onDenied(int requestCode, @NonNull String[] permissions, boolean isProhibit) {
        Log.e("denied", String.valueOf(requestCode));
    }
}
