package com.gallopmark.commom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.gallopmark.commom.dialog.CommonLoadingDialog;
import com.gallopmark.commom.dialog.IosLoadingDialog;
import com.gallopmark.commom.toast.CommonToast;

import java.util.ArrayList;
import java.util.List;

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

    protected FragmentManager mFragmentManager;
    @Nullable
    private List<Fragment> mFragments;

    protected int mActivityCloseEnterAnimation = -1;

    protected int mActivityCloseExitAnimation = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(bindOrientation());
        setupTheme();
        setupWindow();
        setContentView(bindLayoutId());
        setFragmentManager();
        initializeScreen();
        initialize(savedInstanceState);
        loadDataStart();
    }

    /*setContentView之前调用，可以设置theme等*/
    protected void setupTheme() {

    }

    /*activity动画兼容性,style 退出动画 解决退出动画无效问题*/
    private void setupWindow() {
        Resources.Theme theme = getTheme();
        if (theme != null) {
            TypedArray activityStyle = theme.obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
            int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
            activityStyle.recycle();
            activityStyle = theme.obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
            mActivityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
            mActivityCloseExitAnimation = activityStyle.getResourceId(1, 0);
            activityStyle.recycle();
        }
    }

    protected void setFragmentManager() {
        mFragmentManager = getSupportFragmentManager();
    }

    /*设置屏幕全屏或非全屏*/
    protected void initializeScreen() {
        if (isFullScreen()) {
            SystemTintHelper.requestFullScreen(thisActivity);
        }
    }

    /*是否全屏*/
    protected boolean isFullScreen() {
        return false;
    }

    /*设置屏幕方向*/
    protected int bindOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    protected abstract int bindLayoutId();

    protected abstract void initialize(@Nullable Bundle savedInstanceState);

    protected void loadDataStart() {
    }

    /*设置透明状态栏 6.0以上是否设置黑色字体*/
    protected void setTransparentStatusBar(boolean isLightMode) {
        SystemTintHelper.setTransparentStatusBar(thisActivity, isLightMode);
    }

    /*修改状态栏颜色 比如ContextCompat.getColor(context,R.color.colorPrimaryDark)*/
    protected void setStatusBarColor(@ColorInt int colorId) {
        setupStatusBarColor(colorId);
    }

    /*修改状态栏颜色 比如传入R.color.colorPrimaryDark*/
    protected void setStatusBarColorResource(@ColorRes int colorResId) {
        setupStatusBarColor(getColorCompat(colorResId));
    }

    private void setupStatusBarColor(int color) {
        if (!isFullScreen()) {
            SystemTintHelper.setStatusBarColor(thisActivity, color);
        } else {
            View statusView = findViewById(R.id.statusBarView);
            if (statusView != null && color != 0) {
                ViewGroup.LayoutParams params = statusView.getLayoutParams();
                params.height = getStatusBarHeight();
                statusView.setBackgroundColor(color);
            }
        }
    }

    /*Android浅色状态栏黑色字体模式 如果调用了setStatusBarColor方法，则须在setStatusBarColor方法后执行，否则无效*/
    protected void setStatusBarLightMode() {
        int result = SystemTintHelper.setStatusBarLightMode(thisActivity);
        if (!(result == 1 || result == 2 || result == 3) && BuildConfig.DEBUG) {
            Log.d("gallopmark", "This device does not support setStatusBarLightMode");
        }
    }

    /*当设置全屏沉浸状态栏时，一般使用此方法将toolbar等设置topMargin为statusHeight*/
    protected void addMarginTopEqualStatusBarHeight(@NonNull View view) {
        SystemTintHelper.addMarginTopEqualStatusBarHeight(thisActivity, view);
    }

    protected void subtractMarginTopEqualStatusBarHeight(@NonNull View view) {
        SystemTintHelper.subtractMarginTopEqualStatusBarHeight(thisActivity, view);
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
        return getScreenSizePoint().x;
    }

    /*获取屏幕高度*/
    protected int getScreenHeight() {
        return getScreenSizePoint().y;
    }

    private Point getScreenSizePoint() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point;
    }

    /*启动activity*/
    protected void startActivity(@NonNull final Class<? extends Activity> clz) {
        startActivity(clz, null);
    }

    /*启动activity，带bundle参数*/
    protected void startActivity(@NonNull final Class<? extends Activity> clz, @Nullable Bundle extras) {
        Intent intent = obtainIntent(clz);
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivity(intent);
    }

    protected Intent obtainIntent(@NonNull final Class<? extends Activity> clz) {
        return new Intent(thisActivity, clz);
    }

    protected void startActivityForResult(@NonNull final Class<? extends Activity> clz, int requestCode, OnActivityResultCallback resultCallback) {
        startActivityForResult(clz, requestCode, null, resultCallback);
    }

    protected void startActivityForResult(@NonNull final Class<? extends Activity> clz, int requestCode, @Nullable Bundle extras, OnActivityResultCallback resultCallback) {
        this.mActivityRequestCode = requestCode;
        this.mActivityResultCallback = resultCallback;
        Intent intent = obtainIntent(clz);
        if (extras != null) {
            intent.putExtras(extras);
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

    /*add fragment*/
    protected void addFragment(@IdRes int containerId, Fragment fragment) {
        addFragment(containerId, fragment, 0, 0);
    }

    /*add fragment with anim*/
    protected void addFragment(@IdRes int containerId, Fragment fragment, @AnimatorRes @AnimRes int enterAnim, @AnimatorRes @AnimRes int outAnim) {
        addFragment(containerId, fragment, null, enterAnim, outAnim);
    }

    /*add fragment with tag*/
    protected void addFragment(@IdRes int containerId, Fragment fragment, @Nullable String tag) {
        addFragment(containerId, fragment, tag, 0, 0);
    }

    /*取消fragment动画传入参数 enterAnim = 0 && outAnim == 0*/
    protected void addFragment(@IdRes int containerId, Fragment fragment, @Nullable String tag, @AnimatorRes @AnimRes int enterAnim, @AnimatorRes @AnimRes int outAnim) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (enterAnim != 0 || outAnim != 0) {
            transaction.setCustomAnimations(enterAnim, 0);
        }
        if (!TextUtils.isEmpty(tag)) {
            transaction.add(containerId, fragment, tag);
        } else {
            transaction.add(containerId, fragment);
        }
        transaction.commitAllowingStateLoss();
        addFragmentToList(fragment);
    }

    /*replace fragment*/
    protected void replaceFragment(@IdRes int containerId, Fragment fragment) {
        replaceFragment(containerId, fragment, null);
    }

    /*replace fragment with anim*/
    protected void replaceFragment(@IdRes int containerId, Fragment fragment, @AnimatorRes @AnimRes int enterAnim, @AnimatorRes @AnimRes int outAnim) {
        replaceFragment(containerId, fragment, null, enterAnim, outAnim);
    }

    /*replace fragment with tag*/
    protected void replaceFragment(@IdRes int containerId, Fragment fragment, @Nullable String tag) {
        replaceFragment(containerId, fragment, tag, 0, 0);
    }

    protected void replaceFragment(@IdRes int containerId, Fragment fragment, @Nullable String tag, @AnimatorRes @AnimRes int enterAnim, @AnimatorRes @AnimRes int outAnim) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (enterAnim != 0 || outAnim != 0) {
            transaction.setCustomAnimations(enterAnim, outAnim);
        }
        if (!TextUtils.isEmpty(tag)) {
            transaction.replace(containerId, fragment, tag);
        } else {
            transaction.replace(containerId, fragment);
        }
        transaction.commitAllowingStateLoss();
        addFragmentToList(fragment);
    }

    /*remove fragment*/
    protected void removeFragment(Fragment fragment) {
        removeFragment(fragment, 0, 0);
    }

    /*remove fragment with anim*/
    protected void removeFragment(Fragment fragment, @AnimatorRes @AnimRes int enterAnim, @AnimatorRes @AnimRes int outAnim) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (enterAnim != 0 || outAnim != 0) {
            transaction.setCustomAnimations(enterAnim, outAnim);
        }
        transaction.remove(fragment).commitAllowingStateLoss();
        removeFragmentFromList(fragment);
    }

    @Override
    public void onBackPressed() {
        if (!canFragmentPopBackStack()) {
            super.onBackPressed();
        }
    }

    /*当activity添加多个fragment时，点击返回键是否finish当前Activity，返回true退出当前activity，否则移除栈顶fragment*/
    protected boolean isFragmentPopBackStack() {
        return true;
    }

    protected boolean canFragmentPopBackStack() {
        return canFragmentPopBackStack(0, R.anim.fragment_out_right);
    }

    protected boolean canFragmentPopBackStack(@AnimatorRes @AnimRes int enterAnim, @AnimatorRes @AnimRes int outAnim) {
        if (mFragments == null || mFragments.size() <= 1) {  //当前activity没有添加任何fragment或者只添加一个fragment，则直接结束当前activity
            return false;
        } else {
            Fragment topFragment = mFragments.get(mFragments.size() - 1);
            popBackStack(topFragment, enterAnim, outAnim);
            return true;
        }
    }

    protected void popBackStack(Fragment topFragment, @AnimatorRes @AnimRes int enterAnim, @AnimatorRes @AnimRes int outAnim) {
        removeFragment(topFragment, enterAnim, outAnim);
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStack();
        }
    }

    /*add fragment to list*/
    protected void addFragmentToList(Fragment fragment) {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        mFragments.add(fragment);
    }

    /*remove fragment from list*/
    protected void removeFragmentFromList(Fragment fragment) {
        if (mFragments != null) {
            mFragments.remove(fragment);
        }
    }

    /*获取当前activity中addFragment or replaceFragment fragment总数*/
    @NonNull
    protected List<Fragment> getFragments() {
        if (mFragments == null) return new ArrayList<>();
        return mFragments;
    }

    /*解决activity退出动画无效问题*/
    @Override
    public void finish() {
        super.finish();
        if (mActivityCloseEnterAnimation != -1 || mActivityCloseExitAnimation != -1) {
            overridePendingTransition(mActivityCloseEnterAnimation, mActivityCloseExitAnimation);
        }
    }
}
