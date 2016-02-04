package pl.spychalski.WeatherStation;

import android.os.Bundle;

public class AboutActivity extends MyActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.displayActionMenu = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }
}