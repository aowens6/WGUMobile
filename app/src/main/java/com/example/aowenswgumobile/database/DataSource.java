package com.example.aowenswgumobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import model.Course;
import model.Term;

public class DataSource {

  private Context mContext;

  private SQLiteDatabase mDatabase;
  SQLiteOpenHelper mDbHelper;

  public static ArrayList<Course> courses = new ArrayList<>();

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

  public int getTermsCount(){
    return (int) DatabaseUtils.queryNumEntries(mDatabase, TermsTable.TABLE_TERMS);
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

  public void insertCourse(Course course){
    ContentValues values = course.toValues();
    mDatabase.insert(CourseTable.TABLE_COURSES, null, values);
  }

  public void updateCourse(String courseId, int termId){
    ContentValues values = new ContentValues();
    values.put(CourseTable.COURSE_TERM_ID, termId);
    mDatabase.update(CourseTable.TABLE_COURSES, values,
            CourseTable.COURSE_ID + "=" + courseId, null);
  }

  public void updateCourseStart(String courseId, String startDate){
    ContentValues values = new ContentValues();
    values.put(CourseTable.COURSE_START, startDate);
    mDatabase.update(CourseTable.TABLE_COURSES, values,
            CourseTable.COURSE_ID + "=" + courseId, null);
  }

  public void updateCourseEnd(String courseId, String endDate){
    ContentValues values = new ContentValues();
    values.put(CourseTable.COURSE_END, endDate);
    mDatabase.update(CourseTable.TABLE_COURSES, values,
            CourseTable.COURSE_ID + "=" + courseId, null);
  }

  public void updateCourseStatus(String courseId, int statusCode){
    ContentValues values = new ContentValues();
    values.put(CourseTable.COURSE_STATUS_CODE, statusCode);
    mDatabase.update(CourseTable.TABLE_COURSES, values,
            CourseTable.COURSE_ID + "=" + courseId, null);
  }

  public Cursor getCourseById(String courseId){
    Cursor c = mDatabase.query(CourseTable.TABLE_COURSES, CourseTable.ALL_COURSE_COLUMNS,
            CourseTable.COURSE_ID + "=" + courseId,null,null,null,null);

    return c;
  }

  public void initializeCourses(){
//    in progress, completed, dropped, plan to take
    Course course1 = new Course("Organization", "", "", 1, 0, 1);
    Course course2 = new Course("SQL", "", "", 1, 0,1);
    Course course3 = new Course("UX/UI", "", "", 2, 0,2);
    Course course4 = new Course("Software Engineering", "", "", 2, 0,3);
    Course course5 = new Course("Business", "", "", 3, 0,3);
    Course course6 = new Course("IT Applications", "", "", 3, 1,4);
    Course course7 = new Course("Software 1", "", "", 4, 1,4);
    Course course8 = new Course("Software 2", "", "", 4, 1,5);
    Course course9 = new Course("Mobile Apps", "", "", 2, 1,5);
    Course course10 = new Course("Capstone", "", "", 3, 2,5);
    Course course11 = new Course("IT Fundamentals", "", "", 1, 3,5);

    courses.add(course1);
    courses.add(course2);
    courses.add(course3);
    courses.add(course4);
    courses.add(course5);
    courses.add(course6);
    courses.add(course7);
    courses.add(course8);
    courses.add(course9);
    courses.add(course10);
    courses.add(course11);

    for (Course course : courses){
      insertCourse(course);
    }
  }

  public void open(){
    mDatabase = mDbHelper.getWritableDatabase();
  }

  public void close(){
    mDbHelper.close();
  }
}
