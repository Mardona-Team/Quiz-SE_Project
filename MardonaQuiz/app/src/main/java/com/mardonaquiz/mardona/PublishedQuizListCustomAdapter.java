package com.mardonaquiz.mardona;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

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


        PublishedQuizItem publishedQuizItem = publishedQuizListItems.get(position);

       publishedQuizTitle.setText(publishedQuizItem.title);


        if(publishedQuizItem.type.equals("Instructor")){
            publishedQuizButton1.setText("Results");
            publishedQuizButton2.setText("Statics");

            publishedQuizButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, StudentsScoresActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            });

            publishedQuizButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, StatisticsActivty.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            });
        }




        return listItem;
    }
}
