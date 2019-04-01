package model;

import android.content.ContentValues;

import com.example.aowenswgumobile.database.NotesTable;

public class Note {

  private String noteText;
  private int courseId;

  public Note(String noteText, int courseId) {
    this.noteText = noteText;
    this.courseId = courseId;
  }

  public ContentValues toValues(){
    ContentValues values = new ContentValues();

    values.put(NotesTable.NOTE_TEXT, noteText);
    values.put(NotesTable.NOTE_COURSE_ID, courseId);

    return values;
  }
}
