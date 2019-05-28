package com.gallopmark.commom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*viewpager中懒加载fragment*/
public abstract class ViewPagerFragment extends AppCompatFragment {

    protected View rootContainer;

    private boolean isLazyLoaded = false;
    private boolean mViewInflateFinished = false;

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootContainer != null) {
            mViewInflateFinished = true;
            return rootContainer;
        }
        rootContainer = inflater.inflate(onLazyCreateView(container, savedInstanceState), container, false);
        mViewInflateFinished = true;
        return rootContainer;
    }

    protected abstract int onLazyCreateView(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (rootContainer == null) {
            rootContainer = view;
        }
        if (getUserVisibleHint()) {
            onLazyLoad();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onLazyLoad();
        }
    }

    private void onLazyLoad() {
        if (!isLazyLoaded && mViewInflateFinished) {
            loadDataStart();
            isLazyLoaded = true;
        }
    }

    protected <T extends View> T findViewById(int id) {
        if (rootContainer == null) throw new NullPointerException("rootContainer not initialized");
        return rootContainer.findViewById(id);
    }

    /**
     * 用于异步加载数据，只加载一次数据
     */
    protected void loadDataStart() {

    }

    @Override
    public void onDestroyView() {
        mViewInflateFinished = false;
        super.onDestroyView();
    }
}
