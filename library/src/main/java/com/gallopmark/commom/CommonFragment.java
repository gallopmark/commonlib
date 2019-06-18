package com.gallopmark.commom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class CommonFragment extends AppCompatFragment {

    protected View mContentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView != null) return mContentView;
        mContentView = inflater.inflate(bindLayoutId(container, savedInstanceState), container, false);
        bindView(mContentView);
        return mContentView;
    }

    protected abstract int bindLayoutId(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /*kotlin语言，必须在onViewCreated方法里或后续生命周期方法里使用view Id组件，否则会出现NullPointerException*/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        loadDataStart();
    }

    protected abstract void bindView(@NonNull View view);

    /*if you use kotlin, No need to use obtainView*/
    protected <T extends View> T obtainView(int id) {
        if (mContentView == null) throw new NullPointerException("mContentView not initialized");
        return mContentView.findViewById(id);
    }

    /**
     * View creation completes the call, avoiding kotlin throwing view NullPointerException
     */
    protected void setupView() {

    }

    protected void loadDataStart() {

    }
}
