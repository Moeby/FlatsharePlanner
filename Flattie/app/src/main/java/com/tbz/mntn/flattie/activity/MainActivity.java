package com.tbz.mntn.flattie.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.tbz.mntn.flattie.R;
import com.tbz.mntn.flattie.authentication.LoginController;

public class MainActivity extends AppCompatActivity {
  Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    context = this;

    Button                btnSignup = findViewById(R.id.btn_link_signup);
    Button                btnLogin  = findViewById(R.id.btn_login);
    final TextInputLayout email     = findViewById(R.id.login_input_layout_name);
    final TextInputLayout password  = findViewById(R.id.login_input_layout_password);

    btnLogin.setOnClickListener(view -> {
      LoginController loginController = new LoginController();
      Boolean loggedIn = loginController.login(email.getEditText().getText().toString(),
                                               password.getEditText().getText().toString(),
                                               context);

      if (loggedIn == null) {
        Snackbar.make(view, "User not found.", 1000).show();
      } else if (!loggedIn) {
        Snackbar.make(view, "Password incorrect. Please try again.", 1000).show();
      } else {
        // TODO later: launch flattie group page instead of calendar view
        launchCalendarActivity();
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
}
