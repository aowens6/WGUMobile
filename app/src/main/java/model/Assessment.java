package model;

import android.content.ContentValues;

import com.example.aowenswgumobile.database.AssessmentTable;
import com.example.aowenswgumobile.database.CourseTable;

public class Assessment {

  private String assessmentName;
  private String dueDate;
  private String goalDate;
  private int typeCode;
  private int courseId;

  public Assessment() {
  }

  public Assessment(String assessmentName, String dueDate, String goalDate, int typeCode, int courseId) {
    this.assessmentName = assessmentName;
    this.dueDate = dueDate;
    this.goalDate = goalDate;
    this.typeCode = typeCode;
    this.courseId = courseId;
  }

  public ContentValues toValues(){
    ContentValues values = new ContentValues();

    values.put(AssessmentTable.ASSESSMENT_NAME, assessmentName);
    values.put(AssessmentTable.ASSESSMENT_DUE, dueDate);
    values.put(AssessmentTable.ASSESSMENT_GOAL, goalDate);
    values.put(AssessmentTable.ASSESSMENT_TYPE, typeCode);
    values.put(AssessmentTable.ASSESSMENT_COURSE_ID, courseId);

    return values;
  }

}
