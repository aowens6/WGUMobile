package com.example.aowenswgumobile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.TermsTable;

public class TermActivity extends AppCompatActivity {

  DataSource mDataSource;
  CursorAdapter termCourseCursorAdapter;
  ListView termCourseList;
  private Button deleteTermBtn;
  private int termId;
  private static final int TERM_REQUEST_CODE = 1002;
  private static final String TAG = "termActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_term);

    mDataSource = new DataSource(this);
    mDataSource.open();

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra(TermsTable.CONTENT_ITEM_TYPE);

    if(uri != null){
      termId = Integer.parseInt(uri.getLastPathSegment());
      Log.d(TAG, "uri: " + uri.toString());
    }

    populateTermCourseList();

    termCourseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d(TAG, "pos: " + position + " id = " + id);
      }
    });

    deleteTermBtn = findViewById(R.id.deleteTermBtn);

    if(termCourseList.getAdapter().getCount() == 0){
      deleteTermBtn.setVisibility(View.VISIBLE);
    }else{
      deleteTermBtn.setVisibility(View.GONE);
    }
  }

  public void populateTermCourseList(){
    mDataSource.open();
    Cursor cursor = mDataSource.getCoursesByTerm(Integer.toString(termId));
    String[] from = {CourseTable.COURSE_NAME};
    int[] to = {android.R.id.text1};

    termCourseCursorAdapter = new SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_1, cursor, from, to, 0);

    termCourseList = findViewById(R.id.termCourseList);
    termCourseList.setAdapter(termCourseCursorAdapter);
  }

  public void deleteTerm(View view) {
    Log.d("termActivity", "lastURIsegment: " + termId);
    mDataSource.deleteTerm(termId);
    setResult(RESULT_OK);
    finish();
    Toast.makeText(this, "Term deleted", Toast.LENGTH_SHORT).show();
  }


  public void addCourse(View view) {
    Intent intent = new Intent(TermActivity.this, CourseActivity.class);
    Uri uri = Uri.parse(TermsTable.TERM_CONTENT_URI + "/" + termId);
    intent.putExtra(TermsTable.CONTENT_ITEM_TYPE,uri);
    startActivityForResult(intent, TERM_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == TERM_REQUEST_CODE && resultCode == RESULT_OK) {
      populateTermCourseList();
    }
  }
}
