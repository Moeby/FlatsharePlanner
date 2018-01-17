package com.tbz.mntn.flattie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.tbz.mntn.flattie.R;
import com.tbz.mntn.flattie.authentification.LoggedInUser;
import com.tbz.mntn.flattie.authentification.SignupController;

public class SignupActivity extends AppCompatActivity {
  Context context;

  private Button btnLogin;
  private Button btnSignup;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.signup);
    context = this;

    btnSignup = (Button) findViewById(R.id.btn_signup);
    btnLogin = (Button) findViewById(R.id.btn_goto_login);
    final TextInputLayout name        = findViewById(R.id.signup_input_layout_name);
    final TextInputLayout email       = findViewById(R.id.signup_input_layout_email);
    final TextInputLayout password    = findViewById(R.id.signup_input_layout_password);
    final TextInputLayout repPassword = findViewById(R.id.signup_rep_layout_password);

    btnSignup.setOnClickListener(new View.OnClickListener() {
      //TODO: implement inputfield checkers and error messages
      @Override
      public void onClick(View view) {
        SignupController signupController = new SignupController();
        if (name.getEditText().length() > 0
            && email.getEditText().length() > 0
            && password.getEditText().length() > 0
            && repPassword.getEditText().length() > 0) {
          int signedUp = signupController.signup(name.getEditText().getText().toString(),
                                                 email.getEditText().getText().toString(),
                                                 password.getEditText().getText().toString(),
                                                 repPassword.getEditText().getText().toString(),
                                                 context);
          switch (signedUp) {
            case 1:
              //TODO later: launch flattie group page instead of calendar view
              launchCalendarActivity();
              break;
            case -1:
              Snackbar.make(view, "Please enter valid email address.",
                            MainActivity.SNACKBAR_DURATION).show();
              break;
            case -2:
              Snackbar.make(view, "Name or email address already in use. Please chose another one.",
                            MainActivity.SNACKBAR_DURATION).show();
              break;
            case -3:
              Snackbar.make(view, "Creation of a new user account failed.",
                            MainActivity.SNACKBAR_DURATION).show();
              break;
            case -4:
              Snackbar.make(view, "The repeat password does not match your password.",
                            MainActivity.SNACKBAR_DURATION).show();
              break;
            case -5:
              Snackbar.make(view, "Please connect to internet.",
                            MainActivity.SNACKBAR_DURATION).show();
              break;
            default:
              Snackbar.make(view, "Unknown error.", MainActivity.SNACKBAR_DURATION).show();
              break;
          }
        } else {
          Snackbar.make(view, "Please fill out all fields.", MainActivity.SNACKBAR_DURATION).show();
        }
      }
    });

    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        launchMainActivity(view);
      }
    });
  }

  private void launchMainActivity(View view) {
    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
    startActivity(intent);
  }

  private void launchCalendarActivity() {
    Intent intent = new Intent(SignupActivity.this, CalendarActivity.class);
    startActivity(intent);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      if (LoggedInUser.isLoggedIn()) {
        moveTaskToBack(true);
        return true;
      } else {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        return false;
      }
    }
    return super.onKeyDown(keyCode, event);
  }
}
