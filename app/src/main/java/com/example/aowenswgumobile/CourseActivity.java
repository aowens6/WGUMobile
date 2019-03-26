package com.example.aowenswgumobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toolbar;

import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.TermsTable;

public class CourseActivity extends AppCompatActivity {

  DataSource mDataSource;
  CursorAdapter courseCursorAdapter;
  ListView courseList;
  int termId;
  private static final int COURSE_REQUEST_CODE = 1003;
  public static final String TAG = "CourseActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course);

    mDataSource = new DataSource(this);
    mDataSource.open();

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra(TermsTable.CONTENT_ITEM_TYPE);

    if(uri != null){
      termId = Integer.parseInt(uri.getLastPathSegment());
      Log.d(TAG, "uri: " + uri.toString());
    }

    populateCourseLV();
    setTitle("Pending Courses");

    courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d(TAG, "pos: " + position + " id = " + id);
        mDataSource.updateCourse(Long.toString(id), termId);
        populateCourseLV();
      }
    });
  }

  public void populateCourseLV(){
    mDataSource.open();
    Cursor cursor = mDataSource.getUnfinishedCourses();
    String[] from = {CourseTable.COURSE_NAME};
    int[] to = {android.R.id.text1};

    courseCursorAdapter = new SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_1, cursor, from, to, 0);

    courseList = findViewById(R.id.courseList);
    courseList.setAdapter(courseCursorAdapter);
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
//    Intent intent = new Intent(CourseActivity.this, TermActivity.class);
//    Uri uri = Uri.parse(TermsTable.TERM_CONTENT_URI + "/" + termId);
//    Intent intent = getIntent();
//    Uri uri = intent.getParcelableExtra(CourseTable.CONTENT_ITEM_TYPE);
//    Log.d(TAG, "uri: " + uri.toString());
//    intent.putExtra("termId",termId);
  }
}
