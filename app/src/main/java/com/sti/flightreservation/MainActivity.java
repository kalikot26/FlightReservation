package com.sti.flightreservation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private EditText editTextFlightNumber, editTextDestination, editTextDateOfFlight, editTextDepartureTime, editTextArrivalTime, editTextPassengerClass;
    private TextView asteriskFlightNumber, asteriskDestination, asteriskDateOfFlight, asteriskDepartureTime, asteriskArrivalTime, asteriskPassengerClass;
    private Button buttonBook;
    private Calendar calendar;

    //Tests
    private TextView textViewseatCount;


    private int seatNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        //initialize input fields
        editTextFlightNumber = findViewById(R.id.editTextFlightNumber);
        editTextDestination = findViewById(R.id.editTextDestination);
        editTextDateOfFlight = findViewById(R.id.editTextDateOfFlight);
        editTextDepartureTime = findViewById(R.id.editTextDepartureTime);
        editTextArrivalTime = findViewById(R.id.editTextArrivalTime);
        buttonBook = findViewById(R.id.buttonBook);

        //Asterisk TextViews
        asteriskFlightNumber = findViewById(R.id.textViewAsteriskFlightNumber);
        asteriskDestination = findViewById(R.id.textViewAsteriskDestination);
        asteriskDateOfFlight = findViewById(R.id.textViewAsteriskDateOfFlight);
        asteriskDepartureTime = findViewById(R.id.textViewAsteriskDepartureTime);
        asteriskArrivalTime = findViewById(R.id.textViewAsteriskArrivalTime);
        asteriskPassengerClass = findViewById(R.id.textViewAsteriskPassengerClass);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerPassengerClass);

        calendar = Calendar.getInstance();
        int randomInt = (int)(Math.random() * 1000);

        editTextFlightNumber.setText("PHILIGHT" + randomInt);


        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.passenger_class,
                android.R.layout.simple_spinner_item
        );

        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        spinner.setAdapter(adapter);

        //Setup date picker for Flight
        editTextDateOfFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showDatePickerDialog();}

        });

        editTextDepartureTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editTextDepartureTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                calculateArrival(hour, minute);

            }
        });

        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { bookDate();}
        });

    }

    private void calculateArrival(int hours, int minutes) {
        int hour30 = hours;
        int minute30 = minutes;
        String min30, min45;
        int newmin = minute30 + 30;
        int newmins = minute30 + 45;
        if(newmin > 60){
            hour30++;
            newmin -= 60;
            min30 = hour30 + ":" + newmin;
        }else{
            min30 = hour30 + ":" + newmin;
        }
        int hour45 = hours;
        int minute45 = minutes;
        if(newmins > 60){
            hour45++;
            newmins -= 60;
            min45 = hour45 + ":" + newmins;
        }else{
            min45 = hour45 + ":" + newmins;
        }

        editTextArrivalTime.setText(min30 + " - " + min45);

    }


    private void bookDate() {

        Spinner spinner = (Spinner) findViewById(R.id.spinnerPassengerClass);
        String flightNumber = editTextFlightNumber.getText().toString().trim();
        String destination = editTextDestination.getText().toString().trim();
        String dateofFlight = editTextDateOfFlight.getText().toString().trim();
        String departureTime = editTextDepartureTime.getText().toString().trim();
        String arrivalTime = editTextArrivalTime.getText().toString().trim();
        String passengerClass = spinner.getSelectedItem().toString().trim();

        //reset Visibility of Asterisks
        asteriskFlightNumber.setVisibility(View.GONE);
        asteriskDestination.setVisibility(View.GONE);
        asteriskDateOfFlight.setVisibility(View.GONE);
        asteriskDepartureTime.setVisibility(View.GONE);
        asteriskArrivalTime.setVisibility(View.GONE);
        asteriskPassengerClass.setVisibility(View.GONE);

        boolean isValid = true;
        if (TextUtils.isEmpty(flightNumber)){
            asteriskFlightNumber.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (TextUtils.isEmpty(destination)){
            asteriskDestination.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (TextUtils.isEmpty(dateofFlight)){
            asteriskDateOfFlight.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (TextUtils.isEmpty(departureTime)){
            asteriskDepartureTime.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (TextUtils.isEmpty(arrivalTime)){
            asteriskArrivalTime.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (TextUtils.isEmpty(passengerClass)){
            asteriskPassengerClass.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (!isValid){
            Toast.makeText(this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
            return;
        }


        boolean isInserted = db.insertData(flightNumber, destination, dateofFlight, departureTime, arrivalTime, passengerClass, seatNumber);
        if (isInserted){
            Toast.makeText(this, "BOOK SUCCESSFUL! HAVE A NICE TRIP", Toast.LENGTH_SHORT).show();
            clearFields();
            Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this, "BOOK FAILED! PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateDateField() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        editTextDateOfFlight.setText(dateFormat.format(calendar.getTime()));
    }


    private void clearFields() {
        editTextFlightNumber.setText("");  //update to autogen
        editTextDestination.setText("");
        editTextDateOfFlight.setText("");
        editTextDepartureTime.setText("");
        editTextArrivalTime.setText("");

        asteriskFlightNumber.setVisibility(View.GONE);
        asteriskDestination.setVisibility(View.GONE);
        asteriskDateOfFlight.setVisibility(View.GONE);
        asteriskDepartureTime.setVisibility(View.GONE);
        asteriskArrivalTime.setVisibility(View.GONE);
        asteriskPassengerClass.setVisibility(View.GONE);

    }

    private void showDatePickerDialog() {
        int year =calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                calendar.set(Calendar.YEAR, selectedYear);
                calendar.set(Calendar.MONTH, selectedMonth);
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                updateDateField();

            }
        }, year, month, day);

        datePickerDialog.show();
    }



}