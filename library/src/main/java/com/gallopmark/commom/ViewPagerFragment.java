package com.gallopmark.commom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*viewpager中懒加载fragment*/
public abstract class ViewPagerFragment extends AppCompatFragment {

    protected View mRootContainer;

    private boolean isLazyLoaded = false;
    private boolean mViewInflateFinished = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootContainer == null) {
            mRootContainer = inflater.inflate(bindLayoutId(container, savedInstanceState), container, false);
            bindView(mRootContainer);
        }
        mViewInflateFinished = true;
        return mRootContainer;
    }

    protected abstract void bindView(@NonNull View contentView);

    protected abstract int bindLayoutId(@Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /*
     * fragment在viewPager使用，采用全局变量赋值方法,
     * kotlin插件引用view在onDestroyView方法执行后会抛出空指针异常，
     * 原因是view已经解除绑定，类似ButterKnife unbind
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    protected <T extends View> T obtainView(int id) {
        if (mRootContainer == null)
            throw new NullPointerException("mRootContainer not initialized");
        return mRootContainer.findViewById(id);
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
