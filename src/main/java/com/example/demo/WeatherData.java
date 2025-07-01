package com.example.demo;

public class WeatherData {
    private double temperature;
    private int humidity;
    private double windSpeed;
    private String condition;
    private String iconCode;

    public WeatherData(double temperature, int humidity, double windSpeed, String condition, String iconCode) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.condition = condition;
        this.iconCode = iconCode;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getCondition() {
        return condition;
    }

    public String getIconCode() {
        return iconCode;
    }
}
