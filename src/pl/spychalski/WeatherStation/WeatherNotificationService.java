package pl.spychalski.WeatherStation;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

public class WeatherNotificationService extends IntentService {

    public WeatherNotificationService () {
        super("WeatherNotificationService");
    }

    protected void onHandleIntent(Intent intent) {

        new WeatherNotification(this).pushDailyNotification();

        Log.d("Info", "Notification service executed");
    }

    /**
     *
     * Register alarm service for morning weather notification
     *
     * @param context context
     */
    public static void createAlarm(Context context) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        Boolean bDoNotifications = sharedPref.getBoolean(SettingsActivity.NOTIFICATIONS, true);

        if (bDoNotifications) {
            AlarmManager alarmManager;
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

            if (currentHour > 7) {
                calendar.setTimeInMillis(System.currentTimeMillis() + AlarmManager.INTERVAL_DAY);
            }

            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);

            Intent notificationServiceIntent = new Intent(context, WeatherNotificationService.class);
            PendingIntent notificationServicePendingIntent = PendingIntent.getService(context, 0, notificationServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (notificationServicePendingIntent != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, notificationServicePendingIntent);
                Log.d("Notif", "Cotification alarm created");
            }

        }

    }

}
