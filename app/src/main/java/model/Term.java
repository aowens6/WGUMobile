package model;

import android.content.ContentValues;

import com.example.aowenswgumobile.database.TermsTable;

import java.sql.Date;
import java.util.ArrayList;

public class Term {

  private int id;
  private String title;
  private String startDate;
  private String endDate;

  public Term( String title, String startDate, String endDate) {
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public ContentValues toValues(){
    ContentValues values = new ContentValues();

    values.put(TermsTable.TERM_TITLE, title);
    values.put(TermsTable.TERM_START, startDate);
    values.put(TermsTable.TERM_END, endDate);

    return values;
  }
}
