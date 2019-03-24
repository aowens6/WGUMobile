package com.example.aowenswgumobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.Course;
import model.Term;

public class DataSource {

  private Context mContext;

  private SQLiteDatabase mDatabase;
  SQLiteOpenHelper mDbHelper;

  public DataSource(Context context) {
    this.mContext = context;
    mDbHelper = new DBHelper(mContext);
    mDatabase = mDbHelper.getWritableDatabase();
  }

  public Cursor getAllTerms(){
    Cursor c = mDatabase.query(TermsTable.TABLE_TERMS, TermsTable.ALL_TERM_COLUMNS,
            null, null, null, null, null);

    return c;
  }

  public Cursor getTermById(String termId){
    Cursor c = mDatabase.query(TermsTable.TABLE_TERMS, TermsTable.ALL_TERM_COLUMNS,
            TermsTable.TERM_ID + "=" + termId, null,null,null,null);
    return c;
  }

  public Term createTerm(Term term){
    ContentValues values = term.toValues();
    mDatabase.insert(TermsTable.TABLE_TERMS, null, values);
    return term;
  }

  public long getTermsCount(){
    return DatabaseUtils.queryNumEntries(mDatabase, TermsTable.TABLE_TERMS);
  }

  public void deleteTerm(int termId){
    mDatabase.delete(TermsTable.TABLE_TERMS, TermsTable.TERM_ID + "=" + termId, null);
  }

  public Cursor getUnfinishedCourses(){
    Cursor c = mDatabase.query(CourseTable.TABLE_COURSES, CourseTable.ALL_COURSE_COLUMNS,
            CourseTable.COURSE_TERM_ID + "=0",null,null,null,null);
    return c;
  }

  public Cursor getCoursesByTerm(String termId){
    Cursor c = mDatabase.query(CourseTable.TABLE_COURSES, CourseTable.ALL_COURSE_COLUMNS,
            CourseTable.COURSE_TERM_ID + "=" + termId,null, null,null,null);

    return c;
  }

  public void createCourse(Course course){
    ContentValues values = course.toValues();
    mDatabase.insert(CourseTable.TABLE_COURSES, null, values);
  }

  public void insertCourse(String courseName, int termId){
    ContentValues values = new ContentValues();
    values.put(CourseTable.COURSE_NAME, courseName);
    values.put(CourseTable.COURSE_TERM_ID, termId);
    mDatabase.insert(CourseTable.TABLE_COURSES,null, values);
  }

  public void updateCourse(String courseId, int termId){
    ContentValues values = new ContentValues();
    values.put(CourseTable.COURSE_TERM_ID, termId);
    mDatabase.update(CourseTable.TABLE_COURSES, values,
            CourseTable.COURSE_ID + "=" + courseId, null);
  }

  public void initializeCourses(){
    insertCourse("Organization",0);
    insertCourse("SQL",0);
    insertCourse("UX/UI",0);
    insertCourse("Software Engineering",0);
    insertCourse("Business",0);
    insertCourse("IT Applications",0);
    insertCourse("Software 1",1);
    insertCourse("Software 2",1);
    insertCourse("Mobile Apps",1);
    insertCourse("Capstone",1);
    insertCourse("IT Fundamentals",2);
  }

  public void open(){
    mDatabase = mDbHelper.getWritableDatabase();
  }

  public void close(){
    mDbHelper.close();
  }
}
