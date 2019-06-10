package com.gallopmark.common;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gallopmark.commom.CommonActivity;
import com.gallopmark.commom.OnRequestPermissionsCallback;

import java.util.Random;

public class MainActivity extends CommonActivity implements OnRequestPermissionsCallback {

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected void initialize(@Nullable Bundle savedInstanceState) {
        setStatusBarColor(R.color.colorAccent);
        setStatusBarLightMode();
        Button mButton = findViewById(R.id.mButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = {"XPopup", "BasePopup", "HouseLoading", "GifLoadingView"};
                showLongToast(array[new Random().nextInt(array.length)]);
            }
        });
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE}, 2, this);
//        new CommonLoadingDialog(this).setMessage("loading...").show();
//        new IosLoadingDialog(this).setMessage("ios loading...").show();
        showLoadingDialog("loading...");
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