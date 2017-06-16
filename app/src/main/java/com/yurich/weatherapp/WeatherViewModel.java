package com.yurich.weatherapp;

import android.arch.lifecycle.ViewModel;

import com.yurich.weatherapp.interactor.WeatherModel;
import com.yurich.weatherapp.interactor.entities.WeatherData;
import com.yurich.weatherapp.view.entities.DisplayedWeatherData;

import io.reactivex.Observable;

/**
 * Created by yurich on 16.06.17.
 */

class WeatherViewModel extends ViewModel {

    private WeatherModel weatherModel = new WeatherModel();

    void setupModel(String appid, String units) {
        weatherModel.setServer(appid, units);
    }

    Observable<DisplayedWeatherData> getWeather(String city) {
        Observable<WeatherData> weatherData;
        weatherData = weatherModel.getCurrentWeather(city);

        return weatherData
                .map(DisplayedWeatherData::new);
    }

}
