package pl.spychalski.WeatherStation;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastActivity extends MyActionBarActivity {

    DayForecastSimple weatherData;

    TextView date;
    TextView dayOfWeek;
    TextView dayTemperature;
    TextView nightTemperature;
    TextView pressure;
    TextView humidity;
    TextView clouds;
    ImageView weatherIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.menuFile = R.menu.forecast;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast);

        weatherData = (DayForecastSimple) getIntent().getExtras().getSerializable("forecastData");

        date = (TextView) findViewById(R.id.date);
        dayOfWeek = (TextView) findViewById(R.id.dayOfWeek);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        dayTemperature = (TextView) findViewById(R.id.dayTemperature);
        nightTemperature = (TextView) findViewById(R.id.nightTemperature);
        pressure = (TextView) findViewById(R.id.pressure);
        humidity = (TextView) findViewById(R.id.humidity);
        clouds = (TextView) findViewById(R.id.clouds);

        date.setText(weatherData.getDate());
        dayOfWeek.setText(weatherData.getTranslatedDayOfWeek(this));
        weatherIcon.setImageResource(weatherData.getWeatherIconResource(this));
        dayTemperature.setText(Integer.toString(weatherData.getIntTempDay()) + "\u00B0C");
        nightTemperature.setText(Integer.toString(weatherData.getIntTempNight()) + "\u00B0C");
        pressure.setText(Integer.toString(weatherData.getPressureInt()) + "hPa");
        humidity.setText(weatherData.getHumidity() + "%");
        clouds.setText(weatherData.getClouds() + "%");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
