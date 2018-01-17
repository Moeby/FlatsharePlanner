package com.tbz.mntn.flattie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Button;

import com.tbz.mntn.flattie.R;
import com.tbz.mntn.flattie.authentification.LoggedInUser;
import com.tbz.mntn.flattie.authentification.LoginController;

public class MainActivity extends AppCompatActivity {
  Context context;
  public static final int SNACKBAR_DURATION = 3000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    context = this;

    Button btnSignup = findViewById(R.id.btn_link_signup);
    Button btnLogin = findViewById(R.id.btn_login);
    final TextInputLayout usename = findViewById(R.id.login_input_layout_name);
    final TextInputLayout password = findViewById(R.id.login_input_layout_password);

    btnLogin.setOnClickListener(view -> {
      LoginController loginController = new LoginController();
      if (usename.getEditText().length() > 0
          && password.getEditText().length() > 0) {
        int loggedIn = loginController.login(usename.getEditText().getText().toString(),
            password.getEditText().getText().toString(),
            context);

        switch (loggedIn) {
          case 1:
            // TODO later: launch flattie group page instead of calendar view
            launchCalendarActivity();
            break;
          case -1:
            Snackbar.make(view, "Password incorrect. Please try again.", SNACKBAR_DURATION).show();
            break;
          case -2:
            Snackbar.make(view, "User not found.", SNACKBAR_DURATION).show();
            break;
          case -3:
            Snackbar.make(view, "Please connect to internet", SNACKBAR_DURATION).show();
            break;
          default:

        }
      } else {
        Snackbar.make(view, "Please fill out all fields.", SNACKBAR_DURATION).show();
      }
    });

    btnSignup.setOnClickListener(view -> launchSignupActivity());
  }

  private void launchSignupActivity() {
    Intent intent = new Intent(MainActivity.this, SignupActivity.class);
    startActivity(intent);
  }

  private void launchCalendarActivity() {
    Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
    startActivity(intent);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      if(LoggedInUser.isLoggedIn()){
        moveTaskToBack(true);
        return true;
      }else {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        return false;
      }
    }
    return super.onKeyDown(keyCode, event);
  }
}
