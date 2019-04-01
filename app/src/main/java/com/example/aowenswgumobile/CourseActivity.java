package com.example.aowenswgumobile;

import android.content.Intent;
import android.database.Cursor;
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

public class CourseActivity extends AppCompatActivity {

  private DataSource mDataSource;
  private CursorAdapter courseCursorAdapter;
  private ListView courseList;
  private Bundle extras;
  private int termId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course);

    mDataSource = new DataSource(this);
    mDataSource.open();

    Intent intent = getIntent();

    extras = intent.getExtras();
    if (extras != null) {
      termId = extras.getInt("termId");
    }

    populateCourseLV();
    setTitle("Pending Courses");

    courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        mDataSource.updateCourseTerm(Long.toString(id), termId);
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
  }
}
