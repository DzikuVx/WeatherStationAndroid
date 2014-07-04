package pl.spychalski.WeatherStation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

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

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

        updateView();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }
}
