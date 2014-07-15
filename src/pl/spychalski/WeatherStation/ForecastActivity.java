package pl.spychalski.WeatherStation;

import android.os.Bundle;

public class ForecastActivity extends MyActionBarActivity {

    DayForecastSimple data;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.menuFile = R.menu.main;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast);

        data = (DayForecastSimple) getIntent().getExtras().getSerializable("forecastData");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
