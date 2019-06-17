package com.gallopmark.common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.gallopmark.commom.CommonFragment;

public class TestFragment2 extends CommonFragment {
    @Override
    protected int bindLayoutId(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_test2;
    }

    @Override
    protected void bindView(View view) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                replaceChildFragment(R.id.mContainer, new TestFragment3());
            }
        }, 1500);
    }
}
