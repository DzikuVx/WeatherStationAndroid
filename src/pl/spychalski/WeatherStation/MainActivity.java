package pl.spychalski.WeatherStation;

import android.os.Bundle;

public class MainActivity extends MyActionBarActivity {

    private void updateView() {

    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.displayActionBarBack = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);



        updateView();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }
}
