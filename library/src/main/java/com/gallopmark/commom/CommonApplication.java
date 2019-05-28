package com.gallopmark.commom;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public abstract class CommonApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
