package com.yurich.weatherapp.interactor;

import com.yurich.weatherapp.interactor.entities.WeatherData;
import com.yurich.weatherapp.interactor.network.WeatherServer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yurich on 16.06.17.
 */

public class WeatherModel {

    private WeatherServer server = new WeatherServer();

    private WeatherData weatherData;

    private Observable<WeatherData> updateWeather(String city) {
        return server
                .getWeather(city)
                .doOnNext((weather) -> weatherData = weather)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<WeatherData> getCurrentWeather(String city) {
        if (weatherData == null || !weatherData.getName().equals(city)) {
            return updateWeather(city);
        }
        return Observable.just(weatherData);
    }

    public void setServer(String appid, String units) {
        server.setAppid(appid);
        server.setUnits(units);
    }
}
