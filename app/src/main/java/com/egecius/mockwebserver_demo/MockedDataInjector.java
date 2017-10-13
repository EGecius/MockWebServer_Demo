package com.egecius.mockwebserver_demo;


import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

class MockedDataInjector {

    public static final String TAG = MockedDataInjector.class.getSimpleName();


    private final MockWebServer mMockWebServer = new MockWebServer();
    private Context context;

    /** Injects mocked data into all backend responses */
    void injectData(final DemoApplication application) {
        context = application.getApplicationContext();

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
                    return new MockResponse().setBody(readOctocatBody());
                } else if (path.equals("/users/dog")) {
                    return new MockResponse().setBody(readDogBody());
                }

                throw new IllegalArgumentException("not implemented");
            }
        });
    }

    private String readDogBody() {
        return readFromFile("dog_body.json");
    }

    private String readOctocatBody() {
        return readFromFile("octocat_body.json");
    }

    private String readFromFile(String filename) {

        Log.i(TAG, "readFromFile filename " + filename);

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String string = new String(buffer);

            Log.i(TAG, "readFromFile string " + string);

            return string;

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
