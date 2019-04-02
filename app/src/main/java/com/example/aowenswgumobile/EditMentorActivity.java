package com.example.aowenswgumobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.MentorTable;

import model.Mentor;

public class EditMentorActivity extends AppCompatActivity {

  private DataSource mDataSource;
  private Bundle extras;
  private int courseId;
  private int mentorId;
  private int mentorCode;
  private boolean isNewMentor;
  private Cursor mentorCursor;
  private EditText mentorNameFld;
  private EditText phoneFld;
  private EditText emailFld;
  private FloatingActionButton deleteMentorFAB;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_mentor);

    mDataSource = new DataSource(this);
    mDataSource.open();

    mentorNameFld = findViewById(R.id.mentorNameFld);
    phoneFld = findViewById(R.id.phoneFld);
    emailFld = findViewById(R.id.emailFld);
    deleteMentorFAB = findViewById(R.id.deleteMentorFAB);

    Intent intent = getIntent();

    extras = intent.getExtras();

    if(extras != null){
      courseId = extras.getInt("courseId");
      mentorId = extras.getInt("mentorId");
      mentorCode = extras.getInt("mentorCode");

      if(mentorId == 0){
        isNewMentor = true;
        setTitle("New mentor");
        deleteMentorFAB.hide();
      }else{
        isNewMentor = false;
        mentorCursor = mDataSource.getMentorById(Integer.toString(mentorId));
        mentorCursor.moveToFirst();
        mentorNameFld.setText(mentorCursor.getString(mentorCursor.getColumnIndex(MentorTable.MENTOR_NAME)));
        phoneFld.setText(mentorCursor.getString(mentorCursor.getColumnIndex(MentorTable.MENTOR_PHONE)));
        emailFld.setText(mentorCursor.getString(mentorCursor.getColumnIndex(MentorTable.MENTOR_EMAIL)));
        setTitle("Edit Mentor");
      }

    }

  }

  private boolean isValidData(){
    if(mentorNameFld.getText().length() == 0){
      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing mentor's name");
      myAlertDialog.setMessage("Please add mentor's name");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    if(phoneFld.getText().length() == 0){
      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing mentor's phone");
      myAlertDialog.setMessage("Please add mentor's phone");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    if(emailFld.getText().length() == 0){
      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Missing mentor's email");
      myAlertDialog.setMessage("Please add mentor's email");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    if(!isEmailValid(emailFld.getText().toString())){

      final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Email does not match email pattern");
      myAlertDialog.setMessage("Email must contain @ and a web address");
      myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {return;}});

      myAlertDialog.show();

      return false;
    }

    return true;
  }

  public void saveMentor(View view) {

    if(isValidData()){

      Mentor mentor = new Mentor(mentorNameFld.getText().toString(),
              phoneFld.getText().toString(),
              emailFld.getText().toString(),
              mentorCode);

      if (isNewMentor) {
        mDataSource.insertMentor(mentor);
      }else{
        mDataSource.updateMentor(mentor, Integer.toString(mentorId));
      }

      setResult(RESULT_OK);
      finish();
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

  public void deleteMentor(View view) {
    final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
    myAlertDialog.setTitle("Confirm Delete");
    myAlertDialog.setMessage("Are you sure you want to delete this mentor?");
    myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

      public void onClick(DialogInterface arg0, int arg1) {
        mDataSource.deleteMentor(Integer.toString(mentorId));
        Toast.makeText(EditMentorActivity.this,
                "Mentor deleted", Toast.LENGTH_SHORT).show();

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

  }

  boolean isEmailValid(CharSequence email) {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches();
  }
}
