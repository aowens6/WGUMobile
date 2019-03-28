package com.example.aowenswgumobile;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aowenswgumobile.database.DataSource;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

import model.DatePickerFragment;
import model.Term;

import static com.example.aowenswgumobile.MainActivity.dateFormat;

public class EditTermActivity extends AppCompatActivity
implements DatePickerDialog.OnDateSetListener{

  private DataSource mDataSource;
  private boolean isNewTerm;
  private boolean isStartDatePicker;
  private EditText titleTextFld;
  private TextView startTermDateFld;
  private TextView endTermDateFld;
  private int termId;
  private String termTitle;
  private String termStart;
  private String termEnd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_term);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    mDataSource = new DataSource(this);
    mDataSource.open();

    titleTextFld = findViewById(R.id.editTermTitleFld);
    startTermDateFld = findViewById(R.id.startTermFld);
    endTermDateFld = findViewById(R.id.endTermFld);

    Intent intent = getIntent();
    Bundle extras = intent.getExtras();

    if (extras != null) {
      termId = extras.getInt("termId");
      termTitle = extras.getString("termName");
      termStart = extras.getString("termStart");
      termEnd = extras.getString("termEnd");

      if(termId == 0 ){
        isNewTerm = true;
      }
    }

    if(isNewTerm){
      setTitle("New Term");
    }else{
      setTitle("Edit " + termTitle);
      titleTextFld.setText(termTitle);
      startTermDateFld.setText(termStart);
      endTermDateFld.setText(termEnd);
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

  public void setTermStart(View view) {
    isStartDatePicker = true;
    DialogFragment datePicker = new DatePickerFragment();
    datePicker.show(getSupportFragmentManager(), "date picker");
  }

  public void setTermEnd(View view) {
    isStartDatePicker = false;
    DialogFragment datePicker = new DatePickerFragment();
    datePicker.show(getSupportFragmentManager(), "date picker");
  }

  @Override
  public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
    month++;
    LocalDate currentDate = LocalDate.of(year, month, dayOfMonth);
    String currentDateString = currentDate.format(dateFormat);

    if(isStartDatePicker){
      TextView startCourseDate = findViewById(R.id.startTermFld);
      startCourseDate.setText(currentDateString);
    }else{
      TextView endCourseDate = findViewById(R.id.endTermFld);
      endCourseDate.setText(currentDateString);
    }
  }

  private boolean isValidData(){
    if (titleTextFld.getText().length() == 0) {
      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing term title");
      myAlertDialog.setMessage("Please add term title");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    if(startTermDateFld.getText().length() == 0){
      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing start date");
      myAlertDialog.setMessage("Please start date");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    if(endTermDateFld.getText().length() == 0){
      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing end date");
      myAlertDialog.setMessage("Please end date");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    return true;
  }

  public void saveTerm(View view) {
    if(isValidData()){
      Term term = new Term(titleTextFld.getText().toString(),
              startTermDateFld.getText().toString(),
              endTermDateFld.getText().toString());

      if (isNewTerm) {
        mDataSource.insertTerm(term);
      }else{
        mDataSource.updateTerm(Integer.toString(termId), term);
      }

      setResult(RESULT_OK);
      finish();
    }
  }

}
