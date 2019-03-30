package com.example.aowenswgumobile.database;

import android.net.Uri;

public class AlertTable {

  private static final String AUTHORITY = "com.example.aowenswgumobile";
  private static final String BASE_PATH = "alerts";
  public static final Uri ALERT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

  public static final String TABLE_ALERTS = "alerts";
  public static final String ALERT_ID = "_id";
  public static final String ALERT_COURSE_ID = "courseId";
  public static final String ALERT_TYPECODE = "typeCode";

  public static final String ALERT_COURSE_START = "startCourse";
  public static final String ALERT_COURSE_END = "endCourse";
  public static final String ALERT_ASSESSMENT_START = "startAssessment";
  public static final String ALERT_ASSESSMENT_END = "endAssessment";

  public static final String[] ALL_ALERT_COLUMNS =
          {ALERT_ID, ALERT_COURSE_ID, ALERT_TYPECODE};

  public static final String TABLE_CREATE_ALERTS =
          "CREATE TABLE " + TABLE_ALERTS + " (" +
                  ALERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                  ALERT_COURSE_ID + " INTEGER," +
                  ALERT_TYPECODE + " TEXT" +
                  ")";

}
