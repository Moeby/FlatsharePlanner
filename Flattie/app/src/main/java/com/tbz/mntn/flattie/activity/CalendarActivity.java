package com.tbz.mntn.flattie.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

import com.tbz.mntn.flattie.R;

/**
 * Created by Marvin on 04.01.2018.
 */

public class CalendarActivity extends AppCompatActivity{

    int year;
    int month;
    int day;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                setYear(year);
                setMonth(month);
                setDay(day);

                Toast.makeText(getApplicationContext(), ""+day, Toast.LENGTH_SHORT).show();
                launchAddCalendarEntryActivity();
            }
        });
    }

    private void launchAddCalendarEntryActivity() {
        Intent intent = new Intent(getBaseContext(), AddCalendarEntryActivity.class);
        intent.putExtra("Day", day);
        startActivity(intent);
    }

    private void setYear(int year){
        this.year = year;
    }

    private void setMonth(int month){
        this.month = month;
    }

    private void setDay(int day){
        this.day = day;
    }

}