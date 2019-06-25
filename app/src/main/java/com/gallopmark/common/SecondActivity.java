package com.gallopmark.common;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.gallopmark.commom.CommonActivity;

public class SecondActivity extends CommonActivity {

    @Override
    protected void setupTheme() {
        setTheme(R.style.CommonActivityAnimation);
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    protected void initialize(@Nullable Bundle savedInstanceState) {

    }
}
