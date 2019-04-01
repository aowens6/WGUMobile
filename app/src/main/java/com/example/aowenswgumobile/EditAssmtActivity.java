package com.example.aowenswgumobile;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.aowenswgumobile.database.AlertTable;
import com.example.aowenswgumobile.database.AssessmentTable;
import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.util.NotificationReceiver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import model.Alert;
import model.Assessment;
import model.DatePickerFragment;
import model.TimePickerFragment;

import static com.example.aowenswgumobile.MainActivity.dateFormat;


public class EditAssmtActivity extends AppCompatActivity
implements AdapterView.OnItemSelectedListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

  private DataSource mDataSource;
  private Bundle extras;
  private int courseId;
  private int assmtId;
  private String assmtTitle;
  private String courseTitle;
  private boolean isNewAssmt;
  private boolean isGoalPicker;
  private Cursor currentCourse;
  private Cursor currentAssmt;
  private EditText assmtTitleFld;
  private TextView dueDateFld;
  private TextView startAssmtDateFld;
  private Spinner assessmentTypeSpinner;
  private CheckBox startAssmtAlertCbx;
  private CheckBox dueAssmtAlertCbx;
  private int startCbxSelection;
  private int dueCbxSelection;
  private boolean isStartCbx;
  private boolean isDueCbx;
  private LocalDate chosenStartAlertDate;
  private LocalDate chosenDueAlertDate;
  private LocalTime chosenStartAlertTime;
  private LocalTime chosenDueAlertTime;
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
    startAssmtDateFld = findViewById(R.id.startAssmtDateFld);
    dueDateFld = findViewById(R.id.dueDateFld);
    assessmentTypeSpinner = findViewById(R.id.assmtTypeSpinner);
    startAssmtAlertCbx = findViewById(R.id.startAssmtAlertCbx);
    dueAssmtAlertCbx = findViewById(R.id.dueAssmtAlertCbx);

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
      courseTitle = currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_NAME));

      setTitle( courseTitle + " Assessments");
      populateAssmtData();
    }else{
      setTitle("New Assessment");
    }

  }

  private void populateAssmtData(){
    currentAssmt = mDataSource.getAssmtByID(Integer.toString(assmtId));
    currentAssmt.moveToFirst();

    assmtTitle = currentAssmt.getString(currentAssmt.getColumnIndex(AssessmentTable.ASSESSMENT_NAME));

    startCbxSelection = currentAssmt.getInt(currentAssmt.getColumnIndex(AssessmentTable.ASSESSMENT_START_ALERT));
    dueCbxSelection = currentAssmt.getInt(currentAssmt.getColumnIndex(AssessmentTable.ASSESSMENT_DUE_ALERT));

    if(startCbxSelection == 1){
      startAssmtAlertCbx.setChecked(true);
    }else if(startCbxSelection == 0){
      startAssmtAlertCbx.setChecked(false);
    }

    if(dueCbxSelection == 1){
      dueAssmtAlertCbx.setChecked(true);
    }else if(dueCbxSelection == 0){
      dueAssmtAlertCbx.setChecked(false);
    }

    assmtTitleFld.setText(assmtTitle);
    dueDateFld.setText(currentAssmt.getString(currentAssmt.getColumnIndex(AssessmentTable.ASSESSMENT_DUE)));
    startAssmtDateFld.setText(currentAssmt.getString(currentAssmt.getColumnIndex(AssessmentTable.ASSESSMENT_START)));
    assessmentTypeSpinner.setSelection(currentAssmt.getInt(currentAssmt.getColumnIndex(AssessmentTable.ASSESSMENT_TYPE)));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId() ){
      case android.R.id.home:
        onBackPressed();
        return true;

      case R.id.deleteAssmtIcon:
        final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Confirm Delete");
        myAlertDialog.setMessage("Delete " + assmtTitle + " from " + courseTitle + "?");
        myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface arg0, int arg1) {
            mDataSource.deleteAssessment(Integer.toString(assmtId));
            Toast.makeText(EditAssmtActivity.this,
                    assmtTitle + " deleted", Toast.LENGTH_SHORT).show();
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

    if (startAssmtDateFld.getText().length() == 0) {

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

    String title = assmtTitleFld.getText().toString();
    String dueDate = dueDateFld.getText().toString();
    String startDate = startAssmtDateFld.getText().toString();

    if (isValidData()){
      Assessment assessment = new Assessment(title,dueDate,startDate,
              assessmentTypeSpinner.getSelectedItemPosition(),
              courseId, startCbxSelection, dueCbxSelection);

      if(isNewAssmt){
        mDataSource.insertAssessment(assessment);

        int assessmentId = mDataSource.getMaxAssessmentId();

        if(startAssmtAlertCbx.isChecked()){
          createNewAlert(courseId, assessmentId,
                    "Assessment " + title + " starts soon!",
                   title + " starts on " + startDate,
                          AlertTable.ALERT_ASSESSMENT_START,
                          LocalDateTime.of(chosenStartAlertDate, chosenStartAlertTime));
        }

        if (dueAssmtAlertCbx.isChecked()) {
          createNewAlert(courseId, assessmentId,
                    "Assessment " + title + " due soon!",
                  title + " due on " + dueDate,
                         AlertTable.ALERT_ASSESSMENT_END,
                         LocalDateTime.of(chosenDueAlertDate,chosenDueAlertTime));
        }

      }else{
        mDataSource.updateAssessment(assessment, Integer.toString(assmtId));

        if(startAssmtAlertCbx.isChecked()){
          createNewAlert(courseId, assmtId,
                  "Assessment " + title + " starts soon!",
                  title + " starts on " + startDate,
                  AlertTable.ALERT_ASSESSMENT_START,
                  LocalDateTime.of(chosenStartAlertDate, chosenStartAlertTime));
        }else{
          cancelAlert(AlertTable.ALERT_ASSESSMENT_START);
        }

        if (dueAssmtAlertCbx.isChecked()) {
          createNewAlert(courseId, assmtId,
                  "Assessment " + title + " due soon!",
                  title + " due on " + dueDate,
                  AlertTable.ALERT_ASSESSMENT_END,
                  LocalDateTime.of(chosenDueAlertDate,chosenDueAlertTime));
        }else{
          cancelAlert(AlertTable.ALERT_ASSESSMENT_END);
        }
      }

      setResult(RESULT_OK);
      finish();
    }

  }

  public void createNewAlert(int courseId, int assessmentId, String title, String content, String alertType, LocalDateTime chosenDateTime){

    //creating alert record and then retrieving its ID
    Alert alert = new Alert(courseId, alertType, assessmentId);
    mDataSource.insertAlert(alert);
    int alertId = mDataSource.getMaxAlertId();
    Log.d(TAG, "newAlertId: " + alertId + " localDateTime: " + chosenDateTime);

    ZonedDateTime zonedDateTime = chosenDateTime.atZone(ZoneId.systemDefault());
    long millis = zonedDateTime.toInstant().toEpochMilli();
    Log.d(TAG, "millis: " + millis);

    Intent intent = new Intent(EditAssmtActivity.this, NotificationReceiver.class);
    intent.putExtra("title",title);
    intent.putExtra("content", content);
    PendingIntent p1= PendingIntent.getBroadcast(getApplicationContext(), alertId, intent,0);
    AlarmManager a= (AlarmManager) getSystemService(ALARM_SERVICE);
    a.set(AlarmManager.RTC,millis,p1);
  }

  private void cancelAlert(String alertType) {
    Cursor alertCursor = mDataSource.getAlertByCourseAssmtAndType(
            Integer.toString(courseId), Integer.toString(assmtId), alertType);
    while(alertCursor.moveToNext()){
      int alertId = alertCursor.getInt(alertCursor.getColumnIndex(AlertTable.ALERT_ID));
      AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
      Intent myIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
      PendingIntent pendingIntent = PendingIntent.getBroadcast(
              getApplicationContext(), alertId, myIntent, 0);

      alarmManager.cancel(pendingIntent);
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

  public void setStartDate(View view) {
    isGoalPicker = true;
    DialogFragment datePicker = new DatePickerFragment();
    datePicker.show(getSupportFragmentManager(), "date picker");
  }

  @Override
  public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

    month++;
    LocalDate currentDate = LocalDate.of(year, month, dayOfMonth);
    String currentDateString = currentDate.format(dateFormat);

    Log.d(TAG, "isGoalPicker: " + isGoalPicker + " isStartCbx: " + isStartCbx +
            " isDueCbx: " + isDueCbx);

    if(isGoalPicker){
      startAssmtDateFld.setText(currentDateString);
    }else{
      dueDateFld.setText(currentDateString);
    }

    if(isStartCbx){
      chosenStartAlertDate = LocalDate.of(year,month,dayOfMonth);
      DialogFragment timePickerDialog = new TimePickerFragment();
      timePickerDialog.show(getSupportFragmentManager(), "timePicker");
    }

    if (isDueCbx) {
      chosenDueAlertDate = LocalDate.of(year,month,dayOfMonth);
      DialogFragment timePickerDialog = new TimePickerFragment();
      timePickerDialog.show(getSupportFragmentManager(), "timePicker");
    }

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (!isNewAssmt){
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.edit_assessment_menu, menu);
    }
    return true;
  }

  public void chooseAlertStart(View view) {
    if(startAssmtAlertCbx.isChecked()){
      startCbxSelection = 1;
      isStartCbx = true;
      DialogFragment datePicker = new DatePickerFragment();
      datePicker.show(getSupportFragmentManager(), "date picker");
    }else{
      startCbxSelection = 0;
      cancelAlert(AlertTable.ALERT_ASSESSMENT_START);
    }
  }

  public void chooseDueEnd(View view) {
    if(dueAssmtAlertCbx.isChecked()){
      dueCbxSelection = 1;
      isDueCbx = true;
      DialogFragment datePicker = new DatePickerFragment();
      datePicker.show(getSupportFragmentManager(), "date picker");
    }else{
      dueCbxSelection = 0;
      cancelAlert(AlertTable.ALERT_ASSESSMENT_END);
    }
  }

  @Override
  public void onTimeSet(TimePicker timePicker, int hour, int minute) {
    if(isStartCbx){
      chosenStartAlertTime = LocalTime.of(hour,minute);
      isStartCbx = false;
    }

    if(isDueCbx){
      chosenDueAlertTime = LocalTime.of(hour,minute);
      isDueCbx = false;
    }
  }
}
