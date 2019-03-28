package com.example.aowenswgumobile.database;

import android.net.Uri;

public class AssessmentTable {

  private static final String AUTHORITY = "com.example.aowenswgumobile";
  private static final String BASE_PATH = "assessments";
  public static final Uri ASSESSMENT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

  public static final String TABLE_ASSESSMENTS = "assessments";
  public static final String ASSESSMENT_ID = "_id";
  public static final String ASSESSMENT_NAME = "assessmentName";
  public static final String ASSESSMENT_GOAL = "goalDate";
  public static final String ASSESSMENT_DUE = "dueDate";
  public static final String ASSESSMENT_TYPE = "type";
  public static final String ASSESSMENT_COURSE_ID = "courseId";

  public static final String[] ALL_ASSMT_COLUMNS =
          {ASSESSMENT_ID, ASSESSMENT_NAME, ASSESSMENT_GOAL, ASSESSMENT_DUE,ASSESSMENT_TYPE, ASSESSMENT_COURSE_ID};

  public static final String CONTENT_ITEM_TYPE = "Assessment";

  public static final String TABLE_CREATE_ASSESSMENTS =
          "CREATE TABLE " + TABLE_ASSESSMENTS + " (" +
            ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ASSESSMENT_NAME + " TEXT, " +
            ASSESSMENT_GOAL + " TEXT," +
            ASSESSMENT_DUE + " TEXT," +
            ASSESSMENT_TYPE + " INTEGER," +
            ASSESSMENT_COURSE_ID + " INTEGER" +
            ")";
}
