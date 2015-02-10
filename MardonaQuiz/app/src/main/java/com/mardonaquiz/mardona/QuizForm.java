package com.mardonaquiz.mardona;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class QuizForm extends ActionBarActivity {

    private final static String CREARTE_QUIZ_URL = "http://es2alny.herokuapp.com/api/quizzes";

    protected String [] All_Questions ;
    protected String [] First_Answer ;
    protected String [] Second_Answer ;
    protected String [] Third_Answer ;
    protected String [] Fourth_Answer;

    protected static EditText Questions ;
    protected static EditText First_ans ;
    protected static EditText Second_ans ;
    protected static EditText Third_ans ;
    protected static EditText Fourth_ans;

    protected String question ;
    protected String answer_1 ;
    protected String answer_2 ;
    protected String answer_3 ;
    protected String answer_4 ;

    protected int  Questions_Numbers ;
    protected String Title_of_Quiz ;
    protected  String Description_of_quiz ;
    protected String subject_of_quiz ;
    protected String final_mark_of_quiz ;


    public void SubmitQuiz (){
        for(int i=0;i<Questions_Numbers;i++) {
            Log.e("the value of ", All_Questions[i]);
            Log.e("Answer1",First_Answer[i]);
            Log.e("Answer 2",Second_Answer[i]);
            Log.e("Answer 3",Third_Answer[i]);
            Log.e("Answer 4",Fourth_Answer[i]);}
        boolean haveErr=false;
        //this for loop is for validation
       /*or(int i=0;i<Questions_Numbers;i++) {
         if(  All_Questions[i]==""|| First_Answer[i]==""||Second_Answer[i]==""||Third_Answer[i]==""||Fourth_Answer[i]=="") {

         haveErr=true;
         }

        }
        if(haveErr==true){

            Toast.makeText(this,"Please make sure that all questions and answers are filled",Toast.LENGTH_SHORT).show();
        }
        else
        {
            //here we will write the code when there is no error
            for(int i=0;i<Questions_Numbers;i++) {
                Log.e("the value of ", All_Questions[i]);
                Log.e("Answer1",First_Answer[i]);
                Log.e("Answer 2",Second_Answer[i]);
                Log.e("Answer 3",Third_Answer[i]);
                Log.e("Answer 4",Fourth_Answer[i]);
               //ddQuiztoAPI addGrouptoapi = new AddQuiztoAPI();
              //addGrouptoapi.execute(CREARTE_QUIZ_URL);


            }
*/      }


   public void Monitor_Text_Changes(final int fragment_position){


        Questions = (EditText) mSectionsPagerAdapter.getItem(fragment_position).getView().findViewById(R.id.editText6);
        First_ans =(EditText)mSectionsPagerAdapter.getItem(fragment_position).getView().findViewById(R.id.editText7);
        Second_ans = (EditText)mSectionsPagerAdapter.getItem(fragment_position).getView().findViewById(R.id.editText8) ;
        Third_ans = (EditText)mSectionsPagerAdapter.getItem(fragment_position).getView().findViewById(R.id.editText9);
        Fourth_ans = (EditText)mSectionsPagerAdapter.getItem(fragment_position).getView().findViewById(R.id.editText10);



       Questions.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {


           }

           @Override
           public void afterTextChanged(Editable s) {

             if (s == Questions){

                 question = Questions.toString();
                 All_Questions[fragment_position-1]=question;
             }
               else if (s == First_ans){

                 answer_1 = First_ans.toString();
                 First_Answer[fragment_position-1]= answer_1;
             }
               else if (s == Second_ans){
                 answer_2 = Second_ans.toString();
                 Second_Answer[fragment_position-1]= answer_2;
             }
             else if (s == Third_ans){
                 answer_3 = Third_ans.toString();
               Third_Answer[fragment_position-1]= answer_3;
             }
             else if (s == Fourth_ans){
                 answer_4 = Fourth_ans.toString();
                 Fourth_Answer[fragment_position-1]= answer_4;
             }

           }

       });




   }


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_form);


        Bundle Number = getIntent().getExtras();

        if (Number != null) {

            Questions_Numbers =Integer.parseInt( Number.getString("Number_Of_Questions"));
            Title_of_Quiz = Number.getString("Quiz_Title");
            Description_of_quiz = Number.getString("Quiz_Description");
            subject_of_quiz = Number.getString("Quiz_Subject");
            final_mark_of_quiz = Number.getString("Final_Mark");


        }
            All_Questions=new String[Questions_Numbers];
        for(int i=0;i<Questions_Numbers;i++)
        {

            All_Questions[i]="";
        }
        First_Answer = new String[Questions_Numbers];
        for(int i = 0;i <Questions_Numbers;i++){
            First_Answer[i]="";
        }
        Second_Answer = new String[Questions_Numbers];
        for(int i =0; i<Questions_Numbers;i++){
            Second_Answer[i]="";
        }
        Third_Answer = new String[Questions_Numbers];
        for(int i = 0; i<Questions_Numbers;i++){
            Third_Answer[i]="";
        }
        Fourth_Answer = new String[Questions_Numbers];
        for(int i = 0 ; i<Questions_Numbers;i++){
            Fourth_Answer[i]="";
                    }



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {


                Log.e("pos",position+"");
                if (position!= 0) {


                    Monitor_Text_Changes(position);

                    Button submit = (Button) mSectionsPagerAdapter.getItem(position).getView().findViewById(R.id.button5);

                    if (position == Questions_Numbers ) {
                        submit.setVisibility(View.VISIBLE);
                    }


                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SubmitQuiz();
                        }
                    });
                }





                Toast.makeText(QuizForm.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {




            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz_form, menu);
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one o
     * f the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Fragment[] mplaceholder = new Fragment[Questions_Numbers];

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            for(int i = 0 ; i <Questions_Numbers; i++){
                mplaceholder[i]= PlaceholderFragment.newInstance(i);
            }
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new FirstFragment();
            } else {
                return mplaceholder[position-1];
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.

            return Questions_Numbers+1 ;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quiz_form, container, false);


            return rootView;
        }


    }

    public static class FirstFragment extends Fragment {


        public FirstFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quiz_form_first, container, false);


            return rootView;
        }


    }
    private class AddQuiztoAPI extends AsyncTask<String,Void,JSONObject> {

        protected JSONObject doInBackground(String... urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://es2alny.herokuapp.com/api/quizzes");
            JSONObject holder = new JSONObject();
            // Quiz_Details object contains details about the quiz (title,marks,...)
            JSONObject Quiz_Details = new JSONObject();
            //title object contains the three wrong answers
            JSONObject Questions_and_answers = new JSONObject();
            //Wrong_Answers JSON contains the remaining three wrong answers
            JSONObject Wrong_Answers = new JSONObject();
            String response = null;
            JSONObject json = new JSONObject();

            try {
                try {
                    // setup the returned values in case
                    // something goes wrong
                    json.put("success", false);
                    json.put("info", "Something went wrong. Retry!");
                    //add the quiz details to the params
                    Quiz_Details.put("title", Title_of_Quiz);
                    Quiz_Details.put("subject", subject_of_quiz);
                    Quiz_Details.put("description", Description_of_quiz);
                    Quiz_Details.put("marks", final_mark_of_quiz);
                    for (int counter = 0; counter < All_Questions.length; counter++) {
                        Questions_and_answers.put("title", All_Questions[counter]);
                    }
                    for (int counter2 = 0; counter2 < First_Answer.length; counter2++) {
                        Questions_and_answers.put("right_answer_attributes", First_Answer[counter2]);
                    }
                    Quiz_Details.put("questions_attributes", Questions_and_answers);
                    for (int counter3 = 0; counter3 < Second_Answer.length; counter3++) {
                        Wrong_Answers.put("title", Second_Answer[counter3]);
                    }
                    for (int counter4 = 0; counter4 < Third_Answer.length; counter4++) {
                        Wrong_Answers.put("title", Third_Answer[counter4]);
                    }
                    for (int counter5 = 0; counter5 < Fourth_Answer.length; counter5++) {
                        Wrong_Answers.put("title", Fourth_Answer[counter5]);
                    }
                    Quiz_Details.put("answers_attributes", Wrong_Answers);


                    holder.put("user", Quiz_Details);

                    StringEntity se = new StringEntity(holder.toString());
                    post.setEntity(se);


                    // setup the request headers
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-Type", "application/json");

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = client.execute(post, responseHandler);
                    json = new JSONObject(response);

                } catch (HttpResponseException e) {
                    e.printStackTrace();
                    Log.e("ClientProtocol", "" + e);
                    json.put("info", "Email and/or password are invalid. Retry!");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IO", "" + e);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON", "" + e);
            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {


            Log.e("The Quiz ",json.toString());


        }

    }


}
