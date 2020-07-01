package com.huawei.probation.packagedeliveryapp.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static RetrofitClientInstance retrofitClientInstance = null;
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://mapapi.cloud.huawei.com/mapApi/v1/";

    private RetrofitClientInstance(){
        retrofit = getRetrofitClient();
    }

    public static <T> T createService(Class<T> serviceClass) {
        if(retrofitClientInstance == null){
            retrofitClientInstance = new RetrofitClientInstance();
        }
        return (T) retrofit.create(serviceClass);
    }

    private Retrofit getRetrofitClient() {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    }

    private OkHttpClient getOkHttpClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new RequestInterceptor())
                .addInterceptor(loggingInterceptor)
                .build();
    }
}