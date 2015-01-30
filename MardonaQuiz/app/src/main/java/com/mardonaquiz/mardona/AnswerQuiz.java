package com.mardonaquiz.mardona;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class AnswerQuiz extends ActionBarActivity {

    public static final String  KEY_ID="id";
    public static final String  KEY_questions="questions";
    public static final  String  KEY_TITLE="title";
    public static final String  KEY_shuffled_answers="shuffled_answers";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_YEAR = "year";
    public static final String KEY_Marks = "marks";
    public static final String KEY_Desc = "description";

    protected String quizId="3";
    protected int NUM_Questions;
    protected ArrayList<String> Questions = new ArrayList<String>();
    protected ArrayList<String>  answer1 =  new ArrayList<String>();
    protected ArrayList<String>   answer2 =  new ArrayList<String>();
    protected ArrayList<String>   answer3 =  new ArrayList<String>();
    protected ArrayList<String>   answer4 =  new ArrayList<String>();
    HashMap<String, String> Quiz_Data = new HashMap<String, String>();

    private  String TAG="AnswerQuiz";
    protected   ArrayList<Integer> Output_Answers = new ArrayList<Integer>();

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_quiz);
        Get_questions_From_server get_questions = new Get_questions_From_server();
        get_questions.execute(); // getting questions from server
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer_quiz, menu);
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

    public void Monitor_Answers_changes(final int question) {
        RadioGroup agroup = (RadioGroup) mSectionsPagerAdapter.getItem(question).getView().findViewById(R.id.rgroup);
        agroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int answerdIndex = group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId()));
                Log.d("question ", question-1+" answer " + answerdIndex);
                Output_Answers.add(question - 1, answerdIndex);

            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        QuestionsFragments[] fragment=new QuestionsFragments[NUM_Questions];

        QuestionsStartFragments fragmentStart=QuestionsStartFragments.newInstance(0,Quiz_Data);

        QuestionsEndFragments fragmentEnd= QuestionsEndFragments.newInstance(NUM_Questions+1);

        public SectionsPagerAdapter(FragmentManager fm,ArrayList questions,ArrayList a1,ArrayList a2,ArrayList a3,ArrayList a4) {
            super(fm);

            for(int i=0;i< NUM_Questions;i++)
            {
                ArrayList<String> Question_Answers = new ArrayList<String>();
                Question_Answers.add(Questions.get(i).toString());
                Question_Answers.add(a1.get(i).toString());
                Question_Answers.add(a2.get(i).toString());
                Question_Answers.add(a3.get(i).toString());
                Question_Answers.add(a4.get(i).toString());

                fragment[i]=QuestionsFragments.newInstance(i,Question_Answers);

            }
        }

        @Override
        public int getCount() {
            return NUM_Questions +2;
        }


        @Override
        public Fragment getItem(int position) {

            if(position== NUM_Questions+1)
            {
                // last fragment = number of questions + 1 and this one is the first fragment which is intro
                return fragmentEnd;
            }
            else if(position==0)
            {
                //this is the first fragment whic is intro
                return fragmentStart;
            }
            else{
                // i return position-1 to compensate that my first page is taken by onther fragment
                return fragment[position-1];
            }

        }

    }

    public void handleResponse(JSONObject result) {


        ArrayList<HashMap<String, String>> AllQuestions =  new ArrayList<HashMap<String, String>>();

        if (result == null) {
            //Todo here the errore messege
        }
        else {
            try {
                JSONArray QuestionsJason = result.getJSONArray(KEY_questions);

                for (int i = 0; i < QuestionsJason.length(); i++) {
                    JSONObject post = QuestionsJason.getJSONObject(i);


                    String ID = post.getString(KEY_ID);
                    String title = post.getString(KEY_TITLE);

                    JSONArray shuffled_answers = post.getJSONArray(KEY_shuffled_answers);

                    HashMap<String, String> shuffled_Answers_list = new HashMap<String, String>();

                    for (int j=0;j<shuffled_answers.length();j++) {
                        shuffled_Answers_list.put(shuffled_answers.getJSONObject(j).getString(KEY_ID), shuffled_answers.getJSONObject(j).getString(KEY_TITLE));
                    }


                    HashMap<String, String> mQuestion = new HashMap<String, String>();

                    mQuestion.put(KEY_ID, ID);
                    mQuestion.put(KEY_TITLE, title);

                    Questions.add(title);
                    answer1.add(shuffled_answers.getJSONObject(0).getString(KEY_TITLE));
                    answer2.add(shuffled_answers.getJSONObject(1).getString(KEY_TITLE));
                    answer3.add(shuffled_answers.getJSONObject(2).getString(KEY_TITLE));
                    answer4.add(shuffled_answers.getJSONObject(3).getString(KEY_TITLE));
/*


                    mQuestion.put(KEY_SUBJECT, Subject);
                    mQuestion.put(KEY_YEAR, year);
                    mQuestion.put(KEY_Desc, description);
*/


                    AllQuestions.add(mQuestion);
                }
                Quiz_Data.put(KEY_ID,result.getString(KEY_ID));
                Quiz_Data.put(KEY_TITLE,result.getString(KEY_TITLE));
                Quiz_Data.put(KEY_SUBJECT,result.getString(KEY_SUBJECT));
                Quiz_Data.put(KEY_YEAR,result.getString(KEY_YEAR));
                Quiz_Data.put(KEY_Desc,result.getString(KEY_Desc));
                Quiz_Data.put(KEY_Marks,result.getString(KEY_Marks));

                NUM_Questions=Questions.size();
                initiate_view();

            }
            catch (JSONException e) {
                Log.e("", "Exception caught!", e);
            }
        }
    }

    private void initiate_view()   {

        //start


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),Questions,answer1,answer2,answer3,answer4);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {

                if(i!= NUM_Questions+1 &&i!=0) {

                    Monitor_Answers_changes(i);
                    Log.e("i selected is", "" + i);

                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        ///end


    }

    private class Get_questions_From_server extends AsyncTask<Object, Void, JSONObject> {

        ProgressDialog progDailog = new ProgressDialog(AnswerQuiz.this);

        protected void onPreExecute() {
            super.onPreExecute();
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }



        @Override
        protected JSONObject doInBackground(Object... arg0) {
            int responseCode = -1;
            JSONObject jsonResponse = null;

            try {
                URL blogFeedUrl = new URL("https://es2alny.herokuapp.com/api/quizzes/"+quizId);
                HttpURLConnection connection = (HttpURLConnection) blogFeedUrl.openConnection();
                connection.connect();

                responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    Reader reader = new InputStreamReader(inputStream);
                    int contentLength = connection.getContentLength();
                    char[] charArray = new char[contentLength];
                    reader.read(charArray);
                    String responseData = new String(charArray);


                    jsonResponse = new JSONObject(responseData);
                }
                else {
                    Log.i(TAG, "Unsuccessful HTTP Response Code: " + responseCode);
                }
            }
            catch (MalformedURLException e) {
                Log.e(TAG, "Exception caught: ", e);
            }
            catch (IOException e) {
                Log.e(TAG, "Exception caught: ", e);
            }
            catch (Exception e) {
                Log.e(TAG, "Exception caught: ", e);
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            progDailog.cancel();
            handleResponse(result);
        }
    }

    public static class QuestionsFragments extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_Question = "Quest";
        private static final String ARG_A1 = "a1";
        private static final String ARG_A2 = "a2";
        private static final String ARG_A3 = "a3";
        private static final String ARG_A4 = "a4";

        public static QuestionsFragments newInstance(int sectionNumber,ArrayList ques_answers) {
            QuestionsFragments fragment = new QuestionsFragments();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_Question, ques_answers.get(0).toString());
            args.putString(ARG_A1, ques_answers.get(1).toString());
            args.putString(ARG_A2, ques_answers.get(2).toString());
            args.putString(ARG_A3, ques_answers.get(3).toString());
            args.putString(ARG_A4, ques_answers.get(4).toString());

            fragment.setArguments(args);


            return fragment;
        }
        public QuestionsFragments() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_answer_quiz, container, false);
            int secn=getArguments().getInt(ARG_SECTION_NUMBER);

            // Geting variables from arguments
            String ques=getArguments().getString(ARG_Question);

            String answer1=getArguments().getString(ARG_A1);
            String answer2=getArguments().getString(ARG_A2);
            String answer3=getArguments().getString(ARG_A3);
            String answer4=getArguments().getString(ARG_A4);



            //assigning views to variables to be changed
            TextView question=(TextView) rootView.findViewById(R.id.Question);
            RadioButton a1=(RadioButton) rootView.findViewById(R.id.r1);
            RadioButton a2=(RadioButton) rootView.findViewById(R.id.r2);
            RadioButton a3=(RadioButton) rootView.findViewById(R.id.r3);
            RadioButton a4=(RadioButton) rootView.findViewById(R.id.r4);


            //assigning views to variables from arguments
            question.setText(ques);
            a1.setText(answer1);
            a2.setText(answer2);
            a3.setText(answer3);
            a4.setText(answer4);


            return rootView;
        }
    }
    /**
     this is the Last Fragment to view stats about the quiz
     */
    public static class QuestionsEndFragments extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static QuestionsEndFragments newInstance(int sectionNumber) {
            QuestionsEndFragments fragment = new QuestionsEndFragments();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);


            return fragment;
        }



        public QuestionsEndFragments() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_answer_quiz_last, container, false);


            return rootView;
        }
    }
    /**
     this is the Last Fragment to view stats about the quiz
     */
    public static class QuestionsStartFragments extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static QuestionsStartFragments newInstance(int sectionNumber,HashMap<String, String> Quiz_Data) {
            QuestionsStartFragments fragment = new QuestionsStartFragments();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(KEY_YEAR,Quiz_Data.get(KEY_YEAR));
            args.putString(KEY_SUBJECT,Quiz_Data.get(KEY_SUBJECT));
            args.putString(KEY_Marks,Quiz_Data.get(KEY_Marks));
            args.putString(KEY_TITLE,Quiz_Data.get(KEY_TITLE));
            args.putString(KEY_Desc,Quiz_Data.get(KEY_Desc));


            fragment.setArguments(args);


            return fragment;
        }



        public QuestionsStartFragments() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_answer_quiz_first, container, false);
            ((TextView)rootView.findViewById(R.id.QuizTitle)).setText(getArguments().getString(KEY_TITLE));
            ((TextView)rootView.findViewById(R.id.QuizSubj)).setText("Subject "+getArguments().getString(KEY_SUBJECT));
            ((TextView)rootView.findViewById(R.id.QuizDesc)).setText("Desc "+getArguments().getString(KEY_Desc));
            ((TextView)rootView.findViewById(R.id.QuizMarks)).setText("Total Marks "+getArguments().getString(KEY_Marks));
            ((TextView)rootView.findViewById(R.id.QuizYear)).setText("Year "+getArguments().getString(KEY_YEAR));




            return rootView;
        }

    }


}
