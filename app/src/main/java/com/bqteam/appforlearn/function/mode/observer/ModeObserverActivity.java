package com.bqteam.appforlearn.function.mode.observer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bqteam.appforlearn.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author charles
 */
public class ModeObserverActivity extends AppCompatActivity {

    @BindView(R.id.weatherData_basic_tv)
    TextView basicTv;
    @BindView(R.id.weatherData_statistic_tv)
    TextView statisticTv;
    @BindView(R.id.weatherData_weather_tv)
    TextView weatherTv;

    private WeatherData weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_observer);
        ButterKnife.bind(this);

        findViewById(R.id.weatherData_update_btn).setOnClickListener(v -> updateWeatherData());

        weatherData = new WeatherData();
        BasicDisplay basicDisplay = new BasicDisplay(weatherData);
        StatisticDisplay statisticDisplay = new StatisticDisplay(weatherData);
        WeatherDisplay weatherDisplay = new WeatherDisplay(weatherData);
    }

    private void updateWeatherData() {
        weatherData.setData((float)(Math.random() * 100), (float)(Math.random() * 100), (float)(Math.random() * 100));
    }

    public class BasicDisplay implements Observer, DisplayElement {
        private float temperature;
        private float humidity;
        private float pressure;
        private WeatherData weatherData;

        public BasicDisplay(WeatherData weatherData) {
            this.weatherData = weatherData;
            weatherData.registerObserver(this);
        }

        @Override
        public void display() {
            String s = "天气数据\n温度:" + temperature + ", 湿度:" + humidity + ", 气压:" + pressure;
            basicTv.setText(s);
        }

        @Override
        public void update(float temperature, float humidity, float pressure) {
            this.temperature = temperature;
            this.pressure = pressure;
            this.humidity = humidity;
            display();
        }
    }

    public class StatisticDisplay implements Observer, DisplayElement {
        private WeatherData weatherData;

        private List<Float> temperatureHistory = new ArrayList<>();

        public StatisticDisplay(WeatherData weatherData) {
            this.weatherData = weatherData;
            weatherData.registerObserver(this);
        }

        @Override
        public void display() {
            float maxTemperature = 0;
            float minTemperature = 100;
            float averageTemperature = 0;

            float empty = 0;
            for (float temperature: temperatureHistory) {
                if (maxTemperature < temperature) {
                    maxTemperature = temperature;
                }
                if (minTemperature > temperature) {
                    minTemperature = temperature;
                }
                empty += temperature;
            }
            averageTemperature = empty / temperatureHistory.size();

            String s = "温度统计\n最高温度:" + maxTemperature + ", 最低温度:" + minTemperature + ", 平均温度:"
                    + averageTemperature;
            statisticTv.setText(s);
        }

        @Override
        public void update(float temperature, float humidity, float pressure) {
            temperatureHistory.add(temperature);
            display();
        }
    }

    public class WeatherDisplay implements Observer, DisplayElement {
        private float pressure;
        private WeatherData weatherData;

        public WeatherDisplay(WeatherData weatherData) {
            this.weatherData = weatherData;
            weatherData.registerObserver(this);
        }

        @Override
        public void display() {
            String s = pressure > 50 ? "天气\n晴" : "天气\n阴";
            weatherTv.setText(s);
        }

        @Override
        public void update(float temperature, float humidity, float pressure) {
            this.pressure = pressure;
            display();
        }
    }
}
