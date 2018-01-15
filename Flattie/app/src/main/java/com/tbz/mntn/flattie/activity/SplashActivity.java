package com.tbz.mntn.flattie.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tbz.mntn.flattie.R;

/**
 * Starts the splash xml.
 */
public class SplashActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash);

    new Handler().postDelayed(new Runnable() {
      public void run() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
      }
    }, 3500);
  }
}