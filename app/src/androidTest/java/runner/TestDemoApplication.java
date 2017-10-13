package runner;

import com.egecius.mockwebserver_demo.DemoApplication;

public class TestDemoApplication extends DemoApplication {

    private String baseUrl;

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public void setBaseUrl(String url) {
        baseUrl = url;
    }
}
