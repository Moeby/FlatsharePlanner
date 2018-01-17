package com.tbz.mntn.flattie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

import com.tbz.mntn.flattie.R;
import com.tbz.mntn.flattie.authentification.LoggedInUser;

import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

  // intent variables
  public static final String EXTRA_INT_DAY   = "day";
  public static final String EXTRA_INT_MONTH = "month";
  public static final String EXTRA_INT_YEAR  = "year";

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.calendar);
    CalendarView calendarView = findViewById(R.id.calendarView);
    calendarView.setOnDateChangeListener(new OnDateChangeListener() {
      @Override
      public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
        //Toast.makeText(getApplicationContext(), "" + day, Toast.LENGTH_SHORT).show();
        launchAddCalendarEntryActivity(year, month, day);
      }
    });
  }

  /**
   * Create Menu, logout function.
   * @param menu button in top bar
   * @return true if onclickevent is successful, false if not
   */
  @Override
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
  }

  private void launchAddCalendarEntryActivity(int year, int month, int day) {
    Intent intent = new Intent(getBaseContext(), AddCalendarEntryActivity.class);
    intent.putExtra(EXTRA_INT_DAY, day);
    intent.putExtra(EXTRA_INT_MONTH, month);
    intent.putExtra(EXTRA_INT_YEAR, year);
    startActivity(intent);
  }
}