package com.egecius.mockwebserver_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import retrofit.GitHubService;
import retrofit.RetrofitFactory;
import retrofit.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RetrofitFactory mRetrofitFactory = new RetrofitFactory();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.followers);

        callApiService();
    }

    private void callApiService() {
        GitHubService gitHubService = mRetrofitFactory.create((DemoApplication) getApplication());
        gitHubService.getUser("octocat").enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    textView.setText(getString(R.string.login_followers, user.login, user.followers));
                } else {
                    textView.setText(String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                textView.setText(t.getClass().getSimpleName());
            }
        });
    }
}
