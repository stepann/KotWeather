package com.stepanzalis.kotweather.ui.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stepanzalis.kotweather.R;
import com.stepanzalis.kotweather.model.WeatherAPI;

import java.util.List;

/* public class LongTermForecast extends RecyclerView.Adapter<LongTermForecast.ViewHolder> {

    FiveDaysForecast fiveDaysForecast;
    private List<WeatherAPI> itemList;
    Context mContext;

    public LongTermForecast(FiveDaysForecast fiveDaysForecast) {
        this.fiveDaysForecast = fiveDaysForecast;
    }

    public LongTermForecast(Context context, FiveDaysForecast fiveDaysForecast) {
        this.mContext = context;
        this.fiveDaysForecast = fiveDaysForecast;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, desc, wind, pressure, humidity, temp, units;
        ImageView weatherIcon;

        public ViewHolder(View rowView) {
            super(rowView);
            date = (TextView) rowView.findViewById(R.id.itemDate);
            temp = (TextView) rowView.findViewById(R.id.itemTemp);
            desc = (TextView) rowView.findViewById(R.id.itemDesc);
            wind = (TextView) rowView.findViewById(R.id.itemWind);
            units = (TextView) rowView.findViewById(R.id.itemUnits);
            pressure = (TextView) rowView.findViewById(R.id.itemPressure);
            humidity = (TextView) rowView.findViewById(R.id.itemHumidity);
            weatherIcon = (ImageView) rowView.findViewById(R.id.itemIcon);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //fiveDaysForecast = holder.date.setText();
        //weatherIcon.setColorFilter( 0xffff0000, Mode.MULTIPLY)
    }

    @Override
    public int getItemCount() {
        //return;
    }
}
*/


