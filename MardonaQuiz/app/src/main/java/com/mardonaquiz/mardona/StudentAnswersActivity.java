package com.mardonaquiz.mardona;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class StudentAnswersActivity extends ActionBarActivity {

    protected ListView mStudentAnswerList;


    //Dummy DAta
    StudentAnswerItem[] dummyanswers= {new StudentAnswerItem("Q1","Paris","Paris"),new StudentAnswerItem("Q2","Cairo","Paris"),new StudentAnswerItem("Q3","London","London"),new StudentAnswerItem("Q4","LiverPool","Paris")};
    ArrayList<StudentAnswerItem> dummylist=new ArrayList<StudentAnswerItem>(Arrays.asList(dummyanswers));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_answers);

        mStudentAnswerList=(ListView) findViewById(R.id.student_answers_listview);
        StudentAnswerCustomAdapter answersAdapter= new StudentAnswerCustomAdapter(this,R.layout.item_student_answer,dummylist);
        mStudentAnswerList.setAdapter(answersAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_answers, menu);
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
