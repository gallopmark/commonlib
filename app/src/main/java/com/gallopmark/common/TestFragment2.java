package com.gallopmark.common;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;

import com.gallopmark.commom.CommonFragment;

public class TestFragment2 extends CommonFragment {
    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_test2;
    }

    @Override
    protected void bindView(@NonNull View view) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                replaceChildFragment(R.id.mContainer, new TestFragment3());
            }
        }, 1500);
    }
}
