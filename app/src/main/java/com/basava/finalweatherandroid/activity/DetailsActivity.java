package com.basava.finalweatherandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.basava.finalweatherandroid.R;
import com.basava.finalweatherandroid.model.Data;
import com.basava.finalweatherandroid.model.Weather;
import com.basava.finalweatherandroid.utils.Constants;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class DetailsActivity extends BaseActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private ProgressBar mProgressBar;
    private String mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = findViewById(R.id.progress);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCity = bundle.getString(Constants.CITY);
            double latitude = bundle.getDouble(Constants.LATITUDE);
            double longitude = bundle.getDouble(Constants.LONGITUDE);

            getWeatherData(latitude, longitude);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mCity);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getWeatherData(double latitude, double longitude) {
        String url = "https://api.darksky.net/forecast/" + Constants.DARK_SKY_API_KEY + "/" + latitude + "," + longitude + "";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.e(TAG, "onFailure");
                call.cancel();
                e.printStackTrace();
                final List<com.basava.finalweatherandroid.database.Weather> weathers = com.basava.finalweatherandroid.database.Weather.getWeathers(mCity);
                if (weathers.isEmpty()) {
                    Log.e(TAG, "Data not available in DB");
                    if (e instanceof UnknownHostException) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "Loading from DB");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            final Weather weather = gson.fromJson(weathers.get(0).data, Weather.class);
                            setDataFields(weather);
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse");

                final String myResponse = response.body().string();
                Log.d(TAG, "myResponse: " + myResponse);

                com.basava.finalweatherandroid.database.Weather weatherDB = new com.basava.finalweatherandroid.database.Weather();
                weatherDB.city = mCity;
                weatherDB.data = myResponse;
                weatherDB.save();

                Gson gson = new Gson();
                final Weather weather = gson.fromJson(myResponse, Weather.class);
                Log.d(TAG, "Daily: " + weather.getDaily().getData().length);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        setDataFields(weather);
                    }
                });
            }
        });
    }

    private void setDataFields(Weather weather) {
        LinearLayout layoutDays = findViewById(R.id.layout_days);
        Log.d(TAG, "days: " + weather.getDaily().getData().length);
        for (Data data : weather.getDaily().getData()) {
            Button button = new Button(this);
            button.setText(convertTimeWithTimeZome((data.getTime())));
            button.setTag(data);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Data dataTag = (Data) v.getTag();

                    Intent intent = new Intent(DetailsActivity.this, DayDetailActivity.class);
                    intent.putExtra(Constants.DAY_DETAIL, dataTag);
                    intent.putExtra(Constants.DATE, ((Button) v).getText());
                    startActivity(intent);
                }
            });
            layoutDays.addView(button);
        }
    }

    public String convertTimeWithTimeZome(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(time * 1000L);
        return (cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR));
    }
}
