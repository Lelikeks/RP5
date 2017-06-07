package com.example.lelik.rp5;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private static final String SETTINGS_NAME = "Places3";

    private SwipeRefreshLayout refresher;
    private boolean isBusy;
    private Place currentPlace;
    private ArrayAdapter<Place> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, readPreferences());
        currentPlace = adapter.getItem(0);

        setupRefresher();
        setupDrawer();
        setupNewPlaceUrl();

        getSupportActionBar().setTitle(currentPlace.toString());
        setYartempVisibility(currentPlace.isDefault());

        radioClick(null);
    }

    private void setupNewPlaceUrl() {
        EditText newPlaceUrl = (EditText) findViewById(R.id.newPlaceUrl);
        newPlaceUrl.setOnEditorActionListener((v, actionId, event) -> {
            Place place = new Place(v.getText().toString());
            adapter.insert(place, 0);
            savePreferences();
            v.setText("");

            return false;
        });
    }

    private void savePreferences() {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < adapter.getCount(); i++) {
            value.append(adapter.getItem(i).getUrl() + ";");
        }
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(SETTINGS_NAME, value.toString());
        editor.apply();
    }

    private ArrayList<Place> readPreferences() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String value = preferences.getString(SETTINGS_NAME, Place.getDefaultPlace().getUrl());
        String[] values = value.split(";");

        ArrayList<Place> places = new ArrayList<>();
        for (String str : values) {
            places.add(new Place(str));
        }

        return places;
    }

    private void setupRefresher() {
        refresher = (SwipeRefreshLayout) findViewById(R.id.refresher);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                radioClick(null);
            }
        });
    }

    private void setupDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ListView drawerList = (ListView) findViewById(R.id.left_drawer_list);
        drawerList.setAdapter(adapter);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPlace = (Place) parent.getItemAtPosition(position);
                setYartempVisibility(currentPlace.isDefault());

                adapter.remove(currentPlace);
                adapter.insert(currentPlace, 0);
                savePreferences();

                drawerLayout.closeDrawers();
                radioClick(null);
            }
        });

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, null, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setHomeAsUpIndicator(0);
                getSupportActionBar().setTitle(currentPlace.toString());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_room_white_24dp);
                getSupportActionBar().setTitle(currentPlace.toString());
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_room_white_24dp);
    }

    private void setYartempVisibility(boolean visible) {
        View yartemp = findViewById(R.id.yartemp);
        yartemp.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        }
        else {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
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

        new DownloadPageTask(this, period).execute(currentPlace.getUrl(), "http://yartemp.com/mini/");
    }

    public void setIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
        refresher.setRefreshing(isBusy);
    }

    public boolean getIsBusy() {
        return isBusy;
    }
}
