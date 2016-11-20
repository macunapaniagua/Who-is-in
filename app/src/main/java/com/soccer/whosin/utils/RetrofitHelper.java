package com.soccer.whosin.utils;

import com.soccer.whosin.interfaces.WhoIsInService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mario A on 22/10/2016.
 **/
public class RetrofitHelper {

    public static WhoIsInService getAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(WhoIsInService.class);
    }

    public static WhoIsInService getAPIWithHeaders(String pFacebookId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient(pFacebookId))
                .build();
        return retrofit.create(WhoIsInService.class);
    }

    /**
     * Create Client Interceptor to add custom headers
     * @return HttpClient
     */
    private static OkHttpClient getClient(final String pFacebookId) {
        // Request Logger
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // HttpClient
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header(Constants.USER_FB_KEY, pFacebookId)
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        }).addInterceptor(loggingInterceptor).build();
    }
}
