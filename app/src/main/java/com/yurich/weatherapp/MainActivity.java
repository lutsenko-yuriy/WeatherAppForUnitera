package com.yurich.weatherapp;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yurich.weatherapp.view.entities.DisplayedWeatherData;

import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends LifecycleActivity {

    public static final String DEFAULT_CITY_NAME = "";

    @BindView(R.id.city_field)
    SearchView cityName;

    @BindView(R.id.city_information)
    TextView title;

    @BindView(R.id.temperature)
    TextView temperature;

    @BindView(R.id.latitude)
    TextView latitude;
    @BindView(R.id.longitude)
    TextView longitude;

    @BindView(R.id.pressure)
    TextView pressure;

    @BindView(R.id.sunrise)
    TextView sunrise;
    @BindView(R.id.sunset)
    TextView sunset;

    WeatherViewModel weatherViewModel;

    public static final String WEATHER_KEY = "WEATHER_KEY";
    DisplayedWeatherData weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        cityName.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        loadWeather(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );

        setupModel();
        if (savedInstanceState == null) {
            initializeViews();
            loadWeather(DEFAULT_CITY_NAME);
        } else {
            weatherData = (DisplayedWeatherData) savedInstanceState.getSerializable(WEATHER_KEY);
            if (weatherData != null) {
                updateView(weatherData);
            }
        }
    }

    private void initializeViews() {
        title.setAlpha(0f);
        temperature.setAlpha(0f);
        latitude.setAlpha(0f);
        longitude.setAlpha(0f);
        pressure.setAlpha(0f);
        sunrise.setAlpha(0f);
        sunset.setAlpha(0f);
    }

    private void setupModel() {
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        // Создание ключа OpenWeatherMap API
        // предоставляется проверяющему(-им)
        weatherViewModel.setupModel(getString(R.string.appid), getString(R.string.metrics));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (weatherData != null) {
            outState.putSerializable(WEATHER_KEY, weatherData);
        }
    }

    private void loadWeather(String cityName) {
        if (!TextUtils.isEmpty(cityName)) {
            weatherViewModel.getWeather(cityName)
                    .doOnNext(displayedWeatherData -> {
                        weatherData = displayedWeatherData;
                    })
                    .subscribe(
                            this::updateView,
                            (error) -> Snackbar
                                    .make(
                                            findViewById(R.id.container),
                                            R.string.unable_to_load,
                                            Snackbar.LENGTH_LONG
                                    ).show()
                    );
        }
    }

    private void updateView(DisplayedWeatherData weather) {
        displayView(title);
        Locale locale = new Locale("", weather.country);
        title.setText(String.format("%s, %s", weather.city, locale.getDisplayCountry()));

        displayView(temperature);
        temperature.setText(getString(R.string.temperature_unit, weather.temperature));

        displayView(latitude);
        if (weather.latitude > 0) {
            latitude.setText(getString(R.string.to_north, weather.latitude));
        } else {
            latitude.setText(getString(R.string.to_south, -weather.latitude));
        }

        displayView(longitude);
        if (weather.longitude > 0) {
            longitude.setText(getString(R.string.to_east, weather.longitude));
        } else {
            longitude.setText(getString(R.string.to_west, -weather.longitude));
        }

        displayView(pressure);
        pressure.setText(getString(R.string.pressure_unit, weather.pressure));

        displayView(sunrise);
        Date sunriseTime = new Date(weather.sunrise * 1000);
        String readableSunrise = String.format(
                locale,
                "%02d:%02d",
                sunriseTime.getHours(),
                sunriseTime.getMinutes()
        );
        sunrise.setText(readableSunrise);

        displayView(sunset);
        Date sunsetTime = new Date(weather.sunset * 1000);
        String readableSunset = String.format(
                locale,
                "%02d:%02d",
                sunsetTime.getHours(),
                sunsetTime.getMinutes()
        );
        sunset.setText(readableSunset);
    }

    private void displayView(View view) {
        view.animate()
            .alpha(1f)
            .setDuration(500)
            .start();
    }
}
