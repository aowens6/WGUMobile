package com.example.aowenswgumobile.database;

import android.net.Uri;

public class NotesTable {

  private static final String AUTHORITY = "com.example.aowenswgumobile";
  private static final String BASE_PATH = "terms";
  public static final Uri NOTES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

  public static final String TABLE_NOTES = "notes";
  public static final String NOTE_ID = "_id";
  public static final String NOTE_TEXT = "noteText";
  public static final String NOTE_COURSE_ID = "courseId";

  public static final String[] ALL_NOTES_COLUMNS = {NOTE_ID, NOTE_TEXT, NOTE_COURSE_ID};

  public static final String CONTENT_ITEM_TYPE = "Note";

  public static final String TABLE_CREATE_NOTES =
    "CREATE TABLE " + TABLE_NOTES + " (" +
    NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
    NOTE_TEXT + " TEXT, " +
    NOTE_COURSE_ID + " INTEGER " +
    ")";


}
