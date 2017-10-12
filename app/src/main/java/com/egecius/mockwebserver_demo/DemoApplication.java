package com.egecius.mockwebserver_demo;

import android.app.Application;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getBaseUrl() {
        return "https://api.github.com";
    }
}
