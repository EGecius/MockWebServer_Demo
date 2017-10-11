package com.egecius.mockwebserver_demo;

import android.app.Application;

public abstract class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public abstract String getBaseUrl();
}
