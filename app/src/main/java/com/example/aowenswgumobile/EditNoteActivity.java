package com.example.aowenswgumobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aowenswgumobile.database.DataSource;
import com.example.aowenswgumobile.database.NotesTable;

import model.Note;

public class EditNoteActivity extends AppCompatActivity {

  private DataSource mDataSource;
  private String noteText;
  private int noteId;
  private int courseId;
  private boolean newNote;
  private EditText noteTextFld;
  private FloatingActionButton deleteNoteFAB;
  private static final String TAG = "EditNotesActivity";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_note);

    mDataSource = new DataSource(this);
    mDataSource.open();

    noteTextFld = findViewById(R.id.noteTextFld);
    deleteNoteFAB = findViewById(R.id.deleteNoteFAB);

    Intent intent = getIntent();
    Bundle extras = intent.getExtras();

    if (extras != null) {
      noteId = (int) extras.getLong("noteId");
      courseId = extras.getInt("courseId");
      if(noteId == 0){
        newNote = true;
      }
      Log.d(TAG, "noteId: " + noteId + " courseId: " + courseId);
    }

    if(newNote){
      setTitle("New Note");
      deleteNoteFAB.hide();
    }else{
      setTitle("Edit Note");
      Cursor currentNote = mDataSource.getNoteByNoteId(Integer.toString(noteId));
      currentNote.moveToFirst();
      noteTextFld.setText(currentNote.getString(currentNote.getColumnIndex(NotesTable.NOTE_TEXT)));
      noteTextFld.setSelection(noteTextFld.length());
    }

//    Log.d(TAG, "onCreate: newNote" + courseUri.toString());
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

  public void saveNote(View view) {

    if(noteTextFld.getText().length() > 0){
      if(newNote){
        Note newNote = new Note(noteTextFld.getText().toString(), courseId);
        mDataSource.insertNote(newNote);
      }else{
        mDataSource.updateNote(noteTextFld.getText().toString(), Integer.toString(noteId));
      }

    }else{
      mDataSource.deleteNote(Integer.toString(noteId));
    }

    setResult(RESULT_OK);
    finish();
  }

  public void deleteNote(View view) {
    final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
    myAlertDialog.setTitle("Confirm Delete");
    myAlertDialog.setMessage("Are you sure you want to delete this note?");
    myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

      public void onClick(DialogInterface arg0, int arg1) {
        mDataSource.deleteNote(Integer.toString(noteId));
        Toast.makeText(EditNoteActivity.this,
                "Note deleted", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);
        finish();
      }
    });

    myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

      public void onClick(DialogInterface arg0, int arg1) {
        return;
      }
    });

    myAlertDialog.show();

  }
}
