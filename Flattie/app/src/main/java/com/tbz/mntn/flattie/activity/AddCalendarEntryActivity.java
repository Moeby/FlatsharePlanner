package com.tbz.mntn.flattie.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.tbz.mntn.flattie.R;
import com.tbz.mntn.flattie.authentification.LoggedInUser;
import com.tbz.mntn.flattie.calendar.CalendarEntryController;
import com.tbz.mntn.flattie.database.dao.DaoFactory;
import com.tbz.mntn.flattie.database.dao.EventCategoryDao;
import com.tbz.mntn.flattie.database.dataclasses.EventCategory;
import com.tbz.mntn.flattie.database.dataclasses.Repeatable;
import com.tbz.mntn.flattie.database.dataclasses.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Activity for adding an calendar event.
 */
public class AddCalendarEntryActivity extends AppCompatActivity {
  private Date startDate;
  private Date endDate;

  private Date initStartDate;
  private Date initEndDate;

  @SuppressLint("SimpleDateFormat")
  private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy hh:mm aa");

  private SlideDateTimeListener listener = new SlideDateTimeListener() {
    // private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy hh:mm aa");

    /**
     * DateTime set on Datetimepicker.
     * @param date  The {@code Date} object that contains the date
     */
    @Override
    public void onDateTimeSet(Date date) {
      startDate = date;
      TextInputLayout startDate = findViewById(R.id.cal_start_date);
      if (endDate == null || date.before(endDate)) {
        startDate.getEditText().setText(dateFormat.format(date));
      } else {
        startDate.getEditText().setText("");
        // show error hint
        Toast.makeText(AddCalendarEntryActivity.this,
                       "Startdate cannot be later than enddate. Try again",
                       Toast.LENGTH_LONG).show();
      }
    }

    /**
     *  Optional cancel listener.
     */
    @Override
    public void onDateTimeCancel() {
      Toast.makeText(AddCalendarEntryActivity.this,
                     "Canceled", Toast.LENGTH_SHORT).show();
    }
  };

  private SlideDateTimeListener listener2 = new SlideDateTimeListener() {
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy hh:mm aa");

    /**
     * DateTime set on Datetimepicker.
     * @param date  The {@code Date} object that contains the date
     */
    @Override
    public void onDateTimeSet(Date date) {
      endDate = date;
      TextInputLayout endDate = findViewById(R.id.cal_end_date);
      if (startDate == null || date.after(startDate)) {
        endDate.getEditText().setText(dateFormat.format(date));
      } else {
        endDate.getEditText().setText("");
        // show error hint
        Toast.makeText(AddCalendarEntryActivity.this,
                       "Enddate cannot be earlier than startdate. Try again",
                       Toast.LENGTH_LONG).show();
      }
    }

    // Optional cancel listener
    @Override
    public void onDateTimeCancel() {
      Toast.makeText(AddCalendarEntryActivity.this,
                     "Canceled", Toast.LENGTH_SHORT).show();
    }
  };

  @RequiresApi(api = Build.VERSION_CODES.N)
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.add_calentar_entry);

    Intent intent = getIntent();
    int    day    = intent.getIntExtra(CalendarActivity.EXTRA_INT_DAY, 0);
    int    month  = intent.getIntExtra(CalendarActivity.EXTRA_INT_MONTH, 0);
    int    year   = intent.getIntExtra(CalendarActivity.EXTRA_INT_YEAR, 0);

    User loggedInUser = LoggedInUser.getLoggedInUser();

    final EditText        startDate         = findViewById(R.id.cal_start_date_input);
    final EditText        endDate           = findViewById(R.id.cal_end_date_input);
    final Button          submit            = findViewById(R.id.btn_add_cal_entry);
    final TextInputLayout description       = findViewById(R.id.cal_entry_layout_desc);
    final Spinner         repeatableSpinner = findViewById(R.id.cal_entry_repeatable);
    final Spinner         categorySpinner   = findViewById(R.id.cal_entry_category);
    final Spinner         peopleSpinner     = findViewById(R.id.cal_entry_people);

    // todo: change into not deprecated version
    initStartDate = new Date(year - 1900, month, day);
    initEndDate = new Date(year - 1900, month, day);
    startDate.setText(dateFormat.format(initStartDate));
    endDate.setText(dateFormat.format(initStartDate));

    setRepeatableSpinnerValues(repeatableSpinner);
    setCategorySpinnerValues(categorySpinner);
    setGroupMembersSpinnerValues(peopleSpinner);

    addListenersToDateFields(startDate, endDate);

    submit.setOnClickListener(v -> {
      // Check dates and description are not empty
      if (this.startDate != null
          && this.endDate != null
          && description.getEditText().length() > 0) {
        CalendarEntryController calendarEntryController = new CalendarEntryController();
        Boolean isItemSavedToDb =
            calendarEntryController.saveCalendarEntryToDatabase(description.getEditText()
                                                                           .getText().toString(),
                                                                repeatableSpinner.getSelectedItem()
                                                                                 .toString(),
                                                                this.startDate,
                                                                this.endDate,
                                                                loggedInUser.getGroup(),
                                                                categorySpinner.getSelectedItem()
                                                                               .toString(),
                                                                peopleSpinner.getSelectedItem()
                                                                             .toString());
        if (isItemSavedToDb) {
          launchCalendarActivity();
        } else {
          Snackbar.make(v, "Item could not be saved. Please try again.", 1000).show();
        }
      } else {
        Snackbar.make(v, "Date missing.", 1000).show();
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
        Intent intent = new Intent(AddCalendarEntryActivity.this, MainActivity.class);
        startActivity(intent);
        return true;
      }
    });
    return super.onCreateOptionsMenu(menu);
  }

  private void addListenersToDateFields(EditText startDate, EditText endDate) {
    try {
      initStartDate = dateFormat.parse(startDate.getText().toString());
      initEndDate = dateFormat.parse(endDate.getText().toString());
    } catch (ParseException e) {
      initStartDate = new Date();
      initEndDate = new Date();
      Log.w("Parse Date Problem", e);
    }
    startDate.setOnClickListener(v -> new SlideDateTimePicker.Builder(getSupportFragmentManager())
        .setListener(listener)
        .setInitialDate(initStartDate)
        .setMinDate(new Date())
        .build()
        .show());

    endDate.setOnClickListener(v -> new SlideDateTimePicker.Builder(getSupportFragmentManager())
        .setListener(listener2)
        .setInitialDate(initEndDate)
        .setMinDate(new Date())
        .build()
        .show());
  }

  private void setRepeatableSpinnerValues(Spinner repeatableSpinner) {
    String[] repeatableNames = Arrays.toString(Repeatable.values())
                                     .replaceAll("^.|.$", "").split(", ");

    ArrayAdapter<String> repeatableSpinnerArrayAdapter = new ArrayAdapter<>(
        this, android.R.layout.simple_spinner_item, repeatableNames);
    repeatableSpinnerArrayAdapter
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    repeatableSpinner.setAdapter(repeatableSpinnerArrayAdapter);
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private void setCategorySpinnerValues(Spinner categorySpinner) {
    EventCategoryDao    eventCategoryDao = DaoFactory.getEventCategoryDao();
    List<EventCategory> eventCategories  = eventCategoryDao.selectAll();
    String[] categoryNames = eventCategories.stream().map(EventCategory::getName)
                                            .toArray(String[]::new);

    ArrayAdapter<String> repeatableSpinnerArrayAdapter = new ArrayAdapter<>(
        this, android.R.layout.simple_spinner_item, categoryNames);
    repeatableSpinnerArrayAdapter
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    categorySpinner.setAdapter(repeatableSpinnerArrayAdapter);
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private void setGroupMembersSpinnerValues(Spinner peopleSpinner) {
    User            loggedInUser = LoggedInUser.getLoggedInUser();
    ArrayList<User> groupUsers   = loggedInUser.getGroup().getUsers();
    String[] peopleNames = groupUsers.stream().map(User::getUsername)
                                     .toArray(String[]::new);

    ArrayAdapter<String> repeatableSpinnerArrayAdapter = new ArrayAdapter<>(
        this, android.R.layout.simple_spinner_item, peopleNames);
    repeatableSpinnerArrayAdapter
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    peopleSpinner.setAdapter(repeatableSpinnerArrayAdapter);
  }

  private void launchCalendarActivity() {
    Intent intent = new Intent(AddCalendarEntryActivity.this, CalendarActivity.class);
    startActivity(intent);
  }
}
