package com.example.aowenswgumobile.database;

import android.net.Uri;

public class CourseTable {

  private static final String AUTHORITY = "com.example.aowenswgumobile";
  private static final String BASE_PATH = "courses";
  public static final Uri COURSE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

  public static final String TABLE_COURSES = "courses";
  public static final String COURSE_ID = "_id";
  public static final String COURSE_NAME = "courseName";
  public static final String COURSE_START = "startDate";
  public static final String COURSE_END = "endDate";
  public static final String COURSE_STATUS = "status";
  public static final String COURSE_TERM_ID = "termId";

  public static final String[] ALL_COURSE_COLUMNS =
          {COURSE_ID, COURSE_NAME, COURSE_START, COURSE_END,COURSE_STATUS,COURSE_TERM_ID};

  public static final String CONTENT_ITEM_TYPE = "Course";

  public static final String TABLE_CREATE_COURSES=
          "CREATE TABLE " + TABLE_COURSES + " (" +
            COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COURSE_NAME + " TEXT, " +
            COURSE_STATUS + " TEXT," +
            COURSE_START + " TEXT," +
            COURSE_END + " TEXT," +
            COURSE_TERM_ID + " INTEGER" +
            ")";

}
