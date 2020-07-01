package com.huawei.probation.packagedeliveryapp.network;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    public RequestInterceptor() {
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        HttpUrl originalHttpUrl = chain.request().url().newBuilder()
                .addQueryParameter("key", "CgB6e3x9917QHuu3H/bRUeqZUAGwEJYp1p7NIlhcWubs7Bc6FG7PrnDCWUl+JAw/u91OtDmm6iAnhy6RAvm62JaD")
                .build();

        Request originalRequest = chain.request().newBuilder()
                .header("Content-Type","application/json")
                .url(originalHttpUrl)
                .build();
        return chain.proceed(originalRequest);
    }
}