package com.egecius.mockwebserver_demo;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import runner.TestDemoApplication;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    public static final String JSON = "{\"login\":\"octocat\",\"followers\":\"1500\"}";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    MockWebServer server = new MockWebServer();

    @Before
    public void setup() {
        setBaseUrl();
    }

    private void setBaseUrl() {
        TestDemoApplication app =
                (TestDemoApplication) InstrumentationRegistry.getTargetContext()
                        .getApplicationContext();
        String mockServerUrl = server.url("/").toString();
        app.setBaseUrl(mockServerUrl);
    }

    @Test
    public void firstTest() throws IOException {
        server.start();

        server.enqueue(new MockResponse().setBody(JSON));

        server.shutdown();
    }


}