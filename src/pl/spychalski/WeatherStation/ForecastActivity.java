package pl.spychalski.WeatherStation;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastActivity extends MyActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.menuFile = R.menu.forecast;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast);

        DayForecastSimple weatherData = (DayForecastSimple) getIntent().getExtras().getSerializable("forecastData");

        TextView date = (TextView) findViewById(R.id.date);
        TextView dayOfWeek = (TextView) findViewById(R.id.dayOfWeek);
        ImageView weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        TextView dayTemperature = (TextView) findViewById(R.id.dayTemperature);
        TextView nightTemperature = (TextView) findViewById(R.id.nightTemperature);
        TextView pressure = (TextView) findViewById(R.id.pressure);
        TextView humidity = (TextView) findViewById(R.id.humidity);
        TextView clouds = (TextView) findViewById(R.id.clouds);
        TextView rain = (TextView) findViewById(R.id.rain);
        TextView rainHeader = (TextView) findViewById(R.id.rainHeader);
        TextView snow = (TextView) findViewById(R.id.snow);
        TextView snowHeader = (TextView) findViewById(R.id.snowHeader);
        TextView windDirection = (TextView) findViewById(R.id.windDirection);
        TextView windSpeed = (TextView) findViewById(R.id.windSpeed);

        date.setText(weatherData.getDate());
        dayOfWeek.setText(weatherData.getTranslatedDayOfWeek(this));
        weatherIcon.setImageResource(weatherData.getWeatherIconResource(this));
        dayTemperature.setText(Integer.toString(weatherData.getIntTempDay()) + "\u00B0C");
        nightTemperature.setText(Integer.toString(weatherData.getIntTempNight()) + "\u00B0C");
        pressure.setText(Integer.toString(weatherData.getPressureInt()) + "hPa");
        humidity.setText(weatherData.getHumidity() + "%");
        clouds.setText(weatherData.getClouds() + "%");
        rain.setText(weatherData.getRain() + "mm/h");
        snow.setText(weatherData.getSnow() + "mm/h");
        windDirection.setText(weatherData.getWindDirection() + "°");
        windSpeed.setText(weatherData.getWindSpeed() + "m/s");

        if (Float.parseFloat(weatherData.getRain()) > 0) {
            rain.setVisibility(View.VISIBLE);
            rainHeader.setVisibility(View.VISIBLE);
        } else {
            rain.setVisibility(View.GONE);
            rainHeader.setVisibility(View.GONE);
        }

        if (Float.parseFloat(weatherData.getSnow()) > 0) {
            snow.setVisibility(View.VISIBLE);
            snowHeader.setVisibility(View.VISIBLE);
        } else {
            snow.setVisibility(View.GONE);
            snowHeader.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
