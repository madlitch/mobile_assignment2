package com.massimo.location;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.massimo.lab2.R;

public class AddLocation extends AppCompatActivity {

    EditText locationAddress, locationLatitude, locationLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        locationAddress = findViewById(R.id.locationAddress);
        locationLatitude = findViewById(R.id.locationLatitude);
        locationLongitude = findViewById(R.id.locationLongitude);
        setSupportActionBar(findViewById(R.id.addLocationToolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void returnToMainActivity(View view) {
        String address = locationAddress.getText().toString().trim();
        try {
            float latitude = Float.parseFloat(locationLatitude.getText().toString());
            float longitude = Float.parseFloat(locationLongitude.getText().toString());
            if (address.isEmpty()) {
                locationAddress.setError("Address must exist.");
            } else {
                Location location = new Location(address, latitude, longitude);
                LocationDatabase db = new LocationDatabase(this);
                db.saveLocation(location);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            locationLatitude.setError("Not a proper Latitude");
            locationLongitude.setError("Not a proper Longitude");
        }
    }
}
