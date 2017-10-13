package com.egecius.mockwebserver_demo;


import java.io.IOException;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

class MockedDataInjector {

    private static final String OCTOCAT_BODY = "{\"login\":\"octocat\",\"followers\":\"1500\"}";
    private static final String DOG_BODY = "{\"login\":\"dog\",\"followers\":\"1500\"}";

    private final MockWebServer mMockWebServer = new MockWebServer();

    /** Injects mocked data into all backend responses */
    void injectData(final DemoApplication application) {
        Completable.create(e -> {
            setupServer(application);
            e.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void setupServer(DemoApplication application) {
        startServer();
        passBaseUrl(application);
        mockResponses();
    }

    private void startServer() {
        try {
            mMockWebServer.start();
        } catch (IOException e) {
            throw new RuntimeException("server start failed");
        }
    }

    private void passBaseUrl(DemoApplication application) {
        application.setBaseUrl(mMockWebServer.url("/").toString());
    }

    private void mockResponses() {
        mMockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

                String path = request.getPath();
                if (path.equals("/users/octocat")) {
                    return new MockResponse().setBody(OCTOCAT_BODY);
                } else if (path.equals("/users/dog")) {
                    return new MockResponse().setBody(DOG_BODY);
                }

                throw new IllegalArgumentException("not implemented");
            }
        });
    }
}
