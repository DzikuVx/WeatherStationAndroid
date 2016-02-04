package pl.spychalski.WeatherStation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class ForecastListAdapter extends BaseAdapter {

    final private Context ctx;
    final private ArrayList<DayForecastSimple> data;

    public ForecastListAdapter(Context ctx, ArrayList<DayForecastSimple> data) {
        this.data = data;
        this.ctx = ctx;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolderPattern {
        ImageView weatherIcon;
        TextView temperature;
        TextView maxTemperature;
        TextView minTemperature;
        TextView dayOfWeek;
        ImageButton imageButton;
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolderPattern viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.forecast_list, parent, false);

            viewHolder = new ViewHolderPattern();

            viewHolder.weatherIcon = (ImageView) convertView.findViewById(R.id.weatherIcon);
            viewHolder.dayOfWeek = (TextView) convertView.findViewById(R.id.dayOfWeek);
            viewHolder.temperature = (TextView) convertView.findViewById(R.id.temperature);
            viewHolder.maxTemperature = (TextView) convertView.findViewById(R.id.maxTemperature);
            viewHolder.minTemperature = (TextView) convertView.findViewById(R.id.minTemperature);
            viewHolder.imageButton = (ImageButton) convertView.findViewById(R.id.imageButton);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderPattern) convertView.getTag();
        }

        final DayForecastSimple current = data.get(position);

        viewHolder.maxTemperature.setText(Integer.toString(current.getIntTempMax()) + "\u00B0C");
        viewHolder.minTemperature.setText(Integer.toString(current.getIntTempMin()) + "\u00B0C");
        viewHolder.temperature.setText(Integer.toString(current.getIntTempDay()) + "\u00B0C");
        viewHolder.weatherIcon.setImageResource(current.getInvertedWeatherIconResource(ctx));
        viewHolder.dayOfWeek.setText(current.getTranslatedDayOfWeek(ctx));

        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ForecastActivity.class);

                Bundle data = new Bundle();
                data.putSerializable("forecastData", current);
                intent.putExtras(data);

                ctx.startActivity(intent);
            }
        });

        return convertView;
    }

}
