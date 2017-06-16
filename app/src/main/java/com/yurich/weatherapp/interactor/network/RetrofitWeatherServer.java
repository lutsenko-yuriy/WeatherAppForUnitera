package com.yurich.weatherapp.interactor.network;

import com.yurich.weatherapp.interactor.entities.WeatherData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yurich on 16.06.17.
 */

public interface RetrofitWeatherServer {
    @GET("weather?")
    Observable<WeatherData> getWeather(
            @Query("q") String place,
            @Query("units") String units,
            @Query("appid") String appid);
}
