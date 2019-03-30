package model;

import android.content.ContentValues;

import com.example.aowenswgumobile.database.AlertTable;

public class Alert {

  private int courseId;
  private String typeCode;

  public Alert(int courseId, String typeCode) {
    this.courseId = courseId;
    this.typeCode = typeCode;
  }

  public ContentValues toValues(){

    ContentValues values = new ContentValues();

    values.put(AlertTable.ALERT_COURSE_ID, courseId);
    values.put(AlertTable.ALERT_TYPECODE, typeCode);

    return values;
  }
}
