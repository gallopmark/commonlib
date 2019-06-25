package com.gallopmark.commom.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.TextViewCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.lang.reflect.Field;

/*自定义toolbar解决标题居中，NavButtonView和MenuView一起居中*/
public class WrapperToolbar extends Toolbar {

    private TextView mTitleTextView;  //title textView
    private CharSequence mTitleText;    //title text

    public WrapperToolbar(Context context) {
        super(context);
        resolveAttribute(context);
    }

    public WrapperToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        resolveAttribute(context);
    }

    public WrapperToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resolveAttribute(context);
    }

    private void resolveAttribute(Context context) {
        int titleTextAppearance = getTitleTextAppearance();
        if (titleTextAppearance != 0) {
            setTitleTextAppearance(context, titleTextAppearance);
        }
        int titleTextColor = getTitleTextColor();
        if (titleTextColor != 0) {
            setTitleTextColor(titleTextColor);
        }
        post(new Runnable() {
            @Override
            public void run() {
                if (getLayoutParams() instanceof LayoutParams) {
                    ((LayoutParams) getLayoutParams()).gravity = Gravity.CENTER;
                }
            }
        });
    }

    @Override
    public CharSequence getTitle() {
        return mTitleText;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            if (mTitleTextView == null) {
                mTitleTextView = new AppCompatTextView(getContext());
                mTitleTextView.setSingleLine();
                mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                if (getTitleTextAppearance() != 0) {
                    TextViewCompat.setTextAppearance(mTitleTextView, getTitleTextAppearance());
                }
                if (getTitleTextColor() != 0) {
                    mTitleTextView.setTextColor(getTitleTextColor());
                }
            }
            if (mTitleTextView.getParent() != this) {
                addTitleCenter(mTitleTextView);
            }
        } else if (mTitleTextView != null && mTitleTextView.getParent() == this) {// 当title为空时，remove
            removeView(mTitleTextView);
        }
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
        mTitleText = title;
    }

    private void addTitleCenter(View v) {
        ViewGroup.LayoutParams vlp = v.getLayoutParams();
        LayoutParams lp;
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vlp)) {
            lp = generateLayoutParams(vlp);
        } else {
            lp = (LayoutParams) vlp;
        }
        lp.gravity = Gravity.CENTER;
        lp.setMargins(getTitleMarginStart(), getTitleMarginTop(), getTitleMarginEnd(), getTitleMarginBottom());
        addView(v, lp);
    }

    @Override
    public void setTitleTextAppearance(Context context, int resId) {
        if (mTitleTextView != null) {
            TextViewCompat.setTextAppearance(mTitleTextView, resId);
        }
    }

    @Override
    public void setTitleTextColor(int color) {
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(color);
        }
    }

    @Nullable
    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    @Override
    public void setNavigationIcon(@Nullable Drawable icon) {
        super.setNavigationIcon(icon);
        setGravityCenter();
    }

    private void setGravityCenter() {
        post(new Runnable() {
            @Override
            public void run() {
                setCenter("mNavButtonView");
                setCenter("mMenuView");
            }
        });
    }

    private void setCenter(String fieldName) {
        Object obj = getField(fieldName);//拿到对应的Object
        if (obj == null) return;
        if (obj instanceof View) {
            ViewGroup.LayoutParams lp = ((View) obj).getLayoutParams();//拿到LayoutParams
            if (lp instanceof ActionBar.LayoutParams) {
                ((ActionBar.LayoutParams) lp).gravity = Gravity.CENTER;//设置居中
                ((View) obj).setLayoutParams(lp);
            }
        }
    }

    /*通过反射获取titleTextColor*/
    protected int getTitleTextColor() {
        return getFieldIntValue("mTitleTextColor");
    }

    /*通过反射获取mTitleTextAppearance*/
    protected int getTitleTextAppearance() {
        return getFieldIntValue("mTitleTextAppearance");
    }

    /*通过反射获取int属性变量值*/
    protected int getFieldIntValue(String fieldName) {
        Object obj = getField(fieldName);
        if (obj == null) {
            return 0;
        }
        try {
            return (int) obj;
        } catch (Exception e) {
            return 0;
        }
    }

    /*通过反射获取属性值*/
    @Nullable
    protected Object getField(@NonNull String fieldName) {
        try {
            if (getClass().getSuperclass() == null) return null;
            Field field = getClass().getSuperclass().getDeclaredField(fieldName);//反射得到父类Field
            field.setAccessible(true);
            return field.get(this);
        } catch (Exception e) {
            return null;
        }
    }
}
