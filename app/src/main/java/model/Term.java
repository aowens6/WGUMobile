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

  public Term() {
  }

  public Term( String title, String startDate, String endDate) {
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
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
