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

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

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
                       openChart();
                }
            });
        }

        else if(publishedQuizItem.type.equals("Student")){
            publishedQuizButton1.setText("Answer Quiz");
            publishedQuizButton2.setText("Result");

            publishedQuizButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, AnswerQuiz.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            });

            publishedQuizButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, StudentsScoresActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                 }
            });
        }




        return listItem;
    }


    private void openChart() {

        // Pie Chart Section Names
        String[] code = new String[]{
                "Excellent", "Very Good", "Good", "Passed",
                "Failed"
        };

        // Pie Chart Section Value
        double[] distribution = {3.9, 12.9, 55.8, 1.9, 23.7};

        // Color of each Pie Chart Sections
        int[] colors = {Color.GREEN, Color.BLUE, Color.YELLOW,Color.parseColor("#ffa500"), Color.RED};

        // Instantiating CategorySeries to plot Pie Chart
        CategorySeries distributionSeries = new CategorySeries("Students' Grades Distribution");
        for (int i = 0; i < distribution.length; i++) {
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(code[i], distribution[i]);
        }

        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < distribution.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle("Students' Grades Distribution");
        defaultRenderer.setChartTitleTextSize(50);
        defaultRenderer.setApplyBackgroundColor(true);
        defaultRenderer.setBackgroundColor(Color.BLACK);
        defaultRenderer.setLabelsTextSize(30);
        defaultRenderer.setLegendTextSize(30);
        defaultRenderer.setDisplayValues(true);
        defaultRenderer.setZoomButtonsVisible(true);

        Intent intent = ChartFactory.getPieChartIntent(mContext, distributionSeries, defaultRenderer, "AChartEnginePieChartDemo");

        // Start Activity
        mContext.startActivity(intent);
    }
}

