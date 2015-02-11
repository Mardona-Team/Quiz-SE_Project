package com.mardonaquiz.mardona.com.mardonaquiz.mardona.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mardonaquiz.mardona.R;
import com.mardonaquiz.mardona.com.mardonaquiz.mardona.items.StudentAnswerItem;

import java.util.ArrayList;

public class StudentAnswerCustomAdapter extends ArrayAdapter<StudentAnswerItem> {
    Context mContext;
    int layoutResourceId;
    ArrayList<StudentAnswerItem> studentAnswerItems = null;

    public StudentAnswerCustomAdapter(Context mContext, int layoutResourceId, ArrayList<StudentAnswerItem> studentAnswerItems) {
        super(mContext, layoutResourceId, studentAnswerItems);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.studentAnswerItems = studentAnswerItems;
    }


    @Override
    public View getView(int position, final View convertView, android.view.ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView questionTextView = (TextView) listItem.findViewById(R.id.question_number);
        TextView answerTextView = (TextView) listItem.findViewById(R.id.student_answer);
        TextView correctnessTextView =(TextView) listItem.findViewById(R.id.correctness);

        StudentAnswerItem studentAnswerItem = studentAnswerItems.get(position);

        if(studentAnswerItem.answer.equals(studentAnswerItem.rightAnswer)) {
            correctnessTextView.setText("Correct");
            correctnessTextView.setTextColor(Color.parseColor("#4caf50"));
        }
        else {
            correctnessTextView.setText("Wrong");
            correctnessTextView.setTextColor(Color.parseColor("#f44336"));
        }
        questionTextView.setText(studentAnswerItem.question);
        answerTextView.setText(studentAnswerItem.answer);


        return listItem;
    }
}

