package com.massimo.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.massimo.lab2.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Adapter adapter;
    List<Location> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationDatabase db = new LocationDatabase(this);

//        load the coordinates from the file, geocode, then put them into the database
        db.loadCoordinates(this);

        locationList = db.getLocationList();

        RecyclerView recyclerView = findViewById(R.id.noteListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, locationList);
        recyclerView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.notesSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                List<Location> searchResults = db.searchLocations(query);
                adapter.setData(searchResults);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    public void navigateToAddNote(View view) {
        Intent intent = new Intent(this, AddLocation.class);
        startActivity(intent);
    }
}