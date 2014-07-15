package pl.spychalski.WeatherStation;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class WeatherPollerService extends IntentService {

    public WeatherPollerService () {
        super("WeatherPollerService");
    }

    protected void onHandleIntent(Intent intent) {
        WeatherPoller poller = new WeatherPoller(this);
        poller.execute();
    }

}
