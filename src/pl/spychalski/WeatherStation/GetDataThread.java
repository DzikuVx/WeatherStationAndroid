package pl.spychalski.WeatherStation;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class GetDataThread extends AsyncTask<String, Void, String> {

    private MainActivity mActivity;

    public GetDataThread (MainActivity activity){
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mActivity.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mActivity.setProgressBarIndeterminateVisibility(false);

        if (result != null && result.length() > 5 && mActivity != null) {
            Toast.makeText(mActivity, mActivity.getString(R.string.data_fetched), Toast.LENGTH_SHORT).show();
            mActivity.updateView();
        } else {
            Toast.makeText(mActivity, mActivity.getString(R.string.error_data_not_fetched), Toast.LENGTH_LONG).show();
        }

        mActivity.swipeLayout.setRefreshing(false);

    }

    @Override
    protected String doInBackground(String... params) {

        String response = null;

        WeatherPoller poller = new WeatherPoller(mActivity);

        try {
            response = poller.execute();
        } catch (NoNetworkConnection e) {
            Log.i("Network", "No network connection");
        }

        return response;
    }

}
