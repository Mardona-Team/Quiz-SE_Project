package com.mardonaquiz.mardona;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;

public class StudentsScoresCustomAdapter extends ArrayAdapter<StudentScoreItem> {

    Context mContext;
    int layoutResourceId;
    ArrayList<StudentScoreItem> studentScoreItems = null;

    public StudentsScoresCustomAdapter(Context mContext, int layoutResourceId, ArrayList<StudentScoreItem> studentScoreItems) {
        super(mContext, layoutResourceId, studentScoreItems);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.studentScoreItems = studentScoreItems;
    }


    @Override
    public View getView(int position, final View convertView, android.view.ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView studentNameTextView = (TextView) listItem.findViewById(R.id.student_name);
        TextView studentGradeTextView = (TextView) listItem.findViewById(R.id.grade);
        Button showAnswersButton = (Button) listItem.findViewById(R.id.show_answers_button);


        StudentScoreItem studentScoreItem = studentScoreItems.get(position);
        final String studentID=studentScoreItem.id;

        showAnswersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, StudentAnswersActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("id",studentID);
                mContext.startActivity(i);
            }
        });

        if(studentScoreItem.passing)  studentGradeTextView.setTextColor(Color.GREEN);
        else studentGradeTextView.setTextColor(Color.RED);


            studentNameTextView.setText(studentScoreItem.name);
        studentGradeTextView.setText(studentScoreItem.grade);

        return listItem;
    }

    }
