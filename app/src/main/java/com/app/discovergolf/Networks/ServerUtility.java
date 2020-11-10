package com.app.discovergolf.Networks;

import android.app.Application;
import android.content.Context;


import com.app.discovergolf.API.WebUtility;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.BuildConfig;
import timber.log.Timber;

/**
 * Created by simar on 10/12/2016.
 */
public class ServerUtility extends Application {
    public static ServerUtility get(Context ctx) {
        return (ServerUtility) ctx.getApplicationContext();
    }
    public static API getApi(Context ctx) {
        return ServerUtility.get(ctx).api;
    }
    private API api;
    private static ServerUtility mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) +
                            ":timber: line=" + element.getLineNumber() +
                            " method: " + element.getMethodName();
                }
            });
        }
        api = createApi();
    }

    public static synchronized ServerUtility getInstance() {
        return mInstance;
    }
    private API createApi() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);
        if (BuildConfig.DEBUG) {
            client.addInterceptor(interceptor);
        }
        return new Retrofit.Builder()
                .baseUrl(WebUtility.BASE_URL1)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(API.class);
    }
}