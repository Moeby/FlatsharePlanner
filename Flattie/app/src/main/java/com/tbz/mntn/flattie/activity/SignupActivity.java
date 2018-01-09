package com.tbz.mntn.flattie.activity;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tbz.mntn.flattie.R;
import com.tbz.mntn.flattie.authentication.SignupController;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Created by Marvin on 20.12.2017.
 */

public class SignupActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        btnSignup = (Button)findViewById(R.id.btn_link_forgot_pwd);
        btnLogin  = (Button)findViewById(R.id.btn_goto_login);
    }

    private void checkSignUp(View view){
        final TextInputLayout name        = findViewById(R.id.signup_input_layout_name);
        final TextInputLayout email       = findViewById(R.id.signup_input_layout_email);
        final TextInputLayout password    = findViewById(R.id.signup_input_layout_password);
        final TextInputLayout repPassword = findViewById(R.id.signup_rep_layout_password);
        // Hash password
        String salt             = BCrypt.gensalt();
        String passwordCrypt    = BCrypt.hashpw(password.getEditText().getText().toString(), salt);
        String repPasswordCrypt = BCrypt.hashpw(repPassword.getEditText().getText().toString(), salt);
        if(passwordCrypt.equals(repPasswordCrypt)) {

            SignupController signupController = new SignupController();
            Boolean signedUp = signupController.signup(name.getEditText().getText().toString(), email.getEditText().getText().toString(), passwordCrypt, salt);
            if (signedUp == null) {
                Snackbar.make(view, "Creation of a new user account failed.", 1000).show();
            } else if (!signedUp){
                Snackbar.make(view,"Name already in use. Please chose another one.", 1000).show();
            } else{
                launchCalendarActivity();
            }
        } else{
            Snackbar.make(view, "The repeat password does not match your password.",1000).show();
        }
    }

    private void launchMainActivity(View view){
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //TODO: check if we could put it together with the function in MainActivity
    private void launchCalendarActivity(){
        Intent intent = new Intent(SignupActivity.this, CalendarActivity.class);
        startActivity(intent);
    }
}
