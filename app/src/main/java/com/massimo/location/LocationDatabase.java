package com.massimo.location;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.massimo.lab2.R;


public class LocationDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "location_db";
    private static final String DATABASE_TABLE = "locations";
    private static final String ROWID = "ROWID";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    LocationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DATABASE_TABLE + "(" +
                KEY_ADDRESS + " TEXT, " +
                KEY_LATITUDE + " FLOAT, " +
                KEY_LONGITUDE + " FLOAT" +
                ")";
        db.execSQL(query);
    }

    public void loadCoordinates(Context ctx) {
        Resources resources = ctx.getResources();

        try {
            InputStream is = resources.openRawResource(R.raw.coordinates);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonContent = new String(buffer, "UTF-8");

            JSONArray JSONLocationArray = new JSONArray(jsonContent);

            for (int i = 0; i < JSONLocationArray.length(); i++) {
                JSONObject jsonObject = JSONLocationArray.getJSONObject(i);
                float latitude = (float) jsonObject.getDouble("latitude");
                float longitude = (float) jsonObject.getDouble("longitude");

                String address = getAddressFromCoordinate(ctx, latitude, longitude);
                Log.d("LocationUtils", "Latitude: " + latitude + ", Longitude: " + longitude + ", Address: " + address);

                Location location = new Location(address, latitude, longitude);
                Log.d("Address?", address);

                saveLocation(location);
            }
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAddressFromCoordinate(Context ctx, float latitude, float longitude) {
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        String address = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);

                StringBuilder addressBuilder = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    addressBuilder.append(returnedAddress.getAddressLine(i));
                    if (i < returnedAddress.getMaxAddressLineIndex()) {
                        addressBuilder.append(", ");
                    }
                }
                address = addressBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return address;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public void nuke() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
//        db.delete(DATABASE_TABLE, null, null);
        db.close();
    }

    public void saveLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_ADDRESS, location.address);
        c.put(KEY_LATITUDE, location.latitude);
        c.put(KEY_LONGITUDE, location.longitude);
        long ID = db.insert(DATABASE_TABLE, null, c);
        Log.d("Note", "Saved id:" + ID);
    }

    public void editLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_ADDRESS, location.address);
        c.put(KEY_LATITUDE, location.latitude);
        c.put(KEY_LONGITUDE, location.longitude);
        db.update(DATABASE_TABLE, c, "ROWID = ?", new String[]{String.valueOf(location.ID)});
    }

    public void deleteLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, "ROWID = ?", new String[]{String.valueOf(location.ID)});
    }

    public List<Location> getLocationList() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Location> locationList = new ArrayList<>();

        String query = "SELECT ROWID, * FROM " + DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Location location = new Location(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getFloat(2),
                        cursor.getFloat(3)
                );
                locationList.add(location);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return locationList;
    }

    public Location getLocation(int locationID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE ROWID = " + locationID, null);
        Log.d("test", Arrays.toString(cursor.getColumnNames()));
        Log.d("test", String.valueOf(cursor.getCount()));
        cursor.moveToFirst();

        return new Location(
                locationID,
                cursor.getString(0),
                cursor.getFloat(1),
                cursor.getFloat(2)
        );
    }

    public List<Location> searchLocations(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Location> matchingLocations = new ArrayList<>();

        String[] columns = {ROWID, KEY_ADDRESS, KEY_LATITUDE, KEY_LONGITUDE};
        String whereClause = KEY_ADDRESS + " LIKE ? ";
        String[] whereArgs = {"%" + query + "%"};

        Cursor cursor = db.query(DATABASE_TABLE, columns, whereClause, whereArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Location location = new Location(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getFloat(2),
                        cursor.getFloat(3)
                );
                matchingLocations.add(location);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return matchingLocations;
    }
}
