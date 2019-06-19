package com.gallopmark.commom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public abstract class CommonFragment extends AppCompatFragment {

    /*kotlin语言，必须在onViewCreated方法里或后续生命周期方法里使用view Id组件，否则会出现NullPointerException*/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
        loadDataStart();
    }

    /**
     * View creation completes the call, avoiding kotlin throwing view NullPointerException
     */
    protected void setupView() {

    }

    protected void loadDataStart() {

    }
}
