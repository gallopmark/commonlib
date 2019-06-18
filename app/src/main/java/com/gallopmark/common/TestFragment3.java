package com.gallopmark.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;


import com.gallopmark.commom.CommonFragment;

public class TestFragment3 extends CommonFragment {
    @Override
    protected int bindLayoutId(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_test3;
    }

    @Override
    protected void bindView(@NonNull View view) {
    }
}
