package com.example.aowenswgumobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.TermsTable;

import java.time.format.DateTimeFormatter;

public class TermActivity extends AppCompatActivity {

  private DataSource mDataSource;
  private CursorAdapter termCursorAdapter;
  private ListView termList;
  private static final int TERM_REQUEST_CODE = 1001;
  private static final String TAG = "TermActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_term);

    mDataSource = new DataSource(this);
    mDataSource.open();

    termList = findViewById(R.id.termList);

    populateTermLV();

    termList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(TermActivity.this, TermDetailActivity.class);
        Uri uri = Uri.parse(TermsTable.TERM_CONTENT_URI + "/" + id);
        intent.putExtra(TermsTable.CONTENT_ITEM_TYPE,uri);
        startActivityForResult(intent, TERM_REQUEST_CODE);
      }
    });

    setTitle("Terms");

  }

  public void populateTermLV(){
    mDataSource.open();

    Cursor cursor = mDataSource.getAllTerms();
    String[] from = {TermsTable.TERM_TITLE, TermsTable.TERM_START, TermsTable.TERM_END};
    int[] to = {R.id.termTitle, R.id.startDateListTxt, R.id.endDateListTxt};

    termCursorAdapter = new SimpleCursorAdapter(this,
            R.layout.term_list_item, cursor, from, to, 0);

    termList.setAdapter(termCursorAdapter);
  }


  public void addTerm(View view) {
    Intent intent = new Intent(TermActivity.this, EditTermActivity.class);
    intent.putExtra("termId", 0);
    startActivityForResult(intent, TERM_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == TERM_REQUEST_CODE && resultCode == RESULT_OK) {
      populateTermLV();
    }
  }
}
