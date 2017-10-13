package com.egecius.mockwebserver_demo;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import runner.TestDemoApplication;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private static final String OCTOCAT_BODY = "{\"login\":\"octocat\",\"followers\":\"1500\"}";
    private static final String DOG_BODY = "{\"login\":\"dog\",\"followers\":\"1500\"}";

    @Rule
    public MockWebServerRule mockWebServerRule = new MockWebServerRule();
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    private MockWebServer server = mockWebServerRule.server;

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
    public void mocksSuccessResponse() throws IOException {
        givenSeverWillReturnSuccess();

        launchActivity();

        onView(withId(R.id.followers)).check(matches(withText("octocat: 1500")));
    }

    private void givenSeverWillReturnSuccess() {
        server.enqueue(new MockResponse().setBody(OCTOCAT_BODY));
    }

    @Test
    public void mocksErrorResponse() {
        server.enqueue(new MockResponse().setResponseCode(404));

        launchActivity();

        onView(withId(R.id.followers)).check(matches(withText("404")));
    }

    @Test
    public void malformedJson() {
        server.enqueue(new MockResponse().setBody("malformed_json"));

        launchActivity();

        onView(withId(R.id.followers)).check(matches(withText("MalformedJsonException")));
    }

    private void launchActivity() {
        activityRule.launchActivity(null);
    }

    @Ignore // for some reason it does not work
    @Test
    public void failsOnThrottling() {
        server.enqueue(
                new MockResponse()
                        .setBody(OCTOCAT_BODY)
                        .throttleBody(1, 1, MILLISECONDS));

        launchActivity();

        onView(withId(R.id.followers)).check(matches(withText("SocketTimeoutException")));
    }

    @Test
    public void verifyRecordedRequest() throws InterruptedException {
        givenSeverWillReturnSuccess();

        launchActivity();

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("", recordedRequest.getBody().readUtf8());
        assertEquals("GET /users/octocat HTTP/1.1", recordedRequest.getRequestLine());
        assertEquals("/users/octocat", recordedRequest.getPath());

        Headers headers = recordedRequest.getHeaders();
        assertEquals(4, headers.size());
        assertEquals("gzip", headers.get("Accept-Encoding"));
        assertEquals("Keep-Alive", headers.get("Connection"));
        assertEquals("okhttp/3.8.0", headers.get("User-Agent"));
    }

    @Test
    public void useDispatcherForMockingLogic() {
        givenWillReturnSuccessWithDispatcher();

        launchActivityWithUser("octocat");

        onView(withId(R.id.followers)).check(matches(withText("octocat: 1500")));
    }

    private void givenWillReturnSuccessWithDispatcher() {
        // TODO: 13/10/2017 remove duplication in MockedDataInjector
        server.setDispatcher(new Dispatcher() {
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

    private void launchActivityWithUser(String dog) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.KEY_USER_NAME, dog);
        activityRule.launchActivity(intent);
    }

    @Test
    public void useDispatcherForMockingLogic2() {
        givenWillReturnSuccessWithDispatcher();

        launchActivityWithUser("dog");

        onView(withId(R.id.followers)).check(matches(withText("dog: 1500")));
    }

}