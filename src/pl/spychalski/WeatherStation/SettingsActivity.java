package pl.spychalski.WeatherStation;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

public class SettingsActivity extends MyActionBarActivity {

    public static final String API_URL_KEY = "pref_api_url";
    public static final String REQUEST_INTERVAL = "pref_update_interval";
    public static final String AUTO_UPDATE = "pref_auto_update";
    public static final String NOTIFICATIONS = "pref_notifications";


    public void onCreate(Bundle savedInstanceState) {

        super.displayActionMenu = false;

        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}