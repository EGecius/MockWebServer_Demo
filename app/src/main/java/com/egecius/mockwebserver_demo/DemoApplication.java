package com.egecius.mockwebserver_demo;

import android.app.Application;

public class DemoApplication extends Application {

    boolean USE_MOCKED_DATA = true;

    MockedDataInjector mMockedDataInjector = new MockedDataInjector();

    private String baseUrl = "https://api.github.com";

    @Override
    public void onCreate() {
        super.onCreate();

        useMockedDataIfNeeded();
    }

    private void useMockedDataIfNeeded() {
        if (USE_MOCKED_DATA) {
            mMockedDataInjector.inject(this);
        }
    }

    public String getBaseUrl() {

        return baseUrl;
    }

    public void setBaseUrl(String url) {
        baseUrl = url;
    }
}
