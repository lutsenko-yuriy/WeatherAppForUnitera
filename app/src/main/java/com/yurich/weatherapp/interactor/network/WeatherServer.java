package com.yurich.weatherapp.interactor.network;

import com.yurich.weatherapp.interactor.entities.WeatherData;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yurich on 16.06.17.
 */

public class WeatherServer {
    private RetrofitWeatherServer server;

    private String appid;
    private String units;

    public WeatherServer() {
        server = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .build()
                .create(RetrofitWeatherServer.class);
    }
    
    

    public Observable<WeatherData> getWeather(String place) {
        return server.getWeather(place, units, appid);
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
