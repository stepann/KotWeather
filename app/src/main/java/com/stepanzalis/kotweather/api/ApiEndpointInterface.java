package com.stepanzalis.kotweather.api;

import com.stepanzalis.kotweather.model.Forecast;
import com.stepanzalis.kotweather.model.WeatherAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndpointInterface {

    String API_KEY = "46135f58888b9aea7eb7a98a39e85c6e";

    @GET("weather?")
    Call<WeatherAPI> getWeatherCity(@Query("q") String city, @Query("units") String units, @Query("appid") String appid );

    @GET("weather?")
    Call<WeatherAPI> getWeatherCoord(@Query("lat") String lat, @Query("lan") String lon, @Query("units") String units, @Query("appid") String appid);

    @GET("forecast?")
    Call<Forecast> getLongWeatherCity(@Query("q") String city, @Query("units") String units, @Query("appid") String appid);

    @GET("forecast?")
    Call<Forecast> getLongWeatherCoord(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String units, @Query("appid") String appid);
}
