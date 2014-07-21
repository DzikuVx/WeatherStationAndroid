package pl.spychalski.WeatherStation;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.json.JSONObject;

public class WeatherNotification {

    private Context context;

    public WeatherNotification(Context context) {
        this.context = context;
    }

    /**
     * Publish daily weather notification
     */
    public void pushDailyNotification() {

        SharedPreferences weatherData = context.getSharedPreferences(MainActivity.SHARED_PREFERENCE_NAME, 0);
        String sLastReadout = weatherData.getString(MainActivity.LAST_READOUT_KEY, null);

        if (sLastReadout != null) {

            try {

                NotificationCompat.Builder notification = new NotificationCompat.Builder(context);

                JSONObject jData = new JSONObject(sLastReadout);

                notification.setContentTitle(context.getString(R.string.notification_now) + " " + jData.getString("Temperature") + "\u00B0C " + context.getString(R.string.notification_junction) + " " + jData.getString("Pressure") + "hPa");
                notification.setContentText(context.getString(R.string.notification_text));
                notification.setSmallIcon(Utils.getImageIdentifier(context, "icon_" + jData.getString("WeatherIcon")));

                Intent intent = new Intent(context, MainActivity.class);

                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                notification.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, notification.build());
            } catch (Exception e) {
                Log.e("Error", "Failed to format notification", e);
            }
        }
    }

}
