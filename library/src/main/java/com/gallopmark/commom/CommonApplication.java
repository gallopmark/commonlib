package com.gallopmark.commom;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/*application通用类，解决multiDexException*/
public abstract class CommonApplication extends Application {
    /*全局变量*/
    private static CommonApplication mApp;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    public static CommonApplication getApp() {
        return mApp;
    }
}
