package me.alwx.appbase.api;

import android.support.annotation.NonNull;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import me.alwx.appbase.BuildConfig;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * @author alwx (http://alwx.me)
 * @version 1.0
 */
public final class API {
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;

    private static final OkHttpClient sClient = new OkHttpClient();
    private static final Gson sGson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getDeclaringClass().equals(RealmObject.class);
                }

                @Override
                public boolean shouldSkipClass(Class<?> cls) {
                    return false;
                }
            })
            .create();

    static {
        sClient.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        sClient.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        sClient.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
    }

    @NonNull
    public static BarcodeService getBarcodeService() {
        return getRetrofit().create(BarcodeService.class);
    }

    @NonNull
    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(sGson))
                .client(sClient)
                .build();
    }
}
