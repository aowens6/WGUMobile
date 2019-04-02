package com.example.aowenswgumobile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.MentorTable;

public class MentorActivity extends AppCompatActivity {

  private DataSource mDataSource;
  private int courseId;
  private int mentorCode;
  private Cursor currentCourse;
  private ListView mentorList;
  private static final int MENTOR_REQUEST_CODE = 1009;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mentor);

    mDataSource = new DataSource(this);
    mDataSource.open();

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra(MentorTable.CONTENT_ITEM_TYPE);

    mentorList = findViewById(R.id.mentorList);

    courseId = Integer.parseInt(uri.getLastPathSegment());

    currentCourse = mDataSource.getCourseById(Integer.toString(courseId));
    currentCourse.moveToFirst();

    mentorCode = currentCourse.getInt(currentCourse.getColumnIndex(CourseTable.COURSE_MENTOR_CODE));

    setTitle("Mentors for " + currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_NAME)));

//    notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//      @Override
//      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//        Intent intent = new Intent(NotesActivity.this, EditNoteActivity.class);
//        intent.putExtra("noteId", id);
//        intent.putExtra("courseId", courseId);
//        startActivityForResult(intent, NOTE_REQUEST_CODE);
//
//      }
//    });

    mentorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(MentorActivity.this, EditMentorActivity.class);
        intent.putExtra("mentorId", ((int) id));
        intent.putExtra("courseId", courseId);
        intent.putExtra("mentorCode", mentorCode);
        startActivityForResult(intent, MENTOR_REQUEST_CODE);
      }
    });

    populateMentorLV();

  }

  public void populateMentorLV(){
    Cursor cursor = mDataSource.getMentorsByCode(Integer.toString(mentorCode));
    String[] from = {MentorTable.MENTOR_NAME, MentorTable.MENTOR_PHONE, MentorTable.MENTOR_EMAIL};
    int[] to = {R.id.mentorName, R.id.mentorPhone, R.id.mentorEmail};

    CursorAdapter mentorCursorAdapter = new SimpleCursorAdapter(this, R.layout.mentor_list_item, cursor,from, to,0);

    ListView mentorList = findViewById(R.id.mentorList);
    mentorList.setAdapter(mentorCursorAdapter);
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

  public void addMentor(View view) {
    Intent intent = new Intent(MentorActivity.this, EditMentorActivity.class);
    intent.putExtra("mentorId", 0);
    intent.putExtra("courseId", courseId);
    intent.putExtra("mentorCode", mentorCode);
    startActivityForResult(intent, MENTOR_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == MENTOR_REQUEST_CODE && resultCode == RESULT_OK) {
      populateMentorLV();
    }
  }
}
