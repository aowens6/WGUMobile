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
  private String status;
  private ArrayList<CourseMentor> mentors;
  private int termId;

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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public ArrayList<CourseMentor> getMentors() {
    return mentors;
  }

  public void setMentors(ArrayList<CourseMentor> mentors) {
    this.mentors = mentors;
  }

  public int getTermId() {
    return termId;
  }

  public void setTermId(int termId) {
    this.termId = termId;
  }

  public ContentValues toValues(){
    ContentValues values = new ContentValues();

    values.put(CourseTable.COURSE_NAME, courseName);
    values.put(CourseTable.COURSE_START, startDate);
    values.put(CourseTable.COURSE_END, endDate);
    values.put(CourseTable.COURSE_STATUS, status);
    values.put(CourseTable.COURSE_TERM_ID, termId);

    return values;
  }
}
