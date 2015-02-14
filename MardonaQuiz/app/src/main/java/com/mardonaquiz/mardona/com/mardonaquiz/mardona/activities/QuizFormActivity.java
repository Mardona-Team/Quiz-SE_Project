package com.mardonaquiz.mardona.com.mardonaquiz.mardona.activities;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mardonaquiz.mardona.R;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class QuizFormActivity extends ActionBarActivity {

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
    protected String Year_of_Quiz ;
    protected String Instructor_Id;





    public void SubmitQuiz () {
        for (int i = 0; i < Questions_Numbers; i++) {
            Log.e("the value of ", All_Questions[i]);
            Log.e("Answer1", First_Answer[i]);
            Log.e("Answer 2", Second_Answer[i]);
            Log.e("Answer 3", Third_Answer[i]);
            Log.e("Answer 4", Fourth_Answer[i]);
        }
        boolean haveErr = false;
        //this for loop is for validation
        for (int i = 0; i < Questions_Numbers; i++) {
            if (All_Questions[i] == "" || First_Answer[i] == "" || Second_Answer[i] == "" || Third_Answer[i] == "" || Fourth_Answer[i] == "") {

                haveErr = true;
            }

        }
        if (haveErr == true) {

            Toast.makeText(this, "Please make sure that all questions and answers are filled", Toast.LENGTH_SHORT).show();
        } else {
            AddQuiztoAPI addGrouptoapi = new AddQuiztoAPI();
            addGrouptoapi.execute(CREARTE_QUIZ_URL);
        }
    }



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


                 question = s.toString();
                 All_Questions[fragment_position-1]=question;
             }




       });
       First_ans.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {


           }

           @Override
           public void afterTextChanged(Editable First_ans) {


               answer_1 = First_ans.toString();
               First_Answer[fragment_position - 1] = answer_1;
           }


       });
       Second_ans.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {


           }

           @Override
           public void afterTextChanged(Editable Second_ans) {


               answer_2 = Second_ans.toString();
               Second_Answer[fragment_position-1]=answer_2;
           }

       });
       Third_ans.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {


           }

           @Override
           public void afterTextChanged(Editable Third_ans) {


               answer_3 = Third_ans.toString();
               Third_Answer[fragment_position-1]=answer_3;
           }
       });
      Fourth_ans.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {


           }

           @Override
           public void afterTextChanged(Editable Fourth_ans) {


              answer_4 = Fourth_ans.toString();
               Fourth_Answer[fragment_position-1]=answer_4;
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
            Year_of_Quiz = Number.getString("Quiz_Year");
            Instructor_Id=Number.getString("Instructor_Id");



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





//                Toast.makeText(QuizForm.this,
//                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
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


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int QuestionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt("q", QuestionNumber+1);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quiz_form, container, false);



            ((TextView)(rootView.findViewById(R.id.textView7))).setText("Question "+getArguments().getInt("q"));

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



        ProgressDialog progDailog = new ProgressDialog(QuizFormActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        protected JSONObject doInBackground(String... urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://es2alny.herokuapp.com/api/quizzes");
            JSONObject holder = new JSONObject();
            // Quiz_Details object contains details about the quiz (title,marks,...)
            JSONObject quiz = new JSONObject();

            JSONObject title1 = new JSONObject();
            JSONObject title2 = new JSONObject();
            JSONObject title3 = new JSONObject();
            JSONArray questions_attributes = new JSONArray();

            String response = null;
            JSONObject json = new JSONObject();


            try {

                    json.put("success", false);
                    quiz.put("title", Title_of_Quiz);
                    quiz.put("subject", subject_of_quiz);
                    quiz.put("description", Description_of_quiz);
                    quiz.put("marks", final_mark_of_quiz);
                    quiz.put("year", Year_of_Quiz);
                    quiz.put("instructor_id",Instructor_Id);

                    for (int counter = 0; counter < Questions_Numbers; counter++) {

                        JSONObject one_question_attributes = new JSONObject();
                        JSONObject right_answer = new JSONObject();


                        one_question_attributes.put("title", All_Questions[counter]);


                        right_answer.put("title", First_Answer[counter]);


                        one_question_attributes.put("right_answer_attributes", right_answer);
                        
                        JSONArray answers_attributes = new JSONArray();
                        title1.put("title", Second_Answer[counter]);
                        title2.put("title", Third_Answer[counter]);
                        title3.put("title", Fourth_Answer[counter]);

                        answers_attributes.put(0, title1);
                        answers_attributes.put(1, title2);
                        answers_attributes.put(2, title3);

                        one_question_attributes.put("answers_attributes", answers_attributes);
                        questions_attributes.put(one_question_attributes);
                    }

                    quiz.put("questions_attributes", questions_attributes);
                    holder.put("quiz", quiz);
                    StringEntity se = new StringEntity(holder.toString());
                    post.setEntity(se);
                     Log.e("el json hwa",holder.toString());
                    // setup the request headers
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-Type", "application/json");

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = client.execute(post, responseHandler);
                    json = new JSONObject(response);

                    json.put("success", true);

                } catch (HttpResponseException e) {
                    e.printStackTrace();
                    Log.e("ClientProtocol", "" + e);


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IO", "" + e);
                }
                catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON", "" + e);
            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Log.e("the output is ",json.toString());
            try {
                if (json.getBoolean("success")) {


                    // launch the HomeActivity and close this one
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                Toast.makeText(getApplicationContext(),"Quiz has been successfully created", Toast.LENGTH_LONG).show();
                progDailog.cancel();
            } catch (Exception e) {
                // something went wrong: show a Toast
                // with the exception message
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }



        }

    }


}
