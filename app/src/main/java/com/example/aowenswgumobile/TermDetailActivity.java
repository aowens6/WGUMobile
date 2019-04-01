package com.example.aowenswgumobile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.TermsTable;

public class TermDetailActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener{

  private DataSource mDataSource;
  private CursorAdapter termCourseCursorAdapter;
  private ListView termCourseList;
  private TextView startDate;
  private TextView endDate;
  private Cursor currentTerm;
  private int termId;
  private static final int TERM_REQUEST_CODE = 1002;
  private static final String TAG = "termActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_term_detail);

    mDataSource = new DataSource(this);
    mDataSource.open();

    Intent intent = getIntent();

    Uri uri = intent.getParcelableExtra(TermsTable.CONTENT_ITEM_TYPE);

    if(uri != null){
      termId = Integer.parseInt(uri.getLastPathSegment());
      Log.d(TAG, "uri: " + uri.toString());

      populateTermData();

      Log.d(TAG, "termTitle: " + currentTerm.getString(currentTerm.getColumnIndex(TermsTable.TERM_TITLE)));
    }

    populateTermCourseList();

    termCourseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(TermDetailActivity.this, CourseDetailActivity.class);
        intent.putExtra("courseId", ((int) id));
        intent.putExtra("termId", termId);
        startActivityForResult(intent, TERM_REQUEST_CODE);
      }
    });

  }

  private void populateTermData() {
    currentTerm = mDataSource.getTermById(Integer.toString(termId));
    currentTerm.moveToFirst();
    setTitle(currentTerm.getString(currentTerm.getColumnIndex(TermsTable.TERM_TITLE)));

    startDate = findViewById(R.id.startDate);
    startDate.setText(currentTerm.getString(currentTerm.getColumnIndex(TermsTable.TERM_START)));

    endDate = findViewById(R.id.endDate);
    endDate.setText(currentTerm.getString(currentTerm.getColumnIndex(TermsTable.TERM_END)));
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
    if(termCourseList.getAdapter().getCount() > 0){
      Toast.makeText(this, "Terms with courses can't be deleted", Toast.LENGTH_SHORT).show();
    }else{
      Log.d("termActivity", "lastURIsegment: " + termId);
      mDataSource.deleteTerm(termId);
      setResult(RESULT_OK);
      finish();
      Toast.makeText(this, "Term deleted", Toast.LENGTH_SHORT).show();
    }
  }


  public void addCourse(View view) {
    Intent intent = new Intent(TermDetailActivity.this, CourseDetailActivity.class);
    intent.putExtra("termId", termId);
    intent.putExtra("courseId", 0);
    startActivityForResult(intent, TERM_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == TERM_REQUEST_CODE && resultCode == RESULT_OK) {
      populateTermCourseList();
      populateTermData();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
      case R.id.editTermIcon:
        Intent intent = new Intent(TermDetailActivity.this, EditTermActivity.class);
        intent.putExtra("termId", termId);
        intent.putExtra("termName", currentTerm.getString(currentTerm.getColumnIndex(TermsTable.TERM_TITLE)));
        intent.putExtra("termStart", currentTerm.getString(currentTerm.getColumnIndex(TermsTable.TERM_START)));
        intent.putExtra("termEnd", currentTerm.getString(currentTerm.getColumnIndex(TermsTable.TERM_END)));
        startActivityForResult(intent, TERM_REQUEST_CODE);
        return true;
    }

    return false;
  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.term_detail_menu, menu);
    return true;
  }
}
