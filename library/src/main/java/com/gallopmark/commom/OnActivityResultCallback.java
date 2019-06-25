package com.gallopmark.commom;

import android.content.Intent;
import androidx.annotation.Nullable;

public abstract class OnActivityResultCallback {
    abstract void onResultOk(int requestCode, @Nullable Intent data);

    void onResultCanceled(int requestCode, @Nullable Intent data) {
    }
}
