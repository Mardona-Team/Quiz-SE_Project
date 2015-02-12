package com.mardonaquiz.mardona.com.mardonaquiz.mardona.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mardonaquiz.mardona.R;


public class CreateQuizActivity extends ActionBarActivity {

    protected SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);


        Button Submit = (Button) findViewById(R.id.button3);

        Submit.setOnClickListener(new View.OnClickListener() {
            public  void  onClick(View arg0) {



                //Declaring the variables

                EditText Title = (EditText) findViewById(R.id.editText);
                 String Q_Title = Title.getText().toString();

                EditText Desc = (EditText) findViewById(R.id.editText2);
                 String Q_Desc = Desc.getText().toString();

                EditText Subj = (EditText) findViewById(R.id.editText3);
                 String Q_Subj = Subj.getText().toString();

                EditText Qu_Num = (EditText) findViewById(R.id.editText4);
                 String Qu_NO = Qu_Num.getText().toString();

                EditText Final = (EditText) findViewById(R.id.editText5);
                String FinalMarks = Final.getText().toString();

                EditText Year = (EditText) findViewById(R.id.editText12);
                String YearString = Year.getText().toString();


                if(Q_Title.length()==0|| Q_Desc.length()==0|| Q_Subj.length()==0||Qu_NO.length()==0||YearString.length()==0||FinalMarks.length()==0) {
                    Toast.makeText(CreateQuizActivity.this, "Please fill in the missing data", Toast.LENGTH_SHORT).show();

                }
                else {
                    Intent QuizIntent=new Intent(CreateQuizActivity.this,QuizFormActivity.class);
                    QuizIntent.putExtra("Number_Of_Questions",Qu_NO);
                    QuizIntent.putExtra("Quiz_Title",Q_Title);
                    QuizIntent.putExtra("Quiz_Description", Q_Desc);
                    QuizIntent.putExtra("Final_Mark",FinalMarks);
                    QuizIntent.putExtra("Quiz_Subject",Q_Subj);
                    QuizIntent.putExtra("Quiz_Year",YearString);
                    QuizIntent.putExtra("Instructor_Id",mPreferences.getString("id",""));
                    startActivity(QuizIntent);
                    finish();
                }

                }




        });

        }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_quiz, menu);
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
