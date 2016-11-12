package com.stepanzalis.kotweather.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FactoryAPI {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static ApiEndpointInterface REST_CLIENT;

    public FactoryAPI() {}

    public static ApiEndpointInterface get() {
        return REST_CLIENT;
    }

    static { setupRestClient(); }

    private static void setupRestClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        REST_CLIENT = retrofit.create(ApiEndpointInterface.class);
    }
}
