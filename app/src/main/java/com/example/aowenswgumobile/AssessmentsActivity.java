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
import android.widget.Toast;

import com.example.aowenswgumobile.database.DataSource;

import model.AssessmentCursorAdapter;

public class AssessmentsActivity extends AppCompatActivity {

  private DataSource mDataSource;
  private Bundle extras;
  private int courseId;
  private ListView assmtLV;
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

    CursorAdapter assmtCursorAdapter = new AssessmentCursorAdapter(this, cursor, 0);

    assmtLV.setAdapter(assmtCursorAdapter);
  }

  public void addAssessment(View view) {
    int assessmentCount = mDataSource.getAssessmentCount();
    if(assessmentCount < 5){
      Intent intent = new Intent(AssessmentsActivity.this, EditAssmtActivity.class);
      intent.putExtra("courseId", courseId);
      intent.putExtra("assmtId", 0);
      startActivityForResult(intent, ASSMT_REQUEST_CODE);
    } else{
      Toast.makeText(this, "Limit 5 assessments per course", Toast.LENGTH_SHORT).show();
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ASSMT_REQUEST_CODE && resultCode == RESULT_OK) {
      populateAssessmentLV();
    }
  }
}
