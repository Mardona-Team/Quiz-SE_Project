package com.mardonaquiz.mardona;

import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentsScoresCustomAdapter extends ArrayAdapter<StudentScore> {

    Context mContext;
    int layoutResourceId;
    ArrayList<StudentScore> studentScores = null;

    public StudentsScoresCustomAdapter(Context mContext, int layoutResourceId, ArrayList<StudentScore> studentScores) {
        super(mContext, layoutResourceId, studentScores);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.studentScores = studentScores;
    }


    @Override
    public View getView(int position, View convertView, android.view.ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView studentNameTextView = (TextView) listItem.findViewById(R.id.student_name);
        TextView studentGradeTextView = (TextView) listItem.findViewById(R.id.grade);
        Button showAnswersButton= (Button) listItem.findViewById(R.id.show_answers_button);


        StudentScore studentScore = studentScores.get(position);


        studentNameTextView.setText(studentScore.name);
        studentGradeTextView.setText(studentScore.grade);

        return listItem;
    }
}
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        StudentScore studentScore = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent,false);
//        }
//        // Lookup view for data population
//        TextView studentNameTextView = (TextView) convertView.findViewById(R.id.student_name);
//        TextView studentGradeTextView = (TextView) convertView.findViewById(R.id.grade);
//        // Populate the data into the template view using the data object
//        studentNameTextView.setText(studentScore.name);
//        studentGradeTextView.setText(studentScore.grade);
//        // Return the completed view to render on screen
//        return convertView;
//    }
//}