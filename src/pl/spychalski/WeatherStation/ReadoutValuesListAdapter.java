package pl.spychalski.WeatherStation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ReadoutValuesListAdapter extends BaseAdapter {

    final private Context ctx;
    final private ArrayList<ReadoutValue> data;

    public ReadoutValuesListAdapter(Context ctx, ArrayList<ReadoutValue> data) {
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
        TextView name;
        TextView value;
        TextView unit;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderPattern viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.readout_value_list_layout, parent, false);

            viewHolder = new ViewHolderPattern();

            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.value = (TextView) convertView.findViewById(R.id.value);
            viewHolder.unit = (TextView) convertView.findViewById(R.id.unit);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderPattern) convertView.getTag();
        }

        ReadoutValue current = data.get(position);

        viewHolder.name.setText(current.getName());
        viewHolder.value.setText(current.getValue());
        viewHolder.unit.setText(current.getUnit());

        return convertView;
    }

}
