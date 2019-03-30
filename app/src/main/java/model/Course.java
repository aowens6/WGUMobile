package model;

import android.content.ContentValues;

import com.example.aowenswgumobile.database.CourseTable;
import com.example.aowenswgumobile.database.TermsTable;

import java.sql.Date;
import java.util.ArrayList;

public class Course {

  private String courseName;
  private String startDate;
  private String endDate;
  private int status;
  private int termId;
  private int mentorCode;
  private int startAlert;
  private int endAlert;

  public Course() {
  }

  public Course(String courseName, String startDate,
                String endDate, int status,
                int termId, int mentorCode,
                int startAlert, int endAlert) {
    this.courseName = courseName;
    this.startDate = startDate;
    this.endDate = endDate;
    this.status = status;
    this.termId = termId;
    this.mentorCode = mentorCode;
    this.startAlert = startAlert;
    this.endAlert = endAlert;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
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

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getTermId() {
    return termId;
  }

  public void setTermId(int termId) {
    this.termId = termId;
  }

  public int getMentorCode() {
    return mentorCode;
  }

  public void setMentorCode(int mentorCode) {
    this.mentorCode = mentorCode;
  }

  public int getStartAlert() {
    return startAlert;
  }

  public void setStartAlert(int startAlert) {
    this.startAlert = startAlert;
  }

  public int getEndAlert() {
    return endAlert;
  }

  public void setEndAlert(int endAlert) {
    this.endAlert = endAlert;
  }

  public ContentValues toValues(){
    ContentValues values = new ContentValues();

    values.put(CourseTable.COURSE_NAME, courseName);
    values.put(CourseTable.COURSE_START, startDate);
    values.put(CourseTable.COURSE_END, endDate);
    values.put(CourseTable.COURSE_STATUS_CODE, status);
    values.put(CourseTable.COURSE_TERM_ID, termId);
    values.put(CourseTable.COURSE_MENTOR_CODE, mentorCode);
    values.put(CourseTable.COURSE_START_ALERT, startAlert);
    values.put(CourseTable.COURSE_END_ALERT, endAlert);

    return values;
  }
}
