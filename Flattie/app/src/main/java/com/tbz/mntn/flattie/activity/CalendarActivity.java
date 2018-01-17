package com.tbz.mntn.flattie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

import com.tbz.mntn.flattie.R;
import com.tbz.mntn.flattie.authentification.LoggedInUser;

public class CalendarActivity extends AppCompatActivity {

  int year;
  int month;
  int day;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.calendar);
    CalendarView calendarView = findViewById(R.id.calendarView);
    calendarView.setOnDateChangeListener(new OnDateChangeListener() {

      @Override
      public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
        // TODO #58: REVIEW Nadja: the CalendarView view is never used, do you add some logic with it?
        setYear(year);
        setMonth(month);
        setDay(day);

        Toast.makeText(getApplicationContext(), "" + day, Toast.LENGTH_SHORT).show();
        launchAddCalendarEntryActivity();
      }
    });
  }

  /**
   * Create Menu, logout function
   *
   * @param
   * @return
   */
  /*@Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuItem menuItem = menu.add("Logout");
    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
    menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        LoggedInUser.logoutCurrentlyLoggedInUser();
        Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
        startActivity(intent);
        return true;
      }
    });
    return super.onCreateOptionsMenu(menu);
  }*/

  private void launchAddCalendarEntryActivity() {
    Intent intent = new Intent(getBaseContext(), AddCalendarEntryActivity.class);
    intent.putExtra("Day", day);
    startActivity(intent);
  }

  private void setYear(int year) {
    this.year = year;
  }

  private void setMonth(int month) {
    this.month = month;
  }

  private void setDay(int day) {
    this.day = day;
  }

}