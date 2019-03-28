package com.example.aowenswgumobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

  public static final String DB_FILE_NAME = "student.db";
  public static final int DB_VERSION = 1;

  public DBHelper(@Nullable Context context) {
    super(context, DB_FILE_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(TermsTable.TABLE_CREATE_TERMS);
    db.execSQL(CourseTable.TABLE_CREATE_COURSES);
    db.execSQL(MentorTable.TABLE_CREATE_MENTORS);
    db.execSQL(NotesTable.TABLE_CREATE_NOTES);
    db.execSQL(AssessmentTable.TABLE_CREATE_ASSESSMENTS);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    db.execSQL("DROP TABLE IF EXISTS " + TermsTable.TABLE_TERMS);
    db.execSQL("DROP TABLE IF EXISTS " + CourseTable.TABLE_COURSES);
    db.execSQL("DROP TABLE IF EXISTS " + MentorTable.TABLE_MENTORS);
    db.execSQL("DROP TABLE IF EXISTS " + NotesTable.TABLE_NOTES);
    db.execSQL("DROP TABLE IF EXISTS " + AssessmentTable.TABLE_ASSESSMENTS);
    onCreate(db);
  }

}
