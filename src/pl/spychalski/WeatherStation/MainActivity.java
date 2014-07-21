package pl.spychalski.WeatherStation;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends MyActionBarActivity implements View.OnClickListener {

    public static final String SHARED_PREFERENCE_NAME = "weatherData";
    public static final String LAST_READOUT_KEY = "lastReadout";
    public static final String LAST_READOUT_TIMESTAMP_KEY = "lastReadoutTimestamp";

    public static final String DEBUG_TAG = "Debug";

    private void updateView() {

        SharedPreferences weatherData = getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        String sLastReadout = weatherData.getString(LAST_READOUT_KEY, null);

        Long lLastReadoutTimestamp = weatherData.getLong(LAST_READOUT_TIMESTAMP_KEY, 0);
        Long lCurrentTimestamp = System.currentTimeMillis() / 1000;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Long lDiff = Long.parseLong(sharedPref.getString(SettingsActivity.REQUEST_INTERVAL, "60"), 10) * 60;

        if (sLastReadout != null) {
            try {
                JSONObject jData = new JSONObject(sLastReadout);

                currentTemperature.setText(jData.getString("Temperature") + "\u00B0C");
                maxTemperature.setText(jData.getString("TempMax") + "\u00B0C");
                minTemperature.setText(jData.getString("TempMin") + "\u00B0C");
                currentWeatherIcon.setImageResource(Utils.getImageIdentifier(this, "icon_" + jData.getString("WeatherIcon")));
                currentHumidity.setText(jData.getString("Humidity") + "%");
                currentPressure.setText(jData.getString("Pressure") + "hPa");

                windSpeed.setText(jData.getString("WindSpeed") + "m/s");
                windDirection.setText(Integer.toString(Math.round(Float.parseFloat(jData.getString("WindDirection")))) + "°");
                cloudCoverage.setText(jData.getString("Clouds") + "%");

                Float value = Float.parseFloat(jData.getString("Rain"));
                if (value > 0) {
                    rainValue.setText(Integer.toString(Math.round(value)) + "mm/h");
                    rainValue.setVisibility(TextView.VISIBLE);
                    rainHeader.setVisibility(TextView.VISIBLE);
                } else {
                    rainValue.setVisibility(TextView.GONE);
                    rainHeader.setVisibility(TextView.GONE);
                }

                value = Float.parseFloat(jData.getString("Snow"));
                if (value > 0) {
                    snowValue.setText(Integer.toString(Math.round(value)) + "mm/h");
                    snowValue.setVisibility(TextView.VISIBLE);
                    snowHeader.setVisibility(TextView.VISIBLE);
                } else {
                    snowValue.setVisibility(TextView.GONE);
                    snowHeader.setVisibility(TextView.GONE);
                }

                JSONArray jForecastData = jData.getJSONArray("Forecast");
                JSONObject jForecast;

                ArrayList<DayForecastSimple> forecastSimples = new ArrayList<DayForecastSimple>();

                for (int i = 0; i < jForecastData.length(); i++) {
                    jForecast = (JSONObject) jForecastData.get(i);

                    DayForecastSimple day = new DayForecastSimple();

                    day.setDate(jForecast.getString("Date"));
                    day.setDayOfWeek(jForecast.getString("WeekDay"));
                    day.setTempMax(jForecast.getString("TempMax"));
                    day.setTempMin(jForecast.getString("TempMin"));
                    day.setWeatherIcon(jForecast.getString("WeatherIcon"));
                    day.setTempDay(jForecast.getString("TempDay"));
                    day.setTempNight(jForecast.getString("TempNight"));

                    day.setWeatherCode(jForecast.getString("WeatherCode"));
                    day.setClouds(jForecast.getString("Clouds"));
                    day.setSnow(jForecast.getString("Snow"));
                    day.setRain(jForecast.getString("Rain"));
                    day.setPressure(jForecast.getString("Pressure"));
                    day.setHumidity(jForecast.getString("Humidity"));
                    day.setWindSpeed(jForecast.getString("WindSpeed"));
                    day.setWindDirection(jForecast.getString("WindDirection"));

                    forecastSimples.add(day);
                }

                ForecastListAdapter adapter = new ForecastListAdapter(this, forecastSimples);
                dataList.setAdapter(adapter);
                adapter.notifyDataSetChanged();

//                Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();

//                Iterator<?> keys = jForecastData.keys();

//                while (keys.hasNext()) {
//                    String key = (String) keys.next();

//                    jForecast = (JSONObject) jForecastData.getJSONObject(key);
//
//                }

                /*
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

                */

            } catch (JSONException e) {
                Log.e("Error", "failed to build", e);
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
    TextView maxTemperature;
    TextView minTemperature;
    ImageView currentWeatherIcon;
    TextView currentHumidity;
    TextView currentPressure;
    TextView windSpeed;
    TextView windDirection;
    TextView cloudCoverage;

    TextView rainValue;
    TextView rainHeader;
    TextView snowValue;
    TextView snowHeader;

    ListView dataList;

    SwipeRefreshLayout swipeLayout;

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
        maxTemperature = (TextView) findViewById(R.id.maxTemperature);
        minTemperature = (TextView) findViewById(R.id.minTemperature);
        currentWeatherIcon = (ImageView) findViewById(R.id.currentWeatherIcon);
        currentHumidity = (TextView) findViewById(R.id.currentHumidity);
        currentPressure = (TextView) findViewById(R.id.currentPressure);

        windSpeed = (TextView) findViewById(R.id.windSpeed);
        windDirection = (TextView) findViewById(R.id.windDirection);
        cloudCoverage = (TextView) findViewById(R.id.cloudCoverage);

        rainHeader = (TextView) findViewById(R.id.rainHeader);
        rainValue = (TextView) findViewById(R.id.rainValue);

        snowHeader = (TextView) findViewById(R.id.snowHeader);
        snowValue = (TextView) findViewById(R.id.snowValue);

        dataList = (ListView) findViewById(R.id.dataList);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeLayout.setColorSchemeResources(R.color.accent600, R.color.accent400, R.color.accent200, R.color.accent100);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        /**
         * Build manual scroll listener to enable SwipeRefreshLayout and ListView
         */
        dataList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (dataList == null || dataList.getChildCount() == 0) ?
                                0 : dataList.getChildAt(0).getTop();
                swipeLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        updateView();

        /*
         * Register alarm service to periodically get data
         */
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Long lDiff = Long.parseLong(sharedPref.getString(SettingsActivity.REQUEST_INTERVAL, "60"), 10) * 60;

        Intent startServiceIntent = new Intent(this, WeatherPollerService.class);
        PendingIntent startWebServicePendingIntent = PendingIntent.getService(this, 0, startServiceIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(startWebServicePendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), lDiff * 60 * 1000, startWebServicePendingIntent);

        /*
         * Register alarm service for morning weather notification
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + 86400000);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        Intent notificationServiceIntent = new Intent(this, WeatherNotificationService.class);
        PendingIntent notificationServicePendingIntent = PendingIntent.getService(this, 0, notificationServiceIntent, 0);

        alarmManager.cancel(notificationServicePendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 28800000, notificationServicePendingIntent);
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

            swipeLayout.setRefreshing(false);

        }

        @Override
        protected String doInBackground(String... params) {

            String response = null;

            Context context = getApplicationContext();
            WeatherPoller poller = new WeatherPoller(MainActivity.this);

            try {

                response = poller.execute();

                if (context != null && response == null) {
                    Toast.makeText(context, getString(R.string.error_occured), Toast.LENGTH_LONG).show();
                }

            } catch (NoNetworkConnection e) {
                Log.i("Network", "No network connection");
            }

            return response;
        }

    }

}
