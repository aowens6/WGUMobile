package com.example.aowenswgumobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.aowenswgumobile.database.DataSource;

import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

  private DataSource mDataSource;
  private int totalCourses;
  private int completedCourses;
  private ProgressBar programProgress;
  private TextView progressLbl;
  private static final String TAG = "MainActivity";
  private static final int MAIN_REQUEST_CODE = 1000;
  public static int notificationCount;
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


    programProgress = findViewById(R.id.programProgress);
    progressLbl = findViewById(R.id.progressLbl);

    calculateProgress();


    setTitle("WGU Home");
  }

  private void calculateProgress() {
    mDataSource.open();
    totalCourses = mDataSource.getCourseCount();
    completedCourses = mDataSource.getCompletedCourses();
    int percentage = (int) Math.ceil(((double)completedCourses/ (double)totalCourses) * 100);
    programProgress.setProgress(percentage);
    progressLbl.setText("Complete Courses: " +completedCourses +"/" + totalCourses);
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == MAIN_REQUEST_CODE && resultCode == RESULT_OK) {
      Log.d(TAG, "progress: HERE");
      calculateProgress();
    }
  }
}
