package com.example.aowenswgumobile;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.MentorTable;
import com.example.aowenswgumobile.database.TermsTable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.DatePickerFragment;

public class CourseDetailActivity extends AppCompatActivity
implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

  private DataSource mDataSource;
  private int courseId;
  private int termId;
  private String courseTitle;
  private String termTitle;
  private Cursor currentCourse;
  private Cursor currentTerm;
  private TextView startCourseDate;
  private TextView endCourseDate;
  private Spinner statusSpinner;
  private boolean isStartDatePicker;
  private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
  private static final int COURSE_DETAIL_REQUEST_CODE = 1005;
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
      currentTerm = mDataSource.getTermById(currentCourse.getString(
              currentCourse.getColumnIndex(CourseTable.COURSE_TERM_ID)));

      currentTerm.moveToFirst();

      courseTitle = currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_NAME));
      termTitle = currentTerm.getString(currentTerm.getColumnIndex(TermsTable.TERM_TITLE));
      termId = currentTerm.getInt(currentTerm.getColumnIndex(TermsTable.TERM_ID));
      Log.d(TAG, "onCreate: termId: " + termId);

      setTitle(courseTitle);

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
    switch (item.getItemId()){
      case android.R.id.home:
        onBackPressed();
        return true;
      case R.id.deleteIcon:

        final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Confirm Delete");
        myAlertDialog.setMessage("Delete " + courseTitle + " from " + termTitle + "?");
        myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface arg0, int arg1) {
            mDataSource.updateCourse(Integer.toString(courseId), 0);
            Toast.makeText(CourseDetailActivity.this,
                    courseTitle + " moved to pending courses", Toast.LENGTH_SHORT).show();
          }
        });

        myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface arg0, int arg1) {
            return;
          }
        });

        myAlertDialog.show();

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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.course_detail_menu, menu);
    return true;
  }



  public void viewMentors(View view) {
    Intent intent = new Intent(CourseDetailActivity.this, MentorActivity.class);
    Uri uri = Uri.parse(MentorTable.MENTOR_CONTENT_URI + "/" + courseId);
    intent.putExtra(MentorTable.CONTENT_ITEM_TYPE, uri);
    startActivityForResult(intent, COURSE_DETAIL_REQUEST_CODE);
  }
}
