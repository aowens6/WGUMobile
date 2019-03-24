package com.example.aowenswgumobile;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.aowenswgumobile.database.DBHelper;
import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.TermsTable;

import model.Term;

public class MainActivity extends AppCompatActivity {

  DataSource mDataSource;
  CursorAdapter termCursorAdapter;
  ListView termList;
  private static final int MAIN_REQUEST_CODE = 1001;
  public static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mDataSource = new DataSource(this);
    mDataSource.open();

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    if(!prefs.getBoolean("firstTime", false)) {
      mDataSource.initializeCourses();
      SharedPreferences.Editor editor = prefs.edit();
      editor.putBoolean("firstTime", true);
      editor.commit();
    }

    populateTermLV();

    termList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, TermActivity.class);
        Uri uri = Uri.parse(TermsTable.TERM_CONTENT_URI + "/" + id);
        intent.putExtra(TermsTable.CONTENT_ITEM_TYPE,uri);
        startActivityForResult(intent, MAIN_REQUEST_CODE);
      }
    });

  }

  public void populateTermLV(){
    mDataSource.open();

    Cursor cursor = mDataSource.getAllTerms();
    String[] from = {TermsTable.TERM_TITLE};
    int[] to = {android.R.id.text1};

    termCursorAdapter = new SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_1, cursor, from, to, 0);

    termList = findViewById(R.id.termList);
    termList.setAdapter(termCursorAdapter);
  }

  @Override
  protected void onPause() {
    super.onPause();
    mDataSource.close();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mDataSource.open();
  }

  public void addTerm(View view) {
    Term term = new Term("Term " + (mDataSource.getTermsCount() + 1), "", "");
    mDataSource.createTerm(term);
    Log.d("MainActivity", "databaseCount: " + mDataSource.getTermsCount());
    populateTermLV();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == MAIN_REQUEST_CODE && resultCode == RESULT_OK) {
      populateTermLV();
    }
  }
}
