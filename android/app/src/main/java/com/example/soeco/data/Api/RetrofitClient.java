package com.example.soeco.data.Api;

import android.util.Base64;

import com.example.soeco.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = BuildConfig.APIURL;
    private static RetrofitClient mInstance;
    private final Retrofit retrofit;

    private RetrofitClient(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
                retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    // singleton
    public static synchronized RetrofitClient getInstance(){
        if(mInstance==null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getAPI() {
        return retrofit.create(Api.class);
    }
}