package model;

import android.content.ContentValues;

import com.example.aowenswgumobile.database.NotesTable;
import com.example.aowenswgumobile.database.TermsTable;

public class Note {

  private String noteText;
  private int courseId;

  public Note() {
  }

  public Note(String noteText) {
    this.noteText = noteText;
  }

  public String getNoteText() {
    return noteText;
  }

  public void setNoteText(String noteText) {
    this.noteText = noteText;
  }

  public int getCourseId() {
    return courseId;
  }

  public void setCourseId(int courseId) {
    this.courseId = courseId;
  }

  public ContentValues toValues(){
    ContentValues values = new ContentValues();

    values.put(NotesTable.NOTE_TEXT, noteText);
    values.put(NotesTable.NOTE_COURSE_ID, courseId);

    return values;
  }
}
