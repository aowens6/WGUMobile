package com.example.aowenswgumobile.database;

public class AssessmentTable {

  public static final String TABLE_ASSESSMENTS = "assessments";
  public static final String ASSESSMENT_ID = "_id";
  public static final String ASSESSMENT_NAME = "assessmentName";
  public static final String ASSESSMENT_START = "startDate";
  public static final String ASSESSMENT_DUE = "dueDate";
  public static final String ASSESSMENT_TYPE = "type";
  public static final String ASSESSMENT_COURSE_ID = "courseId";
  public static final String ASSESSMENT_START_ALERT= "startAlert";
  public static final String ASSESSMENT_DUE_ALERT = "dueAlert";

  public static final String[] ALL_ASSMT_COLUMNS =
          {ASSESSMENT_ID, ASSESSMENT_NAME,
           ASSESSMENT_START, ASSESSMENT_DUE,
           ASSESSMENT_TYPE, ASSESSMENT_COURSE_ID,
                  ASSESSMENT_START_ALERT, ASSESSMENT_DUE_ALERT};

  public static final String CONTENT_ITEM_TYPE = "Assessment";

  public static final String TABLE_CREATE_ASSESSMENTS =
          "CREATE TABLE " + TABLE_ASSESSMENTS + " (" +
            ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ASSESSMENT_NAME + " TEXT, " +
            ASSESSMENT_START + " TEXT," +
            ASSESSMENT_DUE + " TEXT," +
            ASSESSMENT_TYPE + " INTEGER," +
            ASSESSMENT_COURSE_ID + " INTEGER," +
            ASSESSMENT_START_ALERT + " INTEGER," +
            ASSESSMENT_DUE_ALERT + " INTEGER" +
            ")";
}
