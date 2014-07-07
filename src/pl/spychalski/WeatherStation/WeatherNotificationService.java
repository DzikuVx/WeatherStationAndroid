package pl.spychalski.WeatherStation;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class WeatherNotificationService extends IntentService {

    public WeatherNotificationService () {
        super("WeatherNotificationService");
    }

    protected void onHandleIntent(Intent intent) {

        new WeatherNotification(this).pushDailyNotification();

        Log.d("Info", "Notification service executed");
    }

}
