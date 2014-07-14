package pl.spychalski.WeatherStation;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

abstract public class MyActionBarActivity extends Activity {

    /**
     * To display back button on action bar
     */
    protected Boolean displayActionBarBack = true;

    protected Boolean displayActionMenu = true;

    protected int menuFile = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (displayActionMenu) {
            if (menuFile == 0) {
                menuFile = R.menu.fallback;
            }
            getMenuInflater().inflate(menuFile, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null && this.displayActionBarBack) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
