package com.example.aowenswgumobile;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.aowenswgumobile.database.AlertTable;
import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.MentorTable;
import com.example.aowenswgumobile.database.NotesTable;
import com.example.aowenswgumobile.database.TermsTable;
import com.example.aowenswgumobile.util.NotificationReceiver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import model.Alert;
import model.Course;
import model.DatePickerFragment;
import model.TimePickerFragment;

import static com.example.aowenswgumobile.MainActivity.dateFormat;

public class CourseDetailActivity extends AppCompatActivity
implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

  private DataSource mDataSource;
  private Bundle extras;
  private int courseId;
  private int termId;
  private int startCbxSelection;
  private int endCbxSelection;
  private String courseTitle;
  private String termTitle;
  private Cursor currentCourse;
  private Cursor currentTerm;
  private TextView titleFld;
  private TextView startCourseDate;
  private TextView endCourseDate;
  private CheckBox startCourseAlertCbx;
  private CheckBox endCourseAlertCbx;
  private Spinner statusSpinner;
  private Button notesBtn;
  private Button mentorsBtn;
  private Button assessmentsBtn;
  private FloatingActionButton pendingFAB;
  private boolean isStartDatePicker;
  private boolean isEndDatePicker;
  private boolean isStartCbx;
  private boolean isEndCbx;
  private boolean isNewCourse;
  private LocalDate chosenDate;
  private String chosenDateString;
  private LocalTime chosenTime;
  private LocalDate chosenStartDate;
  private LocalDate chosenEndDate;
  private LocalTime chosenStartTime;
  private LocalTime chosenEndTime;

  private static final int COURSE_DETAIL_REQUEST_CODE = 1005;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course_detail);

    mDataSource = new DataSource(this);
    mDataSource.open();

    Intent intent = getIntent();
    extras = intent.getExtras();

    titleFld = findViewById(R.id.titleFld);
    startCourseDate = findViewById(R.id.startCourseDate);
    endCourseDate = findViewById(R.id.endCourseDate);
    startCourseAlertCbx = findViewById(R.id.startCourseAlertCbx);
    endCourseAlertCbx = findViewById(R.id.endCourseAlertCbx);
    statusSpinner = findViewById(R.id.statusSpinner);

    mentorsBtn = findViewById(R.id.mentorsBtn);
    notesBtn = findViewById(R.id.notesBtn);
    assessmentsBtn = findViewById(R.id.assessmentsBtn);

    pendingFAB = findViewById(R.id.pendingFAB);

    ArrayAdapter<CharSequence> spinnerAdapter =
            ArrayAdapter.createFromResource(this, R.array.statuses, android.R.layout.simple_spinner_item);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    statusSpinner.setAdapter(spinnerAdapter);
    statusSpinner.setOnItemSelectedListener(this);

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
        isEndDatePicker = true;
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
      }
    });

    if(extras != null) {
      courseId = extras.getInt("courseId");
      termId = extras.getInt("termId");

      if(courseId == 0){
        isNewCourse = true;
        setTitle("Add new course");
        notesBtn.setVisibility(View.GONE);
        mentorsBtn.setVisibility(View.GONE);
        assessmentsBtn.setVisibility(View.GONE);
        startCourseAlertCbx.setVisibility(View.GONE);
        endCourseAlertCbx.setVisibility(View.GONE);
      }else{
        currentCourse = mDataSource.getCourseById(Integer.toString(courseId));

        currentCourse.moveToFirst();
        currentTerm = mDataSource.getTermById(currentCourse.getString(
                currentCourse.getColumnIndex(CourseTable.COURSE_TERM_ID)));

        currentTerm.moveToFirst();

        courseTitle = currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_NAME));
        termTitle = currentTerm.getString(currentTerm.getColumnIndex(TermsTable.TERM_TITLE));

        startCbxSelection = currentCourse.getInt(currentCourse.getColumnIndex(CourseTable.COURSE_START_ALERT));
        endCbxSelection = currentCourse.getInt(currentCourse.getColumnIndex(CourseTable.COURSE_END_ALERT));

        if(startCbxSelection == 1){
          startCourseAlertCbx.setChecked(true);
        }else if(startCbxSelection == 0 ){
          startCourseAlertCbx.setChecked(false);
        }

        if (endCbxSelection == 1) {
          endCourseAlertCbx.setChecked(true);
        } else if (endCbxSelection == 0) {
          endCourseAlertCbx.setChecked(false);
        }

        setTitle("Edit " + courseTitle);

        titleFld.setText(courseTitle);
        startCourseDate.setText(currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_START)));
        endCourseDate.setText(currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_END)));
        statusSpinner.setSelection(currentCourse.getInt(currentCourse.getColumnIndex(CourseTable.COURSE_STATUS_CODE)));

        pendingFAB.hide();
      }
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
            mDataSource.updateCourseTerm(Integer.toString(courseId), 0);
            Toast.makeText(CourseDetailActivity.this,
                    courseTitle + " moved to pending courses", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
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

    month++;

    chosenDate = LocalDate.of(year, month, dayOfMonth);
    chosenDateString = chosenDate.format(dateFormat);

    if(isStartCbx){
      chosenStartDate = LocalDate.of(year, month, dayOfMonth);
      DialogFragment timePickerDialog = new TimePickerFragment();
      timePickerDialog.show(getSupportFragmentManager(), "timePicker");
    }

    if(isEndCbx){
      chosenEndDate = LocalDate.of(year, month, dayOfMonth);
      DialogFragment timePickerDialog = new TimePickerFragment();
      timePickerDialog.show(getSupportFragmentManager(), "timePicker");
    }

    populateDate();
  }

  private void populateDate() {
    if(isStartDatePicker){
      startCourseDate.setText(chosenDateString);
      isStartDatePicker = false;
    }else if(isEndDatePicker){
      endCourseDate.setText(chosenDateString);
      isEndDatePicker = false;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if(!isNewCourse){
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.course_detail_menu, menu);
    }
    return true;
  }

  public void viewMentors(View view) {
    Intent intent = new Intent(CourseDetailActivity.this, MentorActivity.class);
    Uri uri = Uri.parse(MentorTable.MENTOR_CONTENT_URI + "/" + courseId);
    intent.putExtra(MentorTable.CONTENT_ITEM_TYPE, uri);
    startActivityForResult(intent, COURSE_DETAIL_REQUEST_CODE);
  }

  public void viewNotes(View view) {
    Intent intent = new Intent(CourseDetailActivity.this, NotesActivity.class);
    Uri uri = Uri.parse(NotesTable.NOTES_CONTENT_URI + "/" + courseId);
    intent.putExtra(NotesTable.CONTENT_ITEM_TYPE, uri);
    startActivityForResult(intent, COURSE_DETAIL_REQUEST_CODE);
  }

  public boolean isValidData(){

    if(titleFld.getText().length() == 0){
      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing course title");
      myAlertDialog.setMessage("Please add course title");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    if(startCourseDate.getText().length() == 0){
      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing start date");
      myAlertDialog.setMessage("Please add start date");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    if(endCourseDate.getText().length() == 0){
      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing end date");
      myAlertDialog.setMessage("Please add end date");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    if(statusSpinner.getSelectedItemPosition() == 0 ||
       statusSpinner.getSelectedItem() == null){
      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing status");
      myAlertDialog.setMessage("Please add a status");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    return true;
  }

  public void saveCourse(View view) {

    String courseTitle = titleFld.getText().toString();
    String startDate = startCourseDate.getText().toString();
    String endDate = endCourseDate.getText().toString();

    if(startCourseAlertCbx.isChecked()){
      startCbxSelection = 1;
    }else{
      startCbxSelection = 0;
    }

    if(endCourseAlertCbx.isChecked()){
      endCbxSelection = 1;
    }else{
      endCbxSelection = 0;
    }

    if(isValidData()){

      Course course = new Course(courseTitle,startDate,endDate,
              statusSpinner.getSelectedItemPosition(),
              termId, 3, startCbxSelection, endCbxSelection);

      if(isNewCourse){

        mDataSource.insertCourse(course);

        int newCourseId = mDataSource.getMaxCourseId();

        if(startCourseAlertCbx.isChecked()){
          createNewAlert(newCourseId,courseTitle + " starting soon!",
                  courseTitle + " starting on " + startDate, AlertTable.ALERT_COURSE_START,
                  LocalDateTime.of(chosenStartDate, chosenStartTime));
        }

        if(endCourseAlertCbx.isChecked()){
          createNewAlert(newCourseId,courseTitle + " ending soon!",
                  courseTitle + " ending on " + endDate, AlertTable.ALERT_COURSE_END,
                  LocalDateTime.of(chosenEndDate, chosenEndTime));
        }

      }else{

        mDataSource.updateCourse(course, Integer.toString(courseId));

        if(startCourseAlertCbx.isChecked()){
          createNewAlert(courseId,courseTitle + " starting soon!",
                  courseTitle + " starting on " + startDate, AlertTable.ALERT_COURSE_START,
                  LocalDateTime.of(chosenStartDate, chosenStartTime));
        }else{
          cancelAlert(AlertTable.ALERT_COURSE_START);
        }

        if(endCourseAlertCbx.isChecked()){
          createNewAlert(courseId,courseTitle + " ending soon!",
                  courseTitle + " ending on " + endDate, AlertTable.ALERT_COURSE_END,
                  LocalDateTime.of(chosenEndDate, chosenEndTime));
        }else{
          cancelAlert(AlertTable.ALERT_COURSE_END);
        }
      }

      setResult(RESULT_OK);
      finish();
    }

  }

  private void cancelAlert(String alertType) {
    Cursor alertCursor = mDataSource.getAlertByCourseAndType(Integer.toString(courseId), alertType);
    while(alertCursor.moveToNext()){
      int alertId = alertCursor.getInt(alertCursor.getColumnIndex(AlertTable.ALERT_ID));
      AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
      Intent myIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
      PendingIntent pendingIntent = PendingIntent.getBroadcast(
              getApplicationContext(), alertId, myIntent, 0);

      alarmManager.cancel(pendingIntent);
    }
  }

  public void createNewAlert(int courseId, String title, String content, String alertType, LocalDateTime chosenDateTime){

    //creating alert record and then retrieving its ID
    Alert alert = new Alert(courseId, alertType, 0);
    mDataSource.insertAlert(alert);
    int alertId = mDataSource.getMaxAlertId();

    ZonedDateTime zonedDateTime = chosenDateTime.atZone(ZoneId.systemDefault());
    long millis = zonedDateTime.toInstant().toEpochMilli();

    Intent intent = new Intent(CourseDetailActivity.this, NotificationReceiver.class);
    intent.putExtra("title",title);
    intent.putExtra("content", content);
    PendingIntent p1= PendingIntent.getBroadcast(getApplicationContext(), alertId, intent,0);
    AlarmManager a= (AlarmManager) getSystemService(ALARM_SERVICE);
    a.set(AlarmManager.RTC,millis,p1);
  }


  public void addPendingCourse(View view) {
    Intent intent = new Intent(CourseDetailActivity.this, CourseActivity.class);
    intent.putExtra("termId", termId);
    startActivityForResult(intent, COURSE_DETAIL_REQUEST_CODE);
  }

  public void viewAssessments(View view) {
    if(!isNewCourse){
      Intent intent = new Intent(CourseDetailActivity.this, AssessmentsActivity.class);
      intent.putExtra("courseId", courseId);
      startActivityForResult(intent, COURSE_DETAIL_REQUEST_CODE);
    }else{
      Toast.makeText(this, "Course is not saved yet", Toast.LENGTH_SHORT).show();
    }
  }

  public void chooseAlertStart(View view) {
    if(startCourseAlertCbx.isChecked()){
      isStartCbx = true;
      DialogFragment datePicker = new DatePickerFragment();
      datePicker.show(getSupportFragmentManager(), "date picker");
    }
  }

  public void chooseAlertEnd(View view) {
    if(endCourseAlertCbx.isChecked()){
      isEndCbx = true;
      DialogFragment datePicker = new DatePickerFragment();
      datePicker.show(getSupportFragmentManager(), "date picker");
    }
  }

  @Override
  public void onTimeSet(TimePicker timePicker, int hour, int minute) {
    chosenTime = LocalTime.of(hour,minute);
    if(isStartCbx){
      chosenStartTime = LocalTime.of(hour, minute);
      isStartCbx = false;
    }

    if(isEndCbx){
      chosenEndTime = LocalTime.of(hour, minute);
      isEndCbx = false;
    }
  }
}
