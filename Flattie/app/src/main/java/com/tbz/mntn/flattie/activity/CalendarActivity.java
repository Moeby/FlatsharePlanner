package com.tbz.mntn.flattie.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

import com.tbz.mntn.flattie.R;

public class CalendarActivity extends AppCompatActivity {

  // intent variables
  public static final String EXTRA_DAY = "day";
  public static final String EXTRA_MONTH = "month";
  public static final String EXTRA_YEAR = "year";

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.calendar);
    CalendarView calendarView = findViewById(R.id.calendarView);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      calendarView.setOnDateChangeListener(new OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
          //Toast.makeText(getApplicationContext(), "" + day, Toast.LENGTH_SHORT).show();
          launchAddCalendarEntryActivity(year, month, day);
        }
      });
    }
  }

  private void launchAddCalendarEntryActivity(int year, int month, int day) {
    Intent intent = new Intent(getBaseContext(), AddCalendarEntryActivity.class);
    intent.putExtra(EXTRA_DAY, day);
    intent.putExtra(EXTRA_MONTH, month);
    intent.putExtra(EXTRA_YEAR, year);
    startActivity(intent);
  }
}