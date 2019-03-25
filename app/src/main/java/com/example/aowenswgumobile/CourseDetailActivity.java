package com.example.aowenswgumobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.TermsTable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.DatePickerFragment;

public class CourseDetailActivity extends AppCompatActivity
implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

  private DataSource mDataSource;
  private int courseId;
  private Cursor currentCourse;
  private TextView startCourseDate;
  private TextView endCourseDate;
  private Spinner statusSpinner;
  private boolean isStartDatePicker;
  private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
  private static final String TAG = "CourseDetail";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course_detail);

    mDataSource = new DataSource(this);
    mDataSource.open();

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra(TermsTable.CONTENT_ITEM_TYPE);

    Button startCourseDateBtn = findViewById(R.id.startCourseDateBtn);
    startCourseDateBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        isStartDatePicker = true;
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
      }
    });

    Button endCourseDateBtn = findViewById(R.id.endCourseDateBtn);
    endCourseDateBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        isStartDatePicker = false;
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
      }
    });

    if(uri != null) {
      courseId = Integer.parseInt(uri.getLastPathSegment());
      Log.d(TAG, "courseID: " + courseId);

      currentCourse = mDataSource.getCourseById(Integer.toString(courseId));

      currentCourse.moveToFirst();
      setTitle(currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_NAME)));

      startCourseDate = findViewById(R.id.startCourseDate);
      endCourseDate = findViewById(R.id.endCourseDate);
      statusSpinner = findViewById(R.id.statusSpinner);

      ArrayAdapter<CharSequence> spinnerAdapter =
              ArrayAdapter.createFromResource(this, R.array.statuses, android.R.layout.simple_spinner_item);
      spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      statusSpinner.setAdapter(spinnerAdapter);
      statusSpinner.setOnItemSelectedListener(this);
      statusSpinner.setSelection(currentCourse.getInt(currentCourse.getColumnIndex(CourseTable.COURSE_STATUS_CODE)));

      startCourseDate.setText(currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_START)));
      endCourseDate.setText(currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_END)));

    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
      return true;
    }
    return false;
  }

  @Override
  public void onBackPressed() {
    setResult(RESULT_OK);
    super.onBackPressed();
  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
    String text = adapterView.getItemAtPosition(position).toString();
    Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    mDataSource.updateCourseStatus(Integer.toString(courseId), position);
  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {

  }

  @Override
  public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    String currentDateString = dateFormat.format(c.getTime());

    if(isStartDatePicker){
      mDataSource.updateCourseStart(Integer.toString(courseId), currentDateString);
      TextView startCourseDate = findViewById(R.id.startCourseDate);
      startCourseDate.setText(currentDateString);
    }else{
      mDataSource.updateCourseEnd(Integer.toString(courseId), currentDateString);
      TextView endCourseDate = findViewById(R.id.endCourseDate);
      endCourseDate.setText(currentDateString);
    }
  }
}
