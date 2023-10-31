package com.massimo.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "notes_db";
    private static final String DATABASE_TABLE = "notes";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_COLOR = "color";

    NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DATABASE_TABLE + "(" +
                KEY_ID + " INT PRIMARY KEY, " +
                KEY_TITLE + " TEXT, " +
                KEY_CONTENT + " TEXT, " +
                KEY_COLOR + " INTEGER" +  // Store color as an integer (color resource ID)
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public void saveNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_TITLE, note.getTitle());
        c.put(KEY_CONTENT, note.getContent());
        c.put(KEY_COLOR, note.getColor()); // Store the color
        long ID = db.insert(DATABASE_TABLE, null, c);
        Log.d("Inserted", "id:" + ID);
    }

    public List<Note> getNoteList() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> noteList = new ArrayList<>();

        String query = "SELECT * FROM " + DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)
                );
                noteList.add(note);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return noteList;
    }

    public List<Note> searchNotes(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> matchingNotes = new ArrayList<>();

        String[] columns = {KEY_TITLE, KEY_CONTENT, KEY_COLOR};
        String whereClause = KEY_TITLE + " LIKE ? OR " + KEY_CONTENT + " LIKE ? OR " + KEY_COLOR + " LIKE ? ";
        String[] whereArgs = {"%" + query + "%", "%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.query(DATABASE_TABLE, columns, whereClause, whereArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getInt(2)
                );
                matchingNotes.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return matchingNotes;
    }
}
