package com.basava.finalweatherandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.basava.finalweatherandroid.R;
import com.basava.finalweatherandroid.utils.Constants;

public class SelectCityActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private static final String TAG = SelectCityActivity.class.getSimpleName();

    private String[] cities = {"Select", "Bengaluru", "Bijapur", "Gulbarga", "Raichur"};
    private Button mButtonDone;
    private String mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Select City");
        }

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner_city);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cities);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        mButtonDone = findViewById(R.id.button_submit);
        mButtonDone.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mCity = cities[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                onSubmitButtonClick();
                break;
        }
    }

    private void onSubmitButtonClick() {
        goToDetailPage();
    }

    private void goToDetailPage() {
        double latitude, longitude;
        switch (mCity) {
            case "Bengaluru":
                latitude = 12.9716;
                longitude = 77.5946;
                break;

            case "Bijapur":
                latitude = 16.8302;
                longitude = 75.7100;
                break;

            case "Gulbarga":
                latitude = 17.3297;
                longitude = 76.8343;
                break;

            case "Raichur":
                latitude = 16.2120;
                longitude = 77.3439;
                break;

            default:
                showToast("Select a city");
                return;
        }
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constants.CITY, mCity);
        intent.putExtra(Constants.LATITUDE, latitude);
        intent.putExtra(Constants.LONGITUDE, longitude);
        startActivity(intent);
    }
}
