package com.example.lelik.rp5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class ItemsListAdapter extends BaseAdapter {
    private static Forecast forecast;
    private static LayoutInflater inflater = null;
    private static ResourceHelper resourceHelper;

    ItemsListAdapter(Context context, Forecast forecast) {
        ItemsListAdapter.forecast = forecast;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resourceHelper = new ResourceHelper(context);
    }

    @Override
    public int getCount() {
        return forecast.Data.length;
    }

    @Override
    public Object getItem(int position) {
        return forecast.Data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ForecastData current = forecast.Data[position];
        View view = inflater.inflate(R.layout.row, null);

        TextView textHour = (TextView) view.findViewById(R.id.textHour);
        textHour.setText(current.Hour);

        TextView textTemp = (TextView) view.findViewById(R.id.textTemp);
        textTemp.setText(current.Temp);
        textTemp.setTextColor(current.Temp.startsWith("-")
                ? view.getResources().getColor(R.color.temp_minus)
                : view.getResources().getColor(R.color.temp_plus));

        SetGraphicItem(current.Clouds, view, R.id.imageClouds, ImageType.Clouds);
        SetGraphicItem(current.Rain.FirstHalf, view, R.id.imageRainFirst, ImageType.Rain);
        SetGraphicItem(current.Rain.SecondHalf, view, R.id.imageRainSecond, ImageType.Rain);

        TextView textRainMMFirst = (TextView) view.findViewById(R.id.textRainMMFirst);
        textRainMMFirst.setText(current.Rain.FirstHalfNum);

        TextView textRainMMSecond = (TextView) view.findViewById(R.id.textRainMMSecond);
        textRainMMSecond.setText(current.Rain.SecondHalfNum);

        ImageView windDirection = (ImageView) view.findViewById(R.id.imageWind);
        windDirection.setImageBitmap(resourceHelper.GetWindBitmap(current.Wind.Direction));

        TextView textWindStrength = (TextView) view.findViewById(R.id.textWindStrength);
        textWindStrength.setText(current.Wind.Strength);

        TextView textWindGusts = (TextView) view.findViewById(R.id.textWindGusts);
        textWindGusts.setText(current.Wind.Gusts);

        TextView textDay = (TextView) view.findViewById(R.id.textDay);
        textDay.setText(current.DayOfWeek);

        if (!current.IsDayLight) {
            if (current.DayOfWeek == null) {
                view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.night));
            }
            else {
                view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.night_border));
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + 2, view.getPaddingRight(), view.getPaddingBottom());
            }
        }

        return view;
    }

    private void SetGraphicItem(String cssClass, View view, int imageId, ImageType type) {
        Bitmap bmp = resourceHelper.GetBitmap(cssClass, type);
        ImageView imageItem = (ImageView) view.findViewById(imageId);
        imageItem.setImageBitmap(bmp);
    }
}
