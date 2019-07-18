package com.hd.wallpaper.background.Retrofit;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor
{
    private String clientId;

    public HeaderInterceptor(String clientId)
    {
        this.clientId = clientId;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException
    {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization","Client-ID " + clientId)
                .build();
        return chain.proceed(request);
    }
}
