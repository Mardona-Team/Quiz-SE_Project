package com.mardonaquiz.mardona;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class StudentsScoresActivity extends ActionBarActivity {

    protected ListView mStudentScoresListView;
    protected Button mShowAnswersButton;

    // Dummy Data
    StudentScoreItem[] dummyStudents= {new StudentScoreItem("7amada","50"),new StudentScoreItem("Nouby","150"),new StudentScoreItem("mo7sen","100")};
    private ArrayList<StudentScoreItem> dummyScores=new ArrayList<StudentScoreItem>(Arrays.asList(dummyStudents));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_scores);

        mStudentScoresListView= (ListView)findViewById(R.id.listView_students_scores);
        StudentsScoresCustomAdapter scoresAdapter= new StudentsScoresCustomAdapter(this,R.layout.item_student_score,dummyScores);
        mStudentScoresListView.setAdapter(scoresAdapter);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_students_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
