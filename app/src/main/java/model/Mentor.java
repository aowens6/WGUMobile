package model;

import android.content.ContentValues;

import com.example.aowenswgumobile.database.MentorTable;

public class Mentor {

  private String name;
  private String phone;
  private String email;
  private int mentorCode;

  public Mentor(String name, String phone, String email, int mentorCode) {
    this.name = name;
    this.phone = phone;
    this.email = email;
    this.mentorCode = mentorCode;
  }

  public ContentValues toValues(){
    ContentValues values = new ContentValues();

    values.put(MentorTable.MENTOR_NAME, name);
    values.put(MentorTable.MENTOR_PHONE, phone);
    values.put(MentorTable.MENTOR_EMAIL, email);
    values.put(MentorTable.MENTOR_CODE, mentorCode);

    return values;
  }
}
