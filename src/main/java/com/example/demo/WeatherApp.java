package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;


public class WeatherApp extends Application{

    private static ConfigurableApplicationContext springContext;

    private WeatherService weatherService = new WeatherService();
    private HistoryManager historyManager = new HistoryManager();

    private Label temperatureLabel = new Label("Temperature: --");
    private Label humidityLabel = new Label("Humidity: --");
    private Label windLabel = new Label("Wind Speed: --");
    private Label conditionLabel = new Label("Conditions: --");
    private ImageView weatherIcon = new ImageView();

    private TextField cityInput = new TextField();
    private Button fetchButton = new Button("Get Weather");
    private ToggleButton unitToggle = new ToggleButton("Celsius / Fahrenheit");
    private ListView<String> historyList = new ListView<>();
    private boolean isCelsius = true;

    @Override
    public void start(Stage primaryStage) throws Exception {
        cityInput.setPromptText("Enter city name");
        fetchButton.setOnAction(e -> fetchWeather());
        unitToggle.setOnAction(e -> toggleUnits());

        VBox weatherBox = new VBox(10, temperatureLabel, humidityLabel, windLabel, conditionLabel, weatherIcon);
        weatherBox.setAlignment(Pos.CENTER);

        VBox historyBox = new VBox(new Label("Search History:"), historyList);
        historyBox.setPadding(new Insets(10));

        HBox inputBox = new HBox(10, cityInput, fetchButton, unitToggle);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(inputBox);
        root.setCenter(weatherBox);
        root.setRight(historyBox);
        root.setPadding(new Insets(20));

        updateBackground(root);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Weather Information App");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void fetchWeather() {
        String city = cityInput.getText().trim();
        if (city.isEmpty()) {
            showAlert("Input Error", "Please enter a city name.");
            return;
        }

        try {
            WeatherData data = weatherService.getWeather(city, isCelsius);

            // Update UI
            temperatureLabel.setText(String.format("Temperature: %.1f °%s", data.getTemperature(), isCelsius ? "C" : "F"));
            humidityLabel.setText("Humidity: " + data.getHumidity() + "%");
            windLabel.setText(String.format("Wind Speed: %.1f %s", data.getWindSpeed(), isCelsius ? "m/s" : "mph"));
            conditionLabel.setText("Conditions: " + data.getCondition());

            String iconUrl = "http://openweathermap.org/img/wn/" + data.getIconCode() + "@2x.png";
            weatherIcon.setImage(new Image(iconUrl));

            // Add to history
            historyManager.addSearch(city);
            updateHistoryList();

        } catch (Exception e) {
            showAlert("Error", "Failed to fetch weather data: " + e.getMessage());
        }
    }

    private void toggleUnits() {
        isCelsius = !isCelsius;
        fetchWeather(); // Refresh weather data in new units
    }

    private void updateHistoryList() {
        List<String> history = historyManager.getHistory();
        historyList.getItems().setAll(history);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateBackground(Pane pane) {
        int hour = java.time.LocalDateTime.now().getHour();
        if (hour >= 6 && hour < 18) {
            pane.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            pane.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void launchApp(ConfigurableApplicationContext context) {
        springContext = context;
        launch();
    }

}
