package com.example.aowenswgumobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.aowenswgumobile.database.DataSource;

import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

  private DataSource mDataSource;
  private static final String TAG = "MainActivity";
  private static final int MAIN_REQUEST_CODE = 1000;
  public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mDataSource = new DataSource(this);
    mDataSource.open();

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    if(!prefs.getBoolean("firstTime", false)) {
      mDataSource.initializeCourses();
      mDataSource.initializeMentors();
      SharedPreferences.Editor editor = prefs.edit();
      editor.putBoolean("firstTime", true);
      editor.commit();
    }

    setTitle("WGU Home");
  }


  @Override
  protected void onPause() {
    super.onPause();
    mDataSource.close();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mDataSource.open();
  }

  public void viewTerms(View view) {
    Intent intent = new Intent(MainActivity.this, TermActivity.class);
    startActivityForResult(intent, MAIN_REQUEST_CODE);
  }
}
