package com.gallopmark.commom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
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
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gallopmark.commom.dialog.CommonLoadingDialog;
import com.gallopmark.commom.dialog.IosLoadingDialog;
import com.gallopmark.commom.toast.CommonToast;

/*activity基础类*/
public abstract class CommonActivity extends AppCompatActivity {
    protected Activity thisActivity = this;
    /*startActivityForResult请求码*/
    private int mActivityRequestCode;
    /*activity result回调*/
    private OnActivityResultCallback mActivityResultCallback;
    /*权限申请请求码*/
    private int mPermissionsRequestCode;
    /*权限申请回调*/
    private OnRequestPermissionsCallback mRequestPermissionsCallback;

    private Dialog mLoadingDialog;

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
            SystemTintHelper.setStatusBarColor(thisActivity, getColorCompat(colorId));
        } else {
            View statusView = findViewById(R.id.statusBarView);
            if (statusView != null && colorId != 0) {
                ViewGroup.LayoutParams params = statusView.getLayoutParams();
                params.height = getStatusBarHeight();
                statusView.setBackgroundResource(colorId);
            }
        }
    }

    /*Android浅色状态栏黑色字体模式*/
    protected void setStatusBarLightMode() {
        int result = SystemTintHelper.statusBarLightMode(thisActivity);
        if (!(result == 1 || result == 2 || result == 3) && BuildConfig.DEBUG) {
            Log.d("gallopmark", "This device does not support setStatusBarLightMode");
        }
    }

    /*当设置全屏沉浸状态栏时，一般使用此方法将toolbar等设置topMargin为statusHeight*/
    protected void addMarginTopEqualStatusBarHeight(@NonNull View view) {
        SystemTintHelper.addMarginTopEqualStatusBarHeight(thisActivity, view);
    }

    /*设置屏幕方向*/
    protected int bindOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    protected abstract int bindLayoutId();

    protected abstract void initialize(@Nullable Bundle savedInstanceState);

    protected void loadDataStart() {
    }

    /*获取color*/
    protected int getColorCompat(@ColorRes int id) {
        return ContextCompat.getColor(thisActivity, id);
    }

    /*获取string数组 values(string-array)*/
    protected String[] getStringArray(@ArrayRes int id) {
        return getResources().getStringArray(id);
    }

    /*获取drawable*/
    protected Drawable getDrawableCompat(@DrawableRes int id) {
        return ContextCompat.getDrawable(thisActivity, id);
    }

    /*获取dimens value*/
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
        return ContextCompat.getColorStateList(thisActivity, id);
    }

    /*获取状态栏颜色*/
    protected int getStatusBarHeight() {
        return SystemTintHelper.getStatusBarHeight(thisActivity);
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

    /*启动activity*/
    protected void startActivity(@NonNull final Class<? extends Activity> clz) {
        startActivity(clz, null);
    }

    /*启动activity，带bundle参数*/
    protected void startActivity(@NonNull final Class<? extends Activity> clz, @Nullable Bundle bundle) {
        Intent intent = obtainIntent(clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected Intent obtainIntent(@NonNull final Class<? extends Activity> clz) {
        return new Intent(thisActivity, clz);
    }

    protected void startActivityForResult(@NonNull final Class<? extends Activity> clz, int requestCode, OnActivityResultCallback resultCallback) {
        startActivityForResult(clz, requestCode, null, resultCallback);
    }

    protected void startActivityForResult(@NonNull final Class<? extends Activity> clz, int requestCode, @Nullable Bundle options, OnActivityResultCallback resultCallback) {
        this.mActivityRequestCode = requestCode;
        this.mActivityResultCallback = resultCallback;
        Intent intent = obtainIntent(clz);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivityForResult(intent, requestCode);
    }

    /*是否授予了权限*/
    protected boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(thisActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 申请单个权限
     *
     * @param permission  需要申请的权限
     * @param requestCode 请求码
     * @param callback    callback
     */
    protected void requestPermission(@NonNull String permission, int requestCode, OnRequestPermissionsCallback callback) {
        requestPermissions(new String[]{permission}, requestCode, callback);
    }

    /**
     * 申请权限
     *
     * @param permissions 需要申请的权限数组
     * @param requestCode 请求码
     * @param callback    请求回调
     */
    protected void requestPermissions(@NonNull final String[] permissions, final int requestCode, OnRequestPermissionsCallback callback) {
        this.mPermissionsRequestCode = requestCode;
        this.mRequestPermissionsCallback = callback;
        ActivityCompat.requestPermissions(thisActivity, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mPermissionsRequestCode) {
            PermissionHelper.convert(thisActivity, requestCode, permissions, grantResults, mRequestPermissionsCallback);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mActivityRequestCode && mActivityResultCallback != null) {
            if (resultCode == RESULT_OK) {
                mActivityResultCallback.onResultOk(requestCode, data);
            } else if (resultCode == RESULT_CANCELED) {
                mActivityResultCallback.onResultCanceled(requestCode, data);
            }
        }
    }

    /*显示loading加载框*/
    protected void showLoadingDialog() {
        showLoadingDialog("");
    }

    /*显示带message的加载框*/
    protected void showLoadingDialog(@Nullable CharSequence message) {
        dismissLoadingDialog();
        if (mLoadingDialog == null) {
            mLoadingDialog = getLoadingDialog();
        }
        if (mLoadingDialog instanceof CommonLoadingDialog) {
            ((CommonLoadingDialog) mLoadingDialog).setMessage(message).show();
        } else {
            mLoadingDialog.show();
        }
    }

    /*消除加载框*/
    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    /*默认加载框，子类重写此方法自定义loadingDialog*/
    @NonNull
    protected Dialog getLoadingDialog() {
        return new IosLoadingDialog(thisActivity);
    }

    /*设置自己的loadingDialog*/
    public void setLoadingDialog(@NonNull Dialog mLoadingDialog) {
        this.mLoadingDialog = mLoadingDialog;
    }

    /*短时间显示Toast，子类也可以重写此方法自定义toast显示风格*/
    protected void showShortToast(CharSequence text) {
        if (!TextUtils.isEmpty(text))
            CommonToast.makeText(thisActivity, text, Toast.LENGTH_SHORT).show();
    }

    /*长时间显示Toast，子类也可以重写此方法自定义toast显示风格*/
    protected void showLongToast(CharSequence text) {
        if (!TextUtils.isEmpty(text))
            CommonToast.makeText(thisActivity, text, Toast.LENGTH_LONG).show();
    }
}
