package com.gallopmark.commom.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.appcompat.R;

import java.lang.reflect.Field;

/*自定义toolbar解决标题居中，NavButtonView和MenuView一起居中*/
public class WrapperToolbar extends Toolbar {

    private TextView mTitleTextView;
    private CharSequence mTitleText;
    private int mTitleTextColor = 0;
    private int mTitleTextAppearance = 0;

    public WrapperToolbar(Context context) {
        super(context);
        resolveAttribute(context, null, R.attr.toolbarStyle);
    }

    public WrapperToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        resolveAttribute(context, attrs, R.attr.toolbarStyle);
    }

    public WrapperToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resolveAttribute(context, attrs, R.attr.toolbarStyle);
    }

    @SuppressLint("RestrictedApi,PrivateResource")
    private void resolveAttribute(Context context, AttributeSet attrs, int defStyleAttr) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.Toolbar, defStyleAttr, 0);
        int titleTextAppearance = a.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
        if (titleTextAppearance != 0) {
            setTitleTextAppearance(context, titleTextAppearance);
        }
        int titleTextColor = a.getColor(R.styleable.Toolbar_titleTextColor, 0);
        if (titleTextColor != 0) {
            setTitleTextColor(titleTextColor);
        }
        a.recycle();
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
                if (mTitleTextAppearance != 0) {
                    TextViewCompat.setTextAppearance(mTitleTextView, mTitleTextAppearance);
                }
                if (mTitleTextColor != 0) {
                    mTitleTextView.setTextColor(mTitleTextColor);
                }
            }
            if (mTitleTextView.getParent() != this) {
                addCenterView(mTitleTextView);
            }
        } else if (mTitleTextView != null && mTitleTextView.getParent() == this) {// 当title为空时，remove
            removeView(mTitleTextView);
        }
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
        mTitleText = title;
    }

    private void addCenterView(View v) {
        ViewGroup.LayoutParams vlp = v.getLayoutParams();
        LayoutParams lp;
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vlp)) {
            lp = generateLayoutParams(vlp);
        } else {
            lp = (LayoutParams) vlp;
        }
        lp.setMargins(getTitleMarginStart(), getTitleMarginTop(), getTitleMarginEnd(), getTitleMarginBottom());
        addView(v, lp);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        LayoutParams lp = new LayoutParams(getContext(), attrs);
        lp.gravity = Gravity.CENTER;
        return lp;
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        LayoutParams lp;
        if (p instanceof LayoutParams) {
            lp = new LayoutParams((LayoutParams) p);
        } else if (p instanceof ActionBar.LayoutParams) {
            lp = new LayoutParams((ActionBar.LayoutParams) p);
        } else if (p instanceof MarginLayoutParams) {
            lp = new LayoutParams((MarginLayoutParams) p);
        } else {
            lp = new LayoutParams(p);
        }
        lp.gravity = Gravity.CENTER;
        return lp;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        return lp;
    }

    @Override
    public void setTitleTextAppearance(Context context, int resId) {
        mTitleTextAppearance = resId;
        if (mTitleTextView != null) {
            TextViewCompat.setTextAppearance(mTitleTextView, resId);
        }
    }

    @Override
    public void setTitleTextColor(int color) {
        mTitleTextColor = color;
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(color);
        }
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
        try {
            if (getClass().getSuperclass() == null) return;
            Field field = getClass().getSuperclass().getDeclaredField(fieldName);//反射得到父类Field
            field.setAccessible(true);
            Object obj = field.get(this);//拿到对应的Object
            if (obj == null) return;
            if (obj instanceof View) {
                ViewGroup.LayoutParams lp = ((View) obj).getLayoutParams();//拿到LayoutParams
                if (lp instanceof ActionBar.LayoutParams) {
                    ((ActionBar.LayoutParams) lp).gravity = Gravity.CENTER;//设置居中
                    ((View) obj).setLayoutParams(lp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
