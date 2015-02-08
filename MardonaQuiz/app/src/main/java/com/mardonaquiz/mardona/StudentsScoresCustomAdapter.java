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

        showAnswersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(mContext, StudentAnswersActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(i);

                openChart();

            }
        });


        StudentScoreItem studentScoreItem = studentScoreItems.get(position);


        studentNameTextView.setText(studentScoreItem.name);
        studentGradeTextView.setText(studentScoreItem.grade);

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
