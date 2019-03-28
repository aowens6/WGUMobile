package com.example.aowenswgumobile;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.aowenswgumobile.database.AssessmentTable;
import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.Assessment;
import model.Course;
import model.DatePickerFragment;

public class EditAssmtActivity extends AppCompatActivity
implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener{

  private DataSource mDataSource;
  private Bundle extras;
  private int courseId;
  private int assmtId;
  private boolean isNewAssmt;
  private boolean isGoalPicker;
  private Cursor currentCourse;
  private Cursor currentAssmt;
  private EditText assmtTitleFld;
  private TextView dueDateFld;
  private TextView goalDateFld;
  private Spinner assessmentTypeSpinner;
  private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
  private static final String TAG = "EditAssmt";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_assmt);

    mDataSource = new DataSource(this);
    mDataSource.open();

    Intent intent = getIntent();
    extras = intent.getExtras();

    assmtTitleFld = findViewById(R.id.assmtTitleFld);
    dueDateFld = findViewById(R.id.dueDateFld);
    goalDateFld = findViewById(R.id.goalDateFld);
    assessmentTypeSpinner = findViewById(R.id.assmtTypeSpinner);

    ArrayAdapter<CharSequence> spinnerAdapter =
            ArrayAdapter.createFromResource(this, R.array.assessmentTypes, android.R.layout.simple_spinner_item);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    assessmentTypeSpinner.setAdapter(spinnerAdapter);
    assessmentTypeSpinner.setOnItemSelectedListener(this);

    if (extras != null) {
      courseId = extras.getInt("courseId");
      assmtId = extras.getInt("assmtId");
      Log.d(TAG, "onCreate: courseId: " + courseId + " assmtId:  " + assmtId);
    }

    if(assmtId == 0){
      isNewAssmt = true;
    }

    if(!isNewAssmt){
      currentCourse = mDataSource.getCourseById(Integer.toString(courseId));
      currentCourse.moveToFirst();
      setTitle(currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_NAME)) + " Assessments");
      populateAssmtData();
    }else{
      setTitle("New Assessment");
    }

  }

  private void populateAssmtData(){
    currentAssmt = mDataSource.getAssmtByID(Integer.toString(assmtId));
    currentAssmt.moveToFirst();

    assmtTitleFld.setText(currentAssmt.getString(currentAssmt.getColumnIndex(AssessmentTable.ASSESSMENT_NAME)));
    dueDateFld.setText(currentAssmt.getString(currentAssmt.getColumnIndex(AssessmentTable.ASSESSMENT_DUE)));
    goalDateFld.setText(currentAssmt.getString(currentAssmt.getColumnIndex(AssessmentTable.ASSESSMENT_GOAL)));
    assessmentTypeSpinner.setSelection(currentAssmt.getInt(currentAssmt.getColumnIndex(AssessmentTable.ASSESSMENT_TYPE)));
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

  public boolean isValidData(){

    if (assmtTitleFld.getText().length() == 0) {

      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing Assessment Title");
      myAlertDialog.setMessage("Please add assessment title");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    if (dueDateFld.getText().length() == 0) {

      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing due date");
      myAlertDialog.setMessage("Please add due date");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    if (goalDateFld.getText().length() == 0) {

      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing goal date");
      myAlertDialog.setMessage("Please add goal date");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    if(assessmentTypeSpinner.getSelectedItemPosition() == 0 ||
       assessmentTypeSpinner.getSelectedItem() == null){
      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing assessment type");
      myAlertDialog.setMessage("Please add type");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    return true;
  }

  public void saveAssessment(View view) {

    if (isValidData()){
      Assessment assessment = new Assessment(assmtTitleFld.getText().toString(),
              dueDateFld.getText().toString(),
              goalDateFld.getText().toString(),
              assessmentTypeSpinner.getSelectedItemPosition(),
              courseId);

      if(isNewAssmt){
        mDataSource.insertAssessment(assessment);
      }else{
        mDataSource.updateAssessment(assessment, Integer.toString(assmtId));
      }

      setResult(RESULT_OK);
      finish();
    }

  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {

  }

  public void setDueDate(View view) {
    isGoalPicker = false;
    DialogFragment datePicker = new DatePickerFragment();
    datePicker.show(getSupportFragmentManager(), "date picker");
  }

  public void setGoalDate(View view) {
    isGoalPicker = true;
    DialogFragment datePicker = new DatePickerFragment();
    datePicker.show(getSupportFragmentManager(), "date picker");
  }

  @Override
  public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    String currentDateString = dateFormat.format(c.getTime());

    if(isGoalPicker){
      goalDateFld.setText(currentDateString);
    }else{
      dueDateFld.setText(currentDateString);
    }
  }
}