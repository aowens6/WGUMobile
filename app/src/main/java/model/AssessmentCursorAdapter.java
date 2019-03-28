package model;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aowenswgumobile.R;
import com.example.aowenswgumobile.database.AssessmentTable;

public class AssessmentCursorAdapter extends CursorAdapter {

  public AssessmentCursorAdapter(Context context, Cursor c, int flags) {
    super(context, c, flags);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
    return LayoutInflater.from(context).inflate(R.layout.assessment_list_item, viewGroup, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {

    int assessmentType = cursor.getInt(cursor.getColumnIndex(AssessmentTable.ASSESSMENT_TYPE));
    ImageView icon = view.findViewById(R.id.typeIcon);
    if(assessmentType == 1){
      icon.setImageResource(R.drawable.ic_o_icon);
    }else if(assessmentType == 2){
      icon.setImageResource(R.drawable.ic_p_icon);
    }

    TextView assessmentNameTxt = view.findViewById(R.id.assmtNameTxt);
    assessmentNameTxt.setText(cursor.getString(cursor.getColumnIndex(AssessmentTable.ASSESSMENT_NAME)));

    TextView assmtDueDate = view.findViewById(R.id.assmtDueTxt);
    assmtDueDate.setText(cursor.getString(cursor.getColumnIndex(AssessmentTable.ASSESSMENT_DUE)));

  }
}
