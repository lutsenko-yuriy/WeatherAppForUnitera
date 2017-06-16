package com.yurich.weatherapp;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yurich.weatherapp.view.entities.DisplayedWeatherData;

import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends LifecycleActivity {

    @BindView(R.id.city_field)
    EditText city;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        title.setVisibility(View.INVISIBLE);
        temperature.setVisibility(View.INVISIBLE);
        latitude.setVisibility(View.INVISIBLE);
        longitude.setVisibility(View.INVISIBLE);
        pressure.setVisibility(View.INVISIBLE);
        sunrise.setVisibility(View.INVISIBLE);
        sunset.setVisibility(View.INVISIBLE);

        setupModel();
    }

    private void setupModel() {
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        // Создание ключа OpenWeatherMap API
        // предоставляется проверяющему(-им)
        weatherViewModel.setupModel(getString(R.string.appid), getString(R.string.metrics));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWeather();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.city_search_button)
    public void buttonClicked() {
        loadWeather();
    }

    private void loadWeather() {
        if (!TextUtils.isEmpty(city.getText().toString())) {
            weatherViewModel.getWeather(city.getText().toString())
                    .subscribe(
                            this::updateView,
                            (error) -> Snackbar
                                    .make(
                                            findViewById(R.id.container),
                                            R.string.error_message,
                                            Snackbar.LENGTH_LONG
                                    ).show()
                    );
        }
    }

    private void updateView(DisplayedWeatherData weather) {
        title.setVisibility(View.VISIBLE);
        Locale locale = new Locale("", weather.country);
        title.setText(String.format("%s, %s", weather.city, locale.getDisplayCountry()));

        temperature.setVisibility(View.VISIBLE);
        temperature.setText(getString(R.string.temperature_unit, weather.temperature));

        latitude.setVisibility(View.VISIBLE);
        if (weather.latitude > 0) {
            latitude.setText(getString(R.string.to_north, weather.latitude));
        } else {
            latitude.setText(getString(R.string.to_south, weather.latitude));
        }

        longitude.setVisibility(View.VISIBLE);
        if (weather.longitude > 0) {
            longitude.setText(getString(R.string.to_east, weather.longitude));
        } else {
            longitude.setText(getString(R.string.to_west, weather.longitude));
        }

        pressure.setVisibility(View.VISIBLE);
        pressure.setText(getString(R.string.pressure_unit, weather.pressure));

        sunrise.setVisibility(View.VISIBLE);
        Date sunriseTime = new Date(weather.sunrise * 1000);
        String readableSunrise = String.format(
                locale,
                "%02d:%02d",
                sunriseTime.getHours(),
                sunriseTime.getMinutes()
        );
        sunrise.setText(readableSunrise);

        sunset.setVisibility(View.VISIBLE);
        Date sunsetTime = new Date(weather.sunset * 1000);
        String readableSunset = String.format(
                locale,
                "%02d:%02d",
                sunsetTime.getHours(),
                sunsetTime.getMinutes()
        );
        sunset.setText(readableSunset);
    }
}
