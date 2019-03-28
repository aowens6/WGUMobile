package com.example.aowenswgumobile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.aowenswgumobile.database.AssessmentTable;
import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.TermsTable;

public class AssessmentsActivity extends AppCompatActivity {

  private DataSource mDataSource;
  private Bundle extras;
  private int courseId;
  private ListView assmtLV;
  private static final String TAG = "AssessmentsActivity";
  private static final int ASSMT_REQUEST_CODE = 1007;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_assessments);

    mDataSource = new DataSource(this);
    mDataSource.open();

    assmtLV = findViewById(R.id.assessmentsLV);

    Intent intent = getIntent();
    extras = intent.getExtras();

    setTitle("Assessments");

    if (extras != null) {
      courseId = extras.getInt("courseId");
      Log.d(TAG, "onCreate: courseId: " + courseId);
    }

    assmtLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(AssessmentsActivity.this, EditAssmtActivity.class);
        intent.putExtra("assmtId", ((int) id));
        intent.putExtra("courseId", courseId);
        startActivityForResult(intent, ASSMT_REQUEST_CODE);
      }
    });

    populateAssessmentLV();
  }

  public void populateAssessmentLV(){
    mDataSource.open();
    Cursor cursor = mDataSource.getAssessmentsByCourseId(Integer.toString(courseId));
    String[] from = {AssessmentTable.ASSESSMENT_NAME, AssessmentTable.ASSESSMENT_DUE};
    int[] to = {R.id.assmtNameTxt, R.id.assmtDueTxt};

    CursorAdapter assmtCursorAdapter = new SimpleCursorAdapter(this,
            R.layout.assessment_list_item, cursor, from, to, 0);

    assmtLV.setAdapter(assmtCursorAdapter);
    Log.d(TAG, "populateAssessmentLV: POPULATE CALLED");
  }

  public void addAssessment(View view) {
    Intent intent = new Intent(AssessmentsActivity.this, EditAssmtActivity.class);
    intent.putExtra("courseId", courseId);
    intent.putExtra("assmtId", 0);
    startActivityForResult(intent, ASSMT_REQUEST_CODE);
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ASSMT_REQUEST_CODE && resultCode == RESULT_OK) {
      Log.d(TAG, "onActivityResult: CALLING POPULATE");
      populateAssessmentLV();
    }
  }
}