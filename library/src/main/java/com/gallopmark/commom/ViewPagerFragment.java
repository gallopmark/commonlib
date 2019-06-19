package com.gallopmark.commom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*viewpager中懒加载fragment*/
public abstract class ViewPagerFragment extends AppCompatFragment {

    private boolean isLazyLoaded = false;

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
        if (!isLazyLoaded && isViewInflated) {
            loadDataStart();
            isLazyLoaded = true;
        }
    }

    /**
     * 用于异步加载数据，只加载一次数据
     */
    protected void loadDataStart() {

    }
}
