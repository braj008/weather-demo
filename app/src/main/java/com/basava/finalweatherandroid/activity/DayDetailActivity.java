package com.basava.finalweatherandroid.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import com.basava.finalweatherandroid.R;
import com.basava.finalweatherandroid.model.Data;
import com.basava.finalweatherandroid.utils.Constants;

public class DayDetailActivity extends AppCompatActivity {
    private TextView mTextViewSummary, mTextViewTemp, mTextViewDew, mTextViewHumidity,
            mTextViewPressure, mTextViewWindSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextViewSummary = findViewById(R.id.tv_summary_value);
        mTextViewTemp = findViewById(R.id.tv_temp_value);
        mTextViewDew = findViewById(R.id.tv_due_value);
        mTextViewHumidity = findViewById(R.id.tv_humidity_value);
        mTextViewPressure = findViewById(R.id.tv_pressure_value);
        mTextViewWindSpeed = findViewById(R.id.tv_windspeed_value);

        Bundle bundle = getIntent().getExtras();
        String title = null;
        if (bundle != null) {
            Data data = (Data) bundle.getSerializable(Constants.DAY_DETAIL);
            title = bundle.getString(Constants.DATE);
            setData(data);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
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

    private void setData(Data dataTag) {
        if (dataTag == null)
            return;
        mTextViewSummary.setText(dataTag.getSummary());
        mTextViewTemp.setText(dataTag.getTemperature());
        mTextViewDew.setText(dataTag.getDewPoint());
        mTextViewHumidity.setText(dataTag.getHumidity());
        mTextViewPressure.setText(dataTag.getPressure());
        mTextViewWindSpeed.setText(dataTag.getWindSpeed());
    }
}
