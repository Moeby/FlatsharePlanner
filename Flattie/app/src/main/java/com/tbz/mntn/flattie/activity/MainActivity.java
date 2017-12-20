package com.tbz.mntn.flattie.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tbz.mntn.flattie.R;

public class MainActivity extends AppCompatActivity {

    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignup = (Button)findViewById(R.id.btn_link_signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchSignupActivity();
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });
    }

    private void launchSignupActivity(){
        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}
