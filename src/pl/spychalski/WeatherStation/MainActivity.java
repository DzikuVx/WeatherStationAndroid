package pl.spychalski.WeatherStation;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class MainActivity extends MyActionBarActivity implements View.OnClickListener {

    public static final String SHARED_PREFERENCE_NAME = "weatherData";
    public static final String LAST_READOUT_KEY = "lastReadout";
    public static final String LAST_READOUT_TIMESTAMP_KEY = "lastReadoutTimestamp";

    private static int getStringIdentifier(Context context, String name) {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }

    private void updateView() {

        SharedPreferences weatherData = getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        String sLastReadout = weatherData.getString(LAST_READOUT_KEY, null);

        ArrayList<ReadoutValue> values = new ArrayList<ReadoutValue>();

        Long lLastReadoutTimestamp = weatherData.getLong(LAST_READOUT_TIMESTAMP_KEY, 0);
        Long lCurrentTimestamp = System.currentTimeMillis() / 1000;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Long lDiff = Long.parseLong(sharedPref.getString(SettingsActivity.REQUEST_INTERVAL, "60"), 10) * 60;

        if (sLastReadout != null) {
            try {
                JSONObject jData = new JSONObject(sLastReadout);

                currentTemperature.setText(jData.getString("Temperature"));

                Iterator<?> keys = jData.keys();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    ReadoutValue entry = new ReadoutValue();

                    if (key.equals("Humidity")) {
                        entry.setValue(jData.getString(key));
                        entry.setUnit("%");
                    } else if (key.equals("Pressure")) {
                        entry.setValue(Integer.toString(Math.round(Float.parseFloat(jData.getString(key)))));
                        entry.setUnit("hPa");
                    } else if (key.equals("WindSpeed")) {
                        entry.setValue(jData.getString(key));
                        entry.setUnit("m/s");
                    } else if (key.equals("WindDirection")) {
                        entry.setValue(Integer.toString(Math.round(Float.parseFloat(jData.getString(key)))));
                        entry.setUnit("\u00B0");
                    } else if (key.equals("TempMax")) {
                        entry.setValue(jData.getString(key));
                        entry.setUnit("\u00B0C");
                    } else if (key.equals("TempMin")) {
                        entry.setValue(jData.getString(key));
                        entry.setUnit("\u00B0C");
                    } else if (key.equals("Clouds")) {
                        entry.setValue(jData.getString(key));
                        entry.setUnit("%");
                    } else if (key.equals("Rain") || key.equals("Snow")) {

                        if (Float.parseFloat(jData.getString(key)) == 0) {
                            continue;
                        }

                        entry.setValue(jData.getString(key));
                        entry.setUnit("mm/h");
                    } else {
                        continue;
                    }

                    entry.setName(getString(getStringIdentifier(this, "readoutName_" + key)));

                    values.add(entry);
                }

                ReadoutValuesListAdapter adapter = new ReadoutValuesListAdapter(this, values);
                dataList.setAdapter(adapter);

                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //If not data, or data is old, start new request for data
        if (sLastReadout == null || (lCurrentTimestamp - lLastReadoutTimestamp) > lDiff) {
            GetDataThread thread = new GetDataThread();
            thread.execute();
        }

    }

    TextView currentTemperature;
    ListView dataList;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.displayActionBarBack = false;
        super.menuFile = R.menu.main;

        //Add spinner dialog
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

        currentTemperature = (TextView) findViewById(R.id.currentTemperature);
        dataList = (ListView) findViewById(R.id.dataList);

        updateView();

        /*
         * Register alarm service to periodically get data
         */
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Long lDiff = Long.parseLong(sharedPref.getString(SettingsActivity.REQUEST_INTERVAL, "60"), 10) * 60;

        Intent startServiceIntent = new Intent(this, WeatherPollerService.class);
        PendingIntent startWebServicePendingIntent = PendingIntent.getService(this, 0, startServiceIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), lDiff * 60 * 1000, startWebServicePendingIntent);

        /*
         * Register alarm service for morning weather notification
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        Intent notificationServiceIntent = new Intent(this, WeatherNotificationService.class);
        PendingIntent notificationServicePendingIntent = PendingIntent.getService(this, 0, notificationServiceIntent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, notificationServicePendingIntent);
    }

    public void onClick(View view) {

    }

    private void getData() {
        GetDataThread thread = new GetDataThread();
        thread.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_refresh:
                getData();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class GetDataThread extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            setProgressBarIndeterminateVisibility(false);

            Context context = getApplicationContext();

            if (result != null && result.length() > 5 && context != null) {
                Toast.makeText(context, getString(R.string.data_fetched), Toast.LENGTH_LONG).show();
                updateView();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            WeatherPoller poller = new WeatherPoller(MainActivity.this);

            String response = poller.execute();

            if (response == null) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_occured), Toast.LENGTH_LONG).show();
            }

            return response;
        }

    }

}
