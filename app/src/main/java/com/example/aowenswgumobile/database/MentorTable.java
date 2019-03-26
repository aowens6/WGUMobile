package com.example.aowenswgumobile.database;

import android.net.Uri;

public class MentorTable {

  private static final String AUTHORITY = "com.example.aowenswgumobile";
  private static final String BASE_PATH = "mentors";
  public static final Uri MENTOR_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

  public static final String TABLE_MENTORS = "mentors";
  public static final String MENTOR_ID = "_id";
  public static final String MENTOR_NAME = "mentorName";
  public static final String MENTOR_PHONE = "phone";
  public static final String MENTOR_EMAIL = "email";
  public static final String MENTOR_CODE = "mentorCode";

  public static final String[] ALL_MENTOR_COLUMNS =
          {MENTOR_ID, MENTOR_NAME, MENTOR_PHONE, MENTOR_EMAIL, MENTOR_CODE};

  public static final String CONTENT_ITEM_TYPE = "Mentor";

  public static final String TABLE_CREATE_MENTORS =
          "CREATE TABLE " + TABLE_MENTORS + " (" +
            MENTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MENTOR_NAME + " TEXT, " +
            MENTOR_PHONE + " TEXT," +
            MENTOR_EMAIL + " TEXT," +
            MENTOR_CODE + " INTEGER" +
            ")";

}
