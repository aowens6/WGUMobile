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
  public static final String COURSE_STATUS_CODE = "status";
  public static final String COURSE_TERM_ID = "termId";
  public static final String COURSE_MENTOR_CODE = "mentorCode";
  public static final String COURSE_START_ALERT = "startAlert";
  public static final String COURSE_END_ALERT = "endAlert";

  public static final String[] ALL_COURSE_COLUMNS =
          {COURSE_ID, COURSE_NAME, COURSE_START,
                  COURSE_END,COURSE_STATUS_CODE,COURSE_TERM_ID,
                  COURSE_MENTOR_CODE, COURSE_START_ALERT, COURSE_END_ALERT};

  public static final String CONTENT_ITEM_TYPE = "Course";

  public static final String TABLE_CREATE_COURSES=
          "CREATE TABLE " + TABLE_COURSES + " (" +
            COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COURSE_NAME + " TEXT, " +
            COURSE_STATUS_CODE + " INTEGER," +
            COURSE_START + " TEXT," +
            COURSE_END + " TEXT," +
            COURSE_TERM_ID + " INTEGER," +
            COURSE_MENTOR_CODE + " INTEGER," +
            COURSE_START_ALERT + " INTEGER," +
            COURSE_END_ALERT + " INTEGER" +
            ")";

}
