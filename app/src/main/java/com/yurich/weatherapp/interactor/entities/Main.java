
package com.yurich.weatherapp.interactor.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    @Expose
    private double temp;
    @SerializedName("pressure")
    @Expose
    private long pressure;

    public double getTemp() {
        return temp;
    }

    public long getPressure() {
        return pressure;
    }

}
