package com.example.lelik.rp5;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

enum ForecastPeriod {
    OneDay,
    ThreeDays,
    SixDays
}

class DownloadPageTask extends AsyncTask<String, Void, AsyncTaskResult<Forecast>> {
    private MainActivity activity;
    private ForecastPeriod forecastPeriod;

    DownloadPageTask(MainActivity activity, ForecastPeriod forecastPeriod) {
        this.activity = activity;
        this.forecastPeriod = forecastPeriod;
    }

    @Override
    protected void onPreExecute() {
        setRadioEnabled(false);
    }

    @Override
    protected AsyncTaskResult<Forecast> doInBackground(String... params) {
        try {
            String rp5 = downloadString(params[0]);
            String yartemp = downloadString(params[1]);
            Forecast forecast = ForecastParser.parseHtml(rp5, yartemp, forecastPeriod);

            return new AsyncTaskResult<>(forecast);
        }
        catch (Exception e) {
            return new AsyncTaskResult<>(e);
        }
    }

    private String downloadString(String param) throws Exception {
        URL url = new URL(param);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("Не удалось загрузить страницу по адресу " + url);
        }
        String page = IOHelper.streamToString(conn.getInputStream());
        conn.disconnect();

        return page;
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Forecast> result) {
        super.onPostExecute(result);

        if (result.getError() != null) {
            AlertDialog dlg = new AlertDialog.Builder(activity)
                    .setTitle("Ошибка сети")
                    .setMessage("Не удалось загрузить данные с сайта, возможно отсутствует доступ к сети.\n\n" + result.getError().toString())
                    .show();

            activity.setIsBusy(false);
            setRadioEnabled(true);

            return;
        }

        Forecast forecast = result.getResult();

        TextView textYartemp = (TextView) activity.findViewById(R.id.textYartemp);
        textYartemp.setText(forecast.YarTemp);

        TextView textYartempDiff = (TextView) activity.findViewById(R.id.textYartempDiff);
        textYartempDiff.setText(forecast.YarTempDiff);
        if (forecast.YarTempDiff.startsWith("-")) {
            textYartempDiff.setTextColor(Color.parseColor("#0000A0"));
        }
        else {
            textYartempDiff.setTextColor(Color.parseColor("#A00000"));
        }

        TextView textRefresh = (TextView) activity.findViewById(R.id.textRefresh);
        textRefresh.setText(forecast.RefreshDate);

        ItemsListAdapter adapter = new ItemsListAdapter(activity, forecast);
        ListView list = (ListView) activity.findViewById(R.id.myList);
        list.setAdapter(adapter);

        activity.setIsBusy(false);
        setRadioEnabled(true);
    }

    private void setRadioEnabled(boolean isEnabled) {
        setRadioButtonEnabled(R.id.radioOneDay, isEnabled);
        setRadioButtonEnabled(R.id.radioThreeDays, isEnabled);
        setRadioButtonEnabled(R.id.radioSixDays, isEnabled);
    }

    private void setRadioButtonEnabled(int id, boolean isEnabled) {
        RadioButton button = (RadioButton) activity.findViewById(id);
        button.setEnabled(isEnabled);
    }
}
