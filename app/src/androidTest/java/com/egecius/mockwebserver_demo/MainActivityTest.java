package com.egecius.mockwebserver_demo;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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

    private static final String OCTOCAT_BODY = "{\"login\":\"octocat\",\"followers\":\"1500\"}";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    MockWebServer server = new MockWebServer();

    @Before
    public void setup() {
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

        setBaseUrl();

        server.enqueue(new MockResponse().setBody(OCTOCAT_BODY));

        mActivityRule.launchActivity(null);

        onView(withId(R.id.followers)).check(matches(withText("octocat: 1500")));

        server.shutdown();
    }

}