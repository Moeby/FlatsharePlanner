package com.tbz.mntn.flattie.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

  private SlideDateTimeListener listener = new SlideDateTimeListener() {
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy hh:mm aa");

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

    // Optional cancel listener
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
    User loggedInUser = LoggedInUser.getLoggedInUser();

    final EditText        startDate         = findViewById(R.id.cal_start_date_input);
    final EditText        endDate           = findViewById(R.id.cal_end_date_input);
    final Button          submit            = findViewById(R.id.btn_add_cal_entry);
    final TextInputLayout description       = findViewById(R.id.cal_entry_layout_desc);
    final Spinner         repeatableSpinner = findViewById(R.id.cal_entry_repeatable);
    final Spinner         categorySpinner   = findViewById(R.id.cal_entry_category);
    final Spinner         peopleSpinner     = findViewById(R.id.cal_entry_people);

    setRepeatableSpinnerValues(repeatableSpinner);
    setCategorySpinnerValues(categorySpinner);
    setGroupMembersSpinnerValues(peopleSpinner);

    addListenersToDateFields(startDate, endDate);

    submit.setOnClickListener(v -> {
      // Check dates and description are not empty
      if (this.startDate != null && this.endDate != null) {
        CalendarEntryController calendarEntryController = new CalendarEntryController();
        Boolean isItemSavedToDb =
            calendarEntryController.saveCalendarEntryToDatabase(description.getEditText().getText().toString(),
                                                                repeatableSpinner.getSelectedItem().toString(),
                                                                this.startDate,
                                                                this.endDate,
                                                                loggedInUser.getGroup(),
                                                                categorySpinner.getSelectedItem().toString(),
                                                                peopleSpinner.getSelectedItem().toString());
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

  private void addListenersToDateFields(EditText startDate, EditText endDate) {
    startDate.setOnClickListener(v -> new SlideDateTimePicker.Builder(getSupportFragmentManager())
        .setListener(listener)
        .setInitialDate(new Date())
        .setMinDate(new Date())
        .build()
        .show());

    endDate.setOnClickListener(v -> new SlideDateTimePicker.Builder(getSupportFragmentManager())
        .setListener(listener2)
        .setInitialDate(new Date())
        .setMinDate(new Date())
        .build()
        .show());
  }

  private void setRepeatableSpinnerValues(Spinner repeatableSpinner) {
    String[] repeatableNames = Arrays.toString(Repeatable.values()).replaceAll("^.|.$", "").split(", ");

    ArrayAdapter<String> repeatableSpinnerArrayAdapter = new ArrayAdapter<>(
        this, android.R.layout.simple_spinner_item, repeatableNames);
    repeatableSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    repeatableSpinner.setAdapter(repeatableSpinnerArrayAdapter);
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private void setCategorySpinnerValues(Spinner categorySpinner) {
    EventCategoryDao    eventCategoryDao = DaoFactory.getEventCategoryDao();
    List<EventCategory> eventCategories  = eventCategoryDao.selectAll();
    String[]            categoryNames    = eventCategories.stream().map(EventCategory::getName).toArray(String[]::new);

    ArrayAdapter<String> repeatableSpinnerArrayAdapter = new ArrayAdapter<>(
        this, android.R.layout.simple_spinner_item, categoryNames);
    repeatableSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    categorySpinner.setAdapter(repeatableSpinnerArrayAdapter);
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private void setGroupMembersSpinnerValues(Spinner peopleSpinner) {
    User            loggedInUser = LoggedInUser.getLoggedInUser();
    ArrayList<User> groupUsers   = loggedInUser.getGroup().getUsers();
    String[]        peopleNames  = groupUsers.stream().map(User::getUsername).toArray(String[]::new);

    ArrayAdapter<String> repeatableSpinnerArrayAdapter = new ArrayAdapter<>(
        this, android.R.layout.simple_spinner_item, peopleNames);
    repeatableSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    peopleSpinner.setAdapter(repeatableSpinnerArrayAdapter);
  }

  private void launchCalendarActivity() {
    Intent intent = new Intent(AddCalendarEntryActivity.this, CalendarActivity.class);
    startActivity(intent);
  }
}
