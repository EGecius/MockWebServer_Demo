package retrofit;

import android.support.annotation.NonNull;

import com.egecius.mockwebserver_demo.DemoApplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private DemoApplication mApplication;

    public GitHubService create(DemoApplication application) {
        mApplication = application;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

       return retrofit.create(GitHubService.class);

    }

    @NonNull
    private String getBaseUrl() {
        return mApplication.getBaseUrl();
    }
}
