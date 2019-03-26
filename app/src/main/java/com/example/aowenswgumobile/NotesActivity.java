package com.example.aowenswgumobile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.NotesTable;
import com.example.aowenswgumobile.database.TermsTable;

public class NotesActivity extends AppCompatActivity {

  private DataSource mDataSource;
  private int courseId;
  private Cursor currentCourse;
  private static final String TAG = "NotesActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notes);

    mDataSource = new DataSource(this);
    mDataSource.open();

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra(NotesTable.CONTENT_ITEM_TYPE);

    if(uri != null){
      courseId = Integer.parseInt(uri.getLastPathSegment());
      currentCourse = mDataSource.getCourseById(Integer.toString(courseId));
      currentCourse.moveToFirst();
    }
    populateNotesList();
    setTitle("Notes for " + currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_NAME)));
  }

  public void populateNotesList(){
    mDataSource.open();
    Cursor cursor = mDataSource.getNotesByCourseId(Integer.toString(courseId));
    String[] from = {NotesTable.NOTE_TEXT};
    int[] to = {android.R.id.text1};

    CursorAdapter notesCursorAdapter = new SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_1, cursor, from, to, 0);

    ListView notesList = findViewById(R.id.notesLV);
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

  }
}
