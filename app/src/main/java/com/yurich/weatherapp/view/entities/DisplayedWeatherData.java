package com.yurich.weatherapp.view.entities;

import com.yurich.weatherapp.interactor.entities.WeatherData;

/**
 * Created by yurich on 16.06.17.
 */

public class DisplayedWeatherData {
    public String city;
    public String country;

    public double temperature;
    public double pressure;

    public double latitude;
    public double longitude;

    // Время по Гринвичу
    public long sunrise;
    public long sunset;

    public DisplayedWeatherData(WeatherData weatherData) {
        city = weatherData.getName();
        country = weatherData.getSys().getCountry();

        temperature = weatherData.getMain().getTemp();
        pressure = weatherData.getMain().getPressure() * 3 / 4.0;

        latitude = weatherData.getCoord().getLat();
        longitude = weatherData.getCoord().getLon();

        sunrise = weatherData.getSys().getSunrise();
        sunset = weatherData.getSys().getSunset();
    }
}
