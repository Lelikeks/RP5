package com.example.lelik.rp5;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    SwipeRefreshLayout refresher;
    private boolean isBusy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refresher = (SwipeRefreshLayout) findViewById(R.id.refresher);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                radioClick(null);
            }
        });

        radioClick(null);
    }

    public void radioClick(View view) {
        if (getIsBusy()) {
            return;
        }

        setIsBusy(true);

        ForecastPeriod period = null;
        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);

        switch (group.getCheckedRadioButtonId()) {
            case R.id.radioOneDay:
                period = ForecastPeriod.OneDay;
                break;
            case R.id.radioThreeDays:
                period = ForecastPeriod.ThreeDays;
                break;
            case R.id.radioSixDays:
                period = ForecastPeriod.SixDays;
                break;
        }

        String url = "https://rp5.ru/%D0%9F%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0_%D0%B2_%D0%AF%D1%80%D0%BE%D1%81%D0%BB%D0%B0%D0%B2%D0%BB%D0%B5,_%D0%AF%D1%80%D0%BE%D1%81%D0%BB%D0%B0%D0%B2%D1%81%D0%BA%D0%B0%D1%8F_%D0%BE%D0%B1%D0%BB%D0%B0%D1%81%D1%82%D1%8C";
        String yartemp = "http://yartemp.com/mini/";
        new DownloadPageTask(this, period).execute(url, yartemp);
    }

    public void setIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
        refresher.setRefreshing(isBusy);
    }

    public boolean getIsBusy() {
        return isBusy;
    }
}
