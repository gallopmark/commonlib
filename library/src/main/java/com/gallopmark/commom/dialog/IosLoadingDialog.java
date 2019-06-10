package com.gallopmark.commom.dialog;

import android.content.Context;

import com.gallopmark.commom.R;

public class IosLoadingDialog extends CommonLoadingDialog{

    public IosLoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected int bindContentView() {
        return R.layout.layout_ios_loading_dialog;
    }
}
