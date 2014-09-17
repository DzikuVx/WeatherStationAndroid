package pl.spychalski.WeatherStation;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class WeatherPollerService extends IntentService {

    public WeatherPollerService () {
        super("WeatherPollerService");
    }

    protected void onHandleIntent(Intent intent) {
        WeatherPoller poller = new WeatherPoller(this);
        try {
            poller.execute();
        } catch (NoNetworkConnection e) {
            Log.e("Network", "No network connection");
        }
        Log.i("Poller", "WeatherPollerService executed");
    }

    public static void createAlarm(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Long lDiff = Long.parseLong(sharedPref.getString(SettingsActivity.REQUEST_INTERVAL, "60"), 10) * 60;
        Boolean bDoPoller = sharedPref.getBoolean(SettingsActivity.AUTO_UPDATE, true);

        if (bDoPoller) {
            AlarmManager alarmManager;
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent startServiceIntent = new Intent(context, WeatherPollerService.class);
            PendingIntent startWebServicePendingIntent = PendingIntent.getService(context, 0, startServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (startWebServicePendingIntent != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (lDiff * 1000), lDiff * 1000, startWebServicePendingIntent);
            }
        }

    }

}
