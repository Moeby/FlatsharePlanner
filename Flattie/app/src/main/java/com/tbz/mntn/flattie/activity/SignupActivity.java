package com.tbz.mntn.flattie.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tbz.mntn.flattie.R;

/**
 * Created by Marvin on 20.12.2017.
 */

public class SignupActivity extends AppCompatActivity {

    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        btnLogin = (Button)findViewById(R.id.btn_link_forgot_pwd);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchMainActivity();
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
            }
        });
    }

    private void launchMainActivity(){
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
