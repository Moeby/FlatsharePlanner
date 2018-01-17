package com.tbz.mntn.flattie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tbz.mntn.flattie.R;
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
        // TODO #56: REVIEW Nadja: without checking if edit texts has a text, the app will crash when a user don't enter a info
        if(name.getEditText().getText() != null
            && email.getEditText().getText() != null
            && password.getEditText().getText() != null
            && repPassword.getEditText().getText() != null){
          int signedUp = signupController.signup(name.getEditText().getText().toString(),
              email.getEditText().getText().toString(),
              password.getEditText().getText().toString(),
              repPassword.getEditText().getText().toString(),
              context);
          // TODO #56: REVIEW Nadja: handle it same way like login controller to have a little convention how to handle snackbars
          switch (signedUp){
            case 1:
              //TODO later: launch flattie group page instead of calendar view
              launchCalendarActivity();
              break;
            case -1:
              Snackbar.make(view, "Please enter a name.", 3000).show();
              break;
            case -2:
              Snackbar.make(view, "Please enter an email address.", 3000).show();
              break;
            case -3:
              Snackbar.make(view, "Please enter a password.", 3000).show();
              break;
            case -4:
              Snackbar.make(view, "Please repeat your password.", 3000).show();
              break;
            case -5:
              Snackbar.make(view, "Please enter valid email address.", 3000).show();
              break;
            case -6:
              Snackbar.make(view, "Name or email address already in use."
                  + " Please chose another one.", 3000).show();
              break;
            case -7:
              Snackbar.make(view, "Creation of a new user account failed.", 3000).show();
              break;
            case -8:
              Snackbar.make(view, "The repeat password does not match your password.", 3000).show();
              break;
            default:
              Snackbar.make(view, "Unknown error.", 3000).show();
              break;
          }
          /*
          if (signedUp == 3) {
            Snackbar.make(view, "The repeat password does not match your password.", 3000).show();
          } else if (signedUp == 2) {
            Snackbar.make(view, "Name or email address already in use. "
            + "Please chose another one.", 3000).show();
          } else if (signedUp == 1) {
            Snackbar.make(view, "Creation of a new user account failed.", 3000).show();
          if (signedUp == 1) {
            launchCalendarActivity();
          }*/
        } else{
          Snackbar.make(view, "Please fill out all fields.", 3000).show();
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
}
