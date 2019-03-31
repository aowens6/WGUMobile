package model;

import android.content.ContentValues;

import com.example.aowenswgumobile.database.AlertTable;

public class Alert {

  private int courseId;
  private String typeCode;
  private int assessmentId;

  public Alert(int courseId, String typeCode, int assessmentId) {
    this.courseId = courseId;
    this.typeCode = typeCode;
    this.assessmentId = assessmentId;
  }

  public ContentValues toValues(){

    ContentValues values = new ContentValues();

    values.put(AlertTable.ALERT_COURSE_ID, courseId);
    values.put(AlertTable.ALERT_TYPECODE, typeCode);
    values.put(AlertTable.ALERT_ASSMT_ID, assessmentId);

    return values;
  }
}
