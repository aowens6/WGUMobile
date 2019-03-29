package com.example.aowenswgumobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.NotesTable;
import com.example.aowenswgumobile.database.TermsTable;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

  private DataSource mDataSource;
  private int courseId;
  private Cursor currentCourse;
  private ListView notesList;
  private EditText mEmail;
  private String courseName;
  private ArrayList<String> notesArray = new ArrayList<>();
  private FloatingActionButton emailNotesFAB;
  private static final String TAG = "NotesActivity";
  private static final int NOTE_REQUEST_CODE = 1006;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notes);

    mDataSource = new DataSource(this);
    mDataSource.open();

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra(NotesTable.CONTENT_ITEM_TYPE);
    emailNotesFAB = findViewById(R.id.emailNoteFAB);

    if(uri != null){
      courseId = Integer.parseInt(uri.getLastPathSegment());
      currentCourse = mDataSource.getCourseById(Integer.toString(courseId));
      currentCourse.moveToFirst();
      courseName = currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_NAME));
    }
    populateNotesList();

    notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(NotesActivity.this, EditNoteActivity.class);
        intent.putExtra("noteId", id);
        intent.putExtra("courseId", courseId);
        startActivityForResult(intent, NOTE_REQUEST_CODE);

      }
    });

    setTitle("Notes for " + courseName );
  }

  public void populateNotesList(){
    mDataSource.open();
    Cursor cursor = mDataSource.getNotesByCourseId(Integer.toString(courseId));

    while(cursor.moveToNext()){
      notesArray.add(cursor.getString(cursor.getColumnIndex(NotesTable.NOTE_TEXT)));
    }

    if(cursor.getCount() == 0){
      emailNotesFAB.hide();
    } else{
      emailNotesFAB.show();
    }

    String[] from = {NotesTable.NOTE_TEXT};
    int[] to = {android.R.id.text1};

    CursorAdapter notesCursorAdapter = new SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_1, cursor, from, to, 0);


    notesList = findViewById(R.id.notesLV);
    notesList.setAdapter(notesCursorAdapter);
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

  public void addNote(View view) {
    Intent intent = new Intent(NotesActivity.this, EditNoteActivity.class);
    intent.putExtra("noteId", 0);
    intent.putExtra("courseId", courseId);
    startActivityForResult(intent, NOTE_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == NOTE_REQUEST_CODE && resultCode == RESULT_OK) {
      populateNotesList();
    }
  }

  public void emailNotes(View view) {
    AlertDialog.Builder mBuilder = new AlertDialog.Builder(NotesActivity.this);
    View mView = getLayoutInflater().inflate(R.layout.email_form, null);

    mEmail = mView.findViewById(R.id.emailFld);
    Button sendBtn = mView.findViewById(R.id.sendNotesBtn);

    mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    });

    mBuilder.setView(mView);
    final AlertDialog dialog = mBuilder.create();
    dialog.show();
    sendBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(!mEmail.getText().toString().isEmpty()){
          if(isEmailValid(mEmail.getText().toString())){
            sendEmail();
            dialog.dismiss();
          }else{
            Toast toast = Toast.makeText(NotesActivity.this, "Invalid Email", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.TOP,0, 0);
            toast.show();
          }

        }
      }
    });

  }

  boolean isEmailValid(CharSequence email) {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches();
  }

  private void sendEmail(){

    String recipientsList = mEmail.getText().toString();
    String[] recipients = recipientsList.split(",");

    StringBuilder notesBuilder = new StringBuilder();

    for(String noteText: notesArray){
      notesBuilder.append(noteText + "\n\n");
    }

    String notesString = notesBuilder.toString();

    Intent i = new Intent(Intent.ACTION_SEND);
    i.setType("message/rfc822");
    i.putExtra(Intent.EXTRA_EMAIL  , recipients);
    i.putExtra(Intent.EXTRA_SUBJECT, "Notes for " + courseName);
    i.putExtra(Intent.EXTRA_TEXT, notesString);
    i.setType("text/*");

    try {
      startActivity(Intent.createChooser(i, "Send Notes"));
    } catch (android.content.ActivityNotFoundException ex) {
      Toast.makeText(NotesActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
    }
  }
}
