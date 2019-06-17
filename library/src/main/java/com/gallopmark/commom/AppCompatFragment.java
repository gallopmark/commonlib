package com.gallopmark.commom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

/*fragment基础类*/
public abstract class AppCompatFragment extends Fragment {
    /*全局mActivity避免getActivity空指针异常*/
    protected Activity mActivity;

    /*startActivityForResult请求码*/
    private int mActivityRequestCode;
    /*activity result回调*/
    private OnActivityResultCallback mActivityResultCallback;
    /*权限申请请求码*/
    private int requestPermissionCode;
    /*权限申请回调*/
    private OnRequestPermissionsCallback requestCallback;

    protected FragmentManager mChildFragmentManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
        setChildFragmentManager();
    }


    protected void setChildFragmentManager() {
        mChildFragmentManager = getChildFragmentManager();
    }

    protected <T extends View> T findViewById(View contentView, int id) {
        return contentView.findViewById(id);
    }

    /*getResources*/
    protected Resources getResourcesCompat() {
        return mActivity.getResources();
    }

    /*fragment中getString()会空指针异常，原因是getActivity为空*/
    protected String getStringCompat(@StringRes int id) {
        return mActivity.getString(id);
    }

    /*获取color值*/
    protected int getColor(@ColorRes int id) {
        return ContextCompat.getColor(mActivity, id);
    }

    /*获取stringArray*/
    protected String[] getStringArray(@ArrayRes int id) {
        return getResourcesCompat().getStringArray(id);
    }

    /*获取drawable*/
    protected Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(mActivity, id);
    }

    /*获取dimens值*/
    protected float getDimension(@DimenRes int id) {
        return mActivity.getResources().getDimension(id);
    }

    /*将getDimension结果转换为int，并且小数部分四舍五入*/
    protected int getDimensionPixelSize(@DimenRes int id) {
        return mActivity.getResources().getDimensionPixelSize(id);
    }

    /*直接截断小数位，即取整其实就是把float强制转化为int，注意不是四舍五入*/
    protected int getDimensionPixelOffset(@DimenRes int id) {
        return mActivity.getResources().getDimensionPixelOffset(id);
    }

    protected int[] getIntArray(@ArrayRes int id) {
        return mActivity.getResources().getIntArray(id);
    }

    protected ColorStateList getColorStateList(@ColorRes int id) {
        return ContextCompat.getColorStateList(mActivity, id);
    }

    /*获取手机状态栏高度*/
    protected int getStatusBarHeight() {
        return SystemTintHelper.getStatusBarHeight(mActivity);
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
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    /*启动activity*/
    protected void startActivity(@NonNull final Class<? extends Activity> clz) {
        startActivity(clz, null);
    }

    protected void startActivity(@NonNull final Class<? extends Activity> clz, @Nullable Bundle bundle) {
        if (getActivity() == null) return;
        Intent intent = obtainIntent(clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected Intent obtainIntent(@NonNull final Class<? extends Activity> clz) {
        return new Intent(getActivity(), clz);
    }

    protected void startActivityForResult(@NonNull final Class<? extends Activity> clz, int requestCode, OnActivityResultCallback resultCallback) {
        startActivityForResult(clz, requestCode, null, resultCallback);
    }

    protected void startActivityForResult(@NonNull final Class<? extends Activity> clz, int requestCode, @Nullable Bundle options, OnActivityResultCallback resultCallback) {
        if (getActivity() == null) return;
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
        return ContextCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /*申请权限*/
    protected void requestPermissions(@NonNull String[] permissions, int requestCode, OnRequestPermissionsCallback callback) {
        if (getActivity() == null) return;
        this.requestPermissionCode = requestCode;
        this.requestCallback = callback;
        requestPermissions(permissions, requestCode);
    }

    /*权限申请回调*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestPermissionCode) {
            PermissionHelper.convert(this, requestCode, permissions, grantResults, requestCallback);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mActivityRequestCode && mActivityResultCallback != null) {
            if (resultCode == Activity.RESULT_OK) {
                mActivityResultCallback.onResultOk(requestCode, data);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                mActivityResultCallback.onResultCanceled(requestCode, data);
            }
        }
    }

    /*显示loading加载框*/
    protected void showLoadingDialog() {
        showLoadingDialog("");
    }

    /*显示带message的加载框,fragment的getActivity需继承CommonActivity*/
    protected void showLoadingDialog(@Nullable CharSequence message) {
        if (mActivity instanceof CommonActivity) {
            ((CommonActivity) mActivity).showLoadingDialog(message);
        }
    }

    /*消除加载框*/
    protected void dismissLoadingDialog() {
        if (mActivity instanceof CommonActivity) {
            ((CommonActivity) mActivity).dismissLoadingDialog();
        }
    }

    /*短时间显示Toast，子类也可以重写此方法自定义toast显示风格,fragment的getActivity需继承CommonActivity*/
    protected void showShortToast(CharSequence text) {
        if (mActivity instanceof CommonActivity) {
            ((CommonActivity) mActivity).showShortToast(text);
        }
    }

    /*长时间显示Toast，子类也可以重写此方法自定义toast显示风格,fragment的getActivity需继承CommonActivity*/
    protected void showLongToast(CharSequence text) {
        if (mActivity instanceof CommonActivity) {
            ((CommonActivity) mActivity).showLongToast(text);
        }
    }

    /*add child fragment*/
    protected void addChildFragment(@IdRes int containerId, Fragment fragment) {
        addChildFragment(containerId, fragment, 0, 0);
    }

    protected void addChildFragment(@IdRes int containerId, Fragment fragment, @AnimatorRes @AnimRes int enterAnim, @AnimatorRes @AnimRes int outAnim) {
        addChildFragment(containerId, fragment, null, enterAnim, outAnim);
    }

    protected void addChildFragment(@IdRes int containerId, Fragment fragment, @Nullable String tag) {
        addChildFragment(containerId, fragment, tag, 0, 0);
    }

    /*取消fragment动画传入参数 enterAnim = 0 && outAnim == 0*/
    protected void addChildFragment(@IdRes int containerId, Fragment fragment, @Nullable String tag, @AnimatorRes @AnimRes int enterAnim, @AnimatorRes @AnimRes int outAnim) {
        FragmentTransaction transaction = mChildFragmentManager.beginTransaction();
        if (enterAnim != 0 || outAnim != 0) {
            transaction.setCustomAnimations(enterAnim, 0);
        }
        if (!TextUtils.isEmpty(tag)) {
            transaction.add(containerId, fragment, tag);
        } else {
            transaction.add(containerId, fragment);
        }
        transaction.commitAllowingStateLoss();
    }

    /*replace fragment*/
    protected void replaceChildFragment(@IdRes int containerId, Fragment fragment) {
        replaceChildFragment(containerId, fragment, null);
    }

    protected void replaceChildFragment(@IdRes int containerId, Fragment fragment, @AnimatorRes @AnimRes int enterAnim, @AnimatorRes @AnimRes int outAnim) {
        replaceChildFragment(containerId, fragment, null, enterAnim, outAnim);
    }

    protected void replaceChildFragment(@IdRes int containerId, Fragment fragment, @Nullable String tag) {
        replaceChildFragment(containerId, fragment, tag, 0, 0);
    }

    protected void replaceChildFragment(@IdRes int containerId, Fragment fragment, @Nullable String tag, @AnimatorRes @AnimRes int enterAnim, @AnimatorRes @AnimRes int outAnim) {
        FragmentTransaction transaction = mChildFragmentManager.beginTransaction();
        if (enterAnim != 0 || outAnim != 0) {
            transaction.setCustomAnimations(enterAnim, outAnim);
        }
        if (!TextUtils.isEmpty(tag)) {
            transaction.replace(containerId, fragment, tag);
        } else {
            transaction.replace(containerId, fragment);
        }
        transaction.commitAllowingStateLoss();
    }

    protected void removeChildFragment(Fragment fragment) {
        removeChildFragment(fragment, 0, 0);
    }

    protected void removeChildFragment(Fragment fragment, @AnimatorRes @AnimRes int enterAnim, @AnimatorRes @AnimRes int outAnim) {
        FragmentTransaction transaction = mChildFragmentManager.beginTransaction();
        if (enterAnim != 0 || outAnim != 0) {
            transaction.setCustomAnimations(enterAnim, outAnim);
        }
        transaction.remove(fragment).commitAllowingStateLoss();
    }
}
