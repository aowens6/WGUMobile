package model;

import android.content.ContentValues;

import com.example.aowenswgumobile.database.AssessmentTable;
import com.example.aowenswgumobile.database.CourseTable;

public class Assessment {

  private String assessmentName;
  private String dueDate;
  private String startDate;
  private int typeCode;
  private int courseId;
  private int startAlert;
  private int dueAlert;

  public Assessment() {
  }

  public Assessment(String assessmentName, String dueDate,
                    String startDate, int typeCode,
                    int courseId, int startAlert, int dueAlert) {
    this.assessmentName = assessmentName;
    this.dueDate = dueDate;
    this.startDate = startDate;
    this.typeCode = typeCode;
    this.courseId = courseId;
    this.startAlert = startAlert;
    this.dueAlert = dueAlert;
  }

  public ContentValues toValues(){
    ContentValues values = new ContentValues();

    values.put(AssessmentTable.ASSESSMENT_NAME, assessmentName);
    values.put(AssessmentTable.ASSESSMENT_DUE, dueDate);
    values.put(AssessmentTable.ASSESSMENT_START, startDate);
    values.put(AssessmentTable.ASSESSMENT_TYPE, typeCode);
    values.put(AssessmentTable.ASSESSMENT_COURSE_ID, courseId);
    values.put(AssessmentTable.ASSESSMENT_START_ALERT, startAlert);
    values.put(AssessmentTable.ASSESSMENT_DUE_ALERT, dueAlert);

    return values;
  }

}
