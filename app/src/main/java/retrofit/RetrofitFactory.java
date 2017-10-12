package retrofit;

import static java.util.concurrent.TimeUnit.SECONDS;

import android.support.annotation.NonNull;

import com.egecius.mockwebserver_demo.DemoApplication;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private DemoApplication mApplication;

    public GitHubService create(DemoApplication application) {
        mApplication = application;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GitHubService.class);
    }

    @NonNull
    private String getBaseUrl() {
        return mApplication.getBaseUrl();
    }

    private OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(1, SECONDS)
                .readTimeout(1, SECONDS)
                .build();
    }
}
