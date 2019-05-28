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
        return mContentView;
    }

    protected abstract int bindLayoutId(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mContentView == null) {
            mContentView = view;
        }
        bindView(mContentView);
        loadDataStart();
    }

    /*if you use kotlin, No need to use findViewById*/
    protected abstract void bindView(View view);

    protected <T extends View> T findViewById(int id) {
        if (mContentView == null) throw new NullPointerException("mContentView not initialized");
        return mContentView.findViewById(id);
    }

    protected void loadDataStart() {

    }
}
