package com.example.aowenswgumobile.database;

import android.net.Uri;

public class TermsTable {

  private static final String AUTHORITY = "com.example.aowenswgumobile";
  private static final String BASE_PATH = "terms";
  public static final Uri TERM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

  public static final String TABLE_TERMS = "terms";
  public static final String TERM_ID = "_id";
  public static final String TERM_TITLE = "title";
  public static final String TERM_START = "startDate";
  public static final String TERM_END = "endDate";

  public static final String[] ALL_TERM_COLUMNS = {TERM_ID, TERM_TITLE, TERM_START, TERM_END};

  public static final String CONTENT_ITEM_TYPE = "Term";

  //SQL to create table TERMS
  public static final String TABLE_CREATE_TERMS =
          "CREATE TABLE " + TABLE_TERMS + " (" +
                  TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                  TERM_TITLE + " TEXT, " +
                  TERM_START + " TEXT," +
                  TERM_END + " TEXT" +
                  ")";

}
