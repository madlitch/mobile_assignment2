package com.massimo.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Adapter adapter;
    List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoteDatabase db = new NoteDatabase(this);
        noteList = db.getNoteList();

        RecyclerView recyclerView = findViewById(R.id.noteListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, noteList);
        recyclerView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.notesSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                List<Note> searchResults = db.searchNotes(query);
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
        Intent intent = new Intent(this, AddNote.class);
        startActivity(intent);
    }
}