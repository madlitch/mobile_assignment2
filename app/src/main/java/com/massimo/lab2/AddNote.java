package com.massimo.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddNote extends AppCompatActivity {

    EditText noteTitle, noteDetails;
    Button yellowButton;
    Button greenButton;
    Button orangeButton;
    TextView noteColorBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        noteTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);
        noteColorBar = findViewById(R.id.noteColorBar);

        yellowButton = findViewById(R.id.yellowButton);
        greenButton = findViewById(R.id.greenButton);
        orangeButton = findViewById(R.id.orangeButton);

        yellowButton.setTag("@color/yellow");
        greenButton.setTag("@android:color/holo_green_light");
        orangeButton.setTag("@android:color/holo_orange_light");

        View.OnClickListener setColorListener = this::setColor;

        yellowButton.setOnClickListener(setColorListener);
        greenButton.setOnClickListener(setColorListener);
        orangeButton.setOnClickListener(setColorListener);

        setColor(yellowButton);
        setSupportActionBar(findViewById(R.id.addNoteToolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    int selectedColor;

    public void setColor(View view) {
        String colorResourceName = (String) view.getTag();
        int colorResourceID = getResources().getIdentifier(colorResourceName, "color", getPackageName());

        if (colorResourceID != 0) {
            int colorValue = ContextCompat.getColor(this, colorResourceID);
            noteColorBar.setBackgroundColor(colorValue);
            selectedColor = colorValue;
        }
    }

    public void returnToMainActivity(View view) {
        String title = noteTitle.getText().toString().trim();
        String details = noteDetails.getText().toString();

        if (title.isEmpty()) {
            noteTitle.setError("Title must exist.");
        } else {
            Note note = new Note(title, details, selectedColor);
            NoteDatabase db = new NoteDatabase(this);
            db.saveNote(note);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
