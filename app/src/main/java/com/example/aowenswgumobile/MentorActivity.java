package com.example.aowenswgumobile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.MentorTable;
import com.example.aowenswgumobile.database.TermsTable;

public class MentorActivity extends AppCompatActivity {

  private DataSource mDataSource;
  private int courseId;
  private int mentorCode;
  private Cursor currentCourse;
  private static final String TAG = "MentorActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mentor);

    mDataSource = new DataSource(this);
    mDataSource.open();

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra(MentorTable.CONTENT_ITEM_TYPE);

    courseId = Integer.parseInt(uri.getLastPathSegment());

    currentCourse = mDataSource.getCourseById(Integer.toString(courseId));
    currentCourse.moveToFirst();

    mentorCode = currentCourse.getInt(currentCourse.getColumnIndex(CourseTable.COURSE_MENTOR_CODE));

    setTitle("Mentors for " + currentCourse.getString(currentCourse.getColumnIndex(CourseTable.COURSE_NAME)));

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

}
