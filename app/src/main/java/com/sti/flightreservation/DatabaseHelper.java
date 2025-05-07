package com.sti.flightreservation;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PhiLights.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Flights";

    //Column Names
    private static final String FLIGHT_NUMBER = "FlightNumber"; //Primary Key
    private static final String DESTINATION = "Destination";
    private static final String DATE_OF_FLIGHT = "DateOfFlight";
    private static final String DEPARTURE_TIME = "DepartureTime";
    private static final String ARRIVAL_TIME = "ArrivalTime";
    private static final String PASSENGER = "TypeOfPassenger";
    private static final String SEAT_NUMBER = "SeatNumber";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                FLIGHT_NUMBER + " TEXT PRIMARY KEY, " +
                DESTINATION + " TEXT NOT NULL, " +
                DATE_OF_FLIGHT + " TEXT NOT NULL, " +
                DEPARTURE_TIME + " TEXT NOT NULL, " +
                ARRIVAL_TIME + " TEXT, " +
                PASSENGER + " TEXT, " +
                SEAT_NUMBER + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String flightNumber, String destination, String dateofFlight, String departureTime, String arrivalTime, String typeofPassenger, int seatNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(FLIGHT_NUMBER, flightNumber);
        contentValues.put(DESTINATION, destination);
        contentValues.put(DATE_OF_FLIGHT, dateofFlight);
        contentValues.put(DEPARTURE_TIME, departureTime);
        contentValues.put(ARRIVAL_TIME, arrivalTime);
        contentValues.put(PASSENGER, typeofPassenger);
        contentValues.put(SEAT_NUMBER, seatNumber);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAlldata() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean updateData(String flightNumber, String destination, String dateofFlight, String departureTime, String arrivalTime, String typeofPassenger, int seatNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(FLIGHT_NUMBER, flightNumber);
        contentValues.put(DESTINATION, destination);
        contentValues.put(DATE_OF_FLIGHT, dateofFlight);
        contentValues.put(DEPARTURE_TIME, departureTime);
        contentValues.put(ARRIVAL_TIME, arrivalTime);
        contentValues.put(PASSENGER, typeofPassenger);
        contentValues.put(SEAT_NUMBER, seatNumber);

        int result = db.update(TABLE_NAME, contentValues, FLIGHT_NUMBER + " = ?", new String[]{flightNumber});
        return result > 0;
    }

    // Delete a record
    public boolean deleteData(String flightNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, FLIGHT_NUMBER + " = ? ",new String[]{flightNumber});
        return  result > 0;
    }

    //Check if a Flight Exists
    public boolean FlightExists(String flightNumber){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NAME + " WHERE " + FLIGHT_NUMBER + " = ?", new String[]{flightNumber});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    //Check if a Seat Is Available on selected Date
    public boolean seatIsAvailable(String seatNumber){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NAME + " WHERE " + SEAT_NUMBER + " = ? ", new String[]{seatNumber});
        boolean notavailable = (cursor.getCount() > 0);
        cursor.close();
        return notavailable;
    }

    public boolean dateIsAvailable(String dateofFlight){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NAME + " WHERE " + DATE_OF_FLIGHT + " = ? ", new String[]{dateofFlight});
        boolean notavailable = (cursor.getCount() > 0);
        cursor.close();
        return notavailable;
    }

    public String getSeatCount(String dateofFlight){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NAME + " WHERE " + DATE_OF_FLIGHT + " = ? ", new String[]{dateofFlight});
        String res = cursor.toString();
        return res;
    }

}
