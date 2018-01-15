package com.tbz.mntn.flattie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tbz.mntn.flattie.R;
import com.tbz.mntn.flattie.authentification.SignupController;

public class SignupActivity extends AppCompatActivity {

  private Button btnLogin;
  private Button btnSignup;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.signup);

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
        Boolean signedUp = signupController.signup(name.getEditText().getText().toString(),
                                                   email.getEditText().getText().toString(),
                                                   password.getEditText().getText().toString(),
                                                   repPassword.getEditText().getText().toString(),
                                                   view);
        // TODO #56: REVIEW Nadja: handle it same way like login controller to have a little convention how to handle snackbars
        /*
        if (signedUp == 3) {
          Snackbar.make(view, "The repeat password does not match your password.", 3000).show();
        } else if (signedUp == 2) {
          Snackbar.make(view, "Name or email address already in use. "
          + "Please chose another one.", 3000).show();
        } else if (signedUp == 1) {
          Snackbar.make(view, "Creation of a new user account failed.", 3000).show();
          */
        if (signedUp) {
          //} else {
          launchCalendarActivity();
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

  //TODO: check if we could put it together with the function in MainActivity
  private void launchCalendarActivity() {
    Intent intent = new Intent(SignupActivity.this, CalendarActivity.class);
    startActivity(intent);
  }
}
