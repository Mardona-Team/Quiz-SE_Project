package com.mardonaquiz.mardona.com.mardonaquiz.mardona.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mardonaquiz.mardona.com.mardonaquiz.mardona.activities.AnswerQuizActivity;
import com.mardonaquiz.mardona.com.mardonaquiz.mardona.items.PublishedQuizItem;
import com.mardonaquiz.mardona.R;
import com.mardonaquiz.mardona.com.mardonaquiz.mardona.activities.StudentsScoresActivity;

import java.util.ArrayList;

public class PublishedQuizListCustomAdapter extends ArrayAdapter<PublishedQuizItem> {
    Context mContext;
    int layoutResourceId;
    ArrayList<PublishedQuizItem> publishedQuizListItems = null;



    public PublishedQuizListCustomAdapter(Context mContext, int layoutResourceId, ArrayList<PublishedQuizItem> publishedQuizListItems) {
        super(mContext, layoutResourceId, publishedQuizListItems);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.publishedQuizListItems = publishedQuizListItems;
    }

    @Override
    public View getView(int position, final View convertView, android.view.ViewGroup parent) {



        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView publishedQuizTitle=(TextView) listItem.findViewById(R.id.published_quiz_title);
        Button publishedQuizButton1= (Button)  listItem.findViewById(R.id.published_quiz_button_1);
        Button publishedQuizButton2= (Button)  listItem.findViewById(R.id.published_quiz_button_2);


        final PublishedQuizItem publishedQuizItem = publishedQuizListItems.get(position);

       publishedQuizTitle.setText(publishedQuizItem.title);


        if(publishedQuizItem.type.equals("Instructor")){
            publishedQuizButton2.setText("Results");
            publishedQuizButton1.setVisibility(View.GONE);

            publishedQuizButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, StudentsScoresActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("quiz_title", publishedQuizItem.title);
                    i.putExtra("Qid",publishedQuizItem.quiz_refrence_id);
                    i.putExtra("id",publishedQuizItem.quiz_id);
                    i.putExtra("Gid",publishedQuizItem.group_id);
                    mContext.startActivity(i);
                }
            });
        }

        else if(publishedQuizItem.type.equals("Student")){
            publishedQuizButton1.setText("Answer");
            publishedQuizButton2.setText("Result");

            publishedQuizButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, AnswerQuizActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("Qid",publishedQuizItem.quiz_refrence_id);
                    i.putExtra("id",publishedQuizItem.quiz_id);
                    mContext.startActivity(i);
                }
            });

            publishedQuizButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, StudentsScoresActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("quiz_title", publishedQuizItem.title);
                    i.putExtra("Qid",publishedQuizItem.quiz_refrence_id);
                    i.putExtra("id",publishedQuizItem.quiz_id);
                    i.putExtra("Gid",publishedQuizItem.group_id);
                    mContext.startActivity(i);
                 }
            });
        }




        return listItem;
    }

}

