package com.gallopmark.commom;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/*activity基础类*/
public abstract class CommonActivity extends AppCompatActivity {
    /*权限申请请求码*/
    private int requestPermissionCode;
    /*权限申请回调*/
    private OnRequestPermissionsCallback requestCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(bindOrientation());
        setContentView(bindLayoutId());
        initializeScreen();
        initialize(savedInstanceState);
        loadDataStart();
    }

    /*设置屏幕全屏或非全屏*/
    protected void initializeScreen() {
        if (isFullScreen()) {
            SystemTintHelper.requestFullScreen(this);
        }
    }

    /*是否全屏*/
    protected boolean isFullScreen() {
        return false;
    }

    /*修改状态栏颜色*/
    protected void setStatusBarColor(@ColorRes int colorId) {
        if (!isFullScreen()) {
            SystemTintHelper.setStatusBarColor(this, getColorCompat(colorId));
        } else {
            View statusView = findViewById(R.id.statusBarView);
            if (statusView != null && colorId != 0) {
                ViewGroup.LayoutParams params = statusView.getLayoutParams();
                params.height = getStatusBarHeight();
                statusView.setBackgroundResource(colorId);
            }
        }
    }

    /*设置状态栏白底黑字！Android浅色状态栏黑色字体模式*/
    protected int setStatusBarLightMode() {
        return SystemTintHelper.statusBarLightMode(this);
    }

    /*设置屏幕方向*/
    protected int bindOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    protected abstract int bindLayoutId();

    protected abstract void initialize(@Nullable Bundle savedInstanceState);

    protected void loadDataStart() {
    }

    protected int getColorCompat(@ColorRes int id) {
        return ContextCompat.getColor(this, id);
    }

    protected String[] getStringArray(@ArrayRes int id) {
        return getResources().getStringArray(id);
    }

    protected Drawable getDrawableCompat(@DrawableRes int id) {
        return ContextCompat.getDrawable(this, id);
    }

    protected float getDimension(@DimenRes int id) {
        return getResources().getDimension(id);
    }

    /*将getDimension结果转换为int，并且小数部分四舍五入*/
    protected int getDimensionPixelSize(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }

    /*直接截断小数位，即取整其实就是把float强制转化为int，注意不是四舍五入*/
    protected int getDimensionPixelOffset(@DimenRes int id) {
        return getResources().getDimensionPixelOffset(id);
    }

    protected int[] getIntArray(@ArrayRes int id) {
        return getResources().getIntArray(id);
    }

    protected ColorStateList getColorStateListCompat(@ColorRes int id) {
        return ContextCompat.getColorStateList(this, id);
    }

    /*获取状态栏颜色*/
    protected int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /*获取屏幕宽度*/
    protected int getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    /*获取屏幕高度*/
    protected int getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    protected void startActivity(@NonNull final Class<? extends Activity> clz) {
        startActivity(clz, null);
    }

    protected void startActivity(@NonNull final Class<? extends Activity> clz, @Nullable Bundle bundle) {
        Intent intent = obtainIntent(clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected Intent obtainIntent(@NonNull final Class<? extends Activity> clz) {
        return new Intent(this, clz);
    }

    protected void startActivityForResult(@NonNull final Class<? extends Activity> clz, int requestCode) {
        startActivityForResult(clz, requestCode, null);
    }

    protected void startActivityForResult(@NonNull final Class<? extends Activity> clz, int requestCode, @Nullable Bundle options) {
        Intent intent = obtainIntent(clz);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivityForResult(intent, requestCode);
    }

    /*是否授予了权限*/
    protected boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 申请权限
     *
     * @param permissions 需要申请的权限数组
     * @param requestCode 请求码
     * @param callback    请求回调
     */
    protected void requestPermissions(@NonNull String[] permissions, int requestCode, OnRequestPermissionsCallback callback) {
        this.requestPermissionCode = requestCode;
        this.requestCallback = callback;
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestPermissionCode) {
            PermissionHelper.convert(this, permissions, grantResults, requestCallback);
        }
    }
}
