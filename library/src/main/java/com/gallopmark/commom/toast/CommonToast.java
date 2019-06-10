package com.gallopmark.commom.toast;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gallopmark.commom.R;

import java.lang.reflect.Field;

/*自定义toast 解决toast频繁显示的问题
 * */
public class CommonToast {

    /*自定义toast动画*/
    class AnimationToast extends Toast {

        @SuppressWarnings("JavaReflectionMemberAccess")
        AnimationToast(Context context, int windowAnimations) {
            super(context);
            Class<Toast> clazz = Toast.class;
            try {
                Field mTN = clazz.getDeclaredField("mTN");
                mTN.setAccessible(true);
                Object mObj = mTN.get(this);
                // 取消掉各个系统的默认toast弹出动画
                Field field = mObj.getClass().getDeclaredField("mParams");
                field.setAccessible(true);
                Object mParams = field.get(mObj);
                if (mParams == null) return;
                if (mParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
                    params.windowAnimations = windowAnimations;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Context mContext;
    private Toast mToast;
    private View mContentView;
    private TextView mToastTextView;
    private int mDuration = Toast.LENGTH_SHORT;
    private int mGravity = -1;
    private float mHorizontalMargin = -1, mVerticalMargin = -1;
    private int mWindowAnimations = R.style.CommonToastStyle; //默认toast动画 淡入淡出

    private CommonToast(Context context) {
        this.mContext = context;
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_common_toast, new LinearLayout(mContext), false);
        mToastTextView = mContentView.findViewById(R.id.mToastTextView);
    }

    public static CommonToast from(Context context) {
        return new CommonToast(context);
    }

    public static CommonToast makeText(Context context, CharSequence text, int duration) {
        CommonToast toast = new CommonToast(context);
        toast.with(text).setDuration(duration);
        return toast;
    }

    public static CommonToast makeText(Context context, @StringRes int resId, int duration) {
        CommonToast toast = new CommonToast(context);
        toast.with(resId).setDuration(duration);
        return toast;
    }

    public FluentInitializer with(@StringRes int resId) {
        return new FluentInitializer(resId);
    }

    public FluentInitializer with(CharSequence text) {
        return new FluentInitializer(text);
    }

    public class FluentInitializer {

        FluentInitializer(@StringRes int resId) {
            this(mContext.getString(resId));
        }

        FluentInitializer(CharSequence text) {
            mToastTextView.setText(text);
        }

        public FluentInitializer setWindowAnimations(int windowAnimations) {
            mWindowAnimations = windowAnimations;
            return this;
        }

        public FluentInitializer setContentPadding(int left, int top, int right, int bottom) {
            mContentView.setPadding(left, top, right, bottom);
            return this;
        }

        public FluentInitializer setBackground(Drawable background) {
            ViewCompat.setBackground(mContentView, background);
            return this;
        }

        public FluentInitializer setBackgroundColor(@ColorInt int color) {
            mContentView.setBackgroundColor(color);
            return this;
        }

        public FluentInitializer setBackgroundResource(@DrawableRes int resId) {
            mContentView.setBackgroundResource(resId);
            return this;
        }

        /*设置文本大小*/
        public FluentInitializer setTextSize(float size) {
            mToastTextView.setTextSize(size);
            return this;
        }

        /*TypedValue*/
        public FluentInitializer setTextSize(int unit, float size) {
            mToastTextView.setTextSize(unit, size);
            return this;
        }

        public FluentInitializer setTextColor(@ColorInt int color) {
            mToastTextView.setTextColor(color);
            return this;
        }

        public FluentInitializer setDuration(int duration) {
            mDuration = duration;
            return this;
        }

        public FluentInitializer setGravity(int gravity) {
            mGravity = gravity;
//            mToast.setGravity(gravity, 0, 0);
            return this;
        }

        public FluentInitializer setMargin(float horizontalMargin, float verticalMargin) {
            mHorizontalMargin = horizontalMargin;
            mVerticalMargin = verticalMargin;
            return this;
        }
    }

    /*先判断toast知否为null，不为null则取消toast并设为null*/
    public void show() {
        cancel();
        mToast = new AnimationToast(mContext, mWindowAnimations);  /*new Toast创建toast 没有设置view会报错*/
        mToast.setView(mContentView);
        mToast.setDuration(mDuration);  //默认toast时间
        if (mGravity != -1) {
            mToast.setGravity(mGravity, 0, 0);
        }
        if (mHorizontalMargin != -1 || mVerticalMargin != -1) {
            mToast.setMargin(mHorizontalMargin, mVerticalMargin);
        }
        mToast.show();
    }

    public void cancel() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
