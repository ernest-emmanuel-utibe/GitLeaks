package com.example.demo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class WeatherService {
    private static final String API_KEY = "896b3159ff28696726449f8674927421";

    private static final String AWS_SECRET_ACCESS_KEY = "";
    private static final String AWS_ACCOUNT_NUMBER = "";
    private static final String API_URL_TEMPLATE = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=%s";
    // // "https://api.openweathermap.org/data/2.5/weather?q=";

    public WeatherData getWeather(String city, boolean celsius) throws Exception {
        String units = celsius ? "metric" : "imperial";
        String apiUrl = String.format(API_URL_TEMPLATE, city, API_KEY, units);

        String jsonResponse = sendGetRequest(apiUrl);
        JSONObject json = new JSONObject(jsonResponse);

        if (json.has("cod") && json.getInt("cod") != 200) {
            throw new Exception(json.getString("message"));
        }

        double temp = json.getJSONObject("main").getDouble("temp");
        int humidity = json.getJSONObject("main").getInt("humidity");
        double windSpeed = json.getJSONObject("wind").getDouble("speed");
        String condition = json.getJSONArray("weather").getJSONObject(0).getString("main");
        String iconCode = json.getJSONArray("weather").getJSONObject(0).getString("icon");

        return new WeatherData(temp, humidity, windSpeed, condition, iconCode);
    }

    private String sendGetRequest(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return response.toString();
        }
    }
}
