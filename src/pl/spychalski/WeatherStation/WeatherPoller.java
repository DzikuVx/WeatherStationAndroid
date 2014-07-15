package pl.spychalski.WeatherStation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class WeatherPoller {

    private Context context;

    public WeatherPoller(Context context) {
        this.context = context;
    }

    @SuppressLint("CommitPrefEdits")
    public String execute() throws NoNetworkConnection {

        if (!Utils.isNetworkAvailable(context)) {
            throw new NoNetworkConnection();
        }

        String response;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String sUrl = sharedPref.getString(SettingsActivity.API_URL_KEY, "");

        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(sUrl);
            HttpResponse httpResponse = httpclient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            response = EntityUtils.toString(httpEntity);

            //Save last response to shared storage
            SharedPreferences weatherData = context.getSharedPreferences(MainActivity.SHARED_PREFERENCE_NAME, 0);
            SharedPreferences.Editor editor = weatherData.edit();
            editor.putString(MainActivity.LAST_READOUT_KEY, response);

            //Put last readout time into storage
            editor.putLong(MainActivity.LAST_READOUT_TIMESTAMP_KEY, System.currentTimeMillis() / 1000);
            editor.commit();

            Log.d("Info", "Poller service executed successfully");

            return response;
        } catch (Exception ex) {
            Log.e("WeatherStation", "Exception", ex);
            return null;
        }
    }

}
