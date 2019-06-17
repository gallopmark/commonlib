package com.gallopmark.commom.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gallopmark.commom.CommonDialog;
import com.gallopmark.commom.R;

public class CommonLoadingDialog extends CommonDialog {

    private CharSequence message;

    public CommonLoadingDialog(Context context) {
        super(context);
    }

    public CharSequence getMessage() {
        return message;
    }

    public CommonLoadingDialog setMessage(CharSequence message) {
        this.message = message;
        return this;
    }

    @Override
    protected int bindContentView() {
        return R.layout.layout_common_loadingdialog;
    }

    @Override
    protected void initView(View mContentView) {
        TextView mLoadingMessageTv = mContentView.findViewById(R.id.mLoadingMessageTv);
        if (!TextUtils.isEmpty(getMessage())) {
            mLoadingMessageTv.setText(message);
            mLoadingMessageTv.setVisibility(View.VISIBLE);
        } else {
            mLoadingMessageTv.setVisibility(View.GONE);
        }
    }
}
