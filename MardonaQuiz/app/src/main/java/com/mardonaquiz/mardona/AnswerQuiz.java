package com.mardonaquiz.mardona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AnswerQuiz extends ActionBarActivity {

    /**
     * ########################  Kyes ###############################
     **/
    public static final String  KEY_ID="id";
    public static final String  KEY_questions="questions";
    public static final  String  KEY_TITLE="title";
    public static final String  KEY_shuffled_answers="shuffled_answers";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_YEAR = "year";
    public static final String KEY_Marks = "marks";
    public static final String KEY_Desc = "description";
    public static final String KEY_Answe = "answers";
    public static final String KEY_user = "user";
    public static final String KEY_Quiz = "quiz";

    /**
     * ########################  variables  ###############################
     **/

    private SharedPreferences mPreferences;
    private String Result_Score;
    private String Result_Max;
    private  String TAG="AnswerQuiz";
    protected String quizId;
    protected String userID;
    protected int NUM_Questions;
    protected ArrayList<String> Questions = new ArrayList<String>();
    protected ArrayList<String[][]> Shuffled_Answers_array =  new ArrayList<String[][]>();
    HashMap<String, String> Quiz_Data = new HashMap<String, String>();
    protected   ArrayList<Integer> Output_Answers = new ArrayList<Integer>();
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    /**
     * ########################  main functions ###############################
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_quiz);

        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
       Log.e("user is", mPreferences.getString("id", ""));


        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            quizId    =  extras.getString("Qid");
            Log.e("quiz id is",quizId);
        userID= mPreferences.getString("id", "");
        Get_questions_From_server get_questions = new Get_questions_From_server();
        get_questions.execute(); // getting questions from server
        }
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
    /**
     * ########################  Custom functions ###############################
     **/
    //Get data
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
            return GET( "https://es2alny.herokuapp.com/api/quizzes/"+quizId);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            Log.e("json is",result.toString());
            handleResponse(result);
            progDailog.cancel();
        }

    }

    public static JSONObject GET(String url){
        InputStream inputStream = null;
        String result = "";
        JSONObject jsonResponse=null;
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";




            jsonResponse = new JSONObject(result);

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }


        return jsonResponse;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

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

                    String[][] TempAnswerS=new String[4][2];

                    for (int j=0;j<shuffled_answers.length();j++) {

                        TempAnswerS[j][0]=shuffled_answers.getJSONObject(j).getString(KEY_TITLE);
                        TempAnswerS[j][1]=shuffled_answers.getJSONObject(j).getString(KEY_ID);

                    }
                    //TempAnswers have all answers of one question
                    Shuffled_Answers_array.add(TempAnswerS);
                    //Shuffled answers array have all answers in all questions
                    Questions.add(title);
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
    //view data
    private void initiate_view()   {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),Questions,Shuffled_Answers_array);

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
                if(i==NUM_Questions+1)
                {

                    ((Button)mSectionsPagerAdapter.getItem(NUM_Questions+1).getView().findViewById(R.id.submitQuiz)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SubmitQuiz submitQuiz = new SubmitQuiz();
                            submitQuiz.execute("http://es2alny.herokuapp.com/api/answer_quiz");

                        }
                    });

                }

            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });




    }
    //monitor data
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
    //Post data
    private class SubmitQuiz extends AsyncTask<String,Void,JSONObject> {

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
        protected JSONObject doInBackground(String... urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urls[0]);
            String response = null;
            JSONObject json = new JSONObject();

            try{
                try {
                    JSONArray answersJSonArray = new JSONArray();
                    for (int i = 0; i < Output_Answers.size(); i++) {
                        JSONObject tempJson = new JSONObject();
                        tempJson.put(KEY_ID,Shuffled_Answers_array.get(i)[Output_Answers.get(i)][1]);
                        answersJSonArray.put(tempJson);
                    }
                    JSONObject quiz_holderJSON =new JSONObject();
                    quiz_holderJSON.put(KEY_ID,quizId);
                    JSONObject user_holderJSON =new JSONObject();
                    user_holderJSON.put(KEY_ID,userID);
                    user_holderJSON.put(KEY_Quiz,quiz_holderJSON);
                    user_holderJSON.put(KEY_Answe,answersJSonArray);
                    JSONObject Holder=new JSONObject();
                    Holder.put(KEY_user,user_holderJSON);

                   Log.e("json sent is",Holder.toString());
                    StringEntity se = new StringEntity(Holder.toString());
                    post.setEntity(se);
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-Type", "application/json");
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = client.execute(post, responseHandler);
                    json = new JSONObject(response);

                }catch (HttpResponseException e) {
                    e.printStackTrace();
                    Log.e("ClientProtocol", "" + e);
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


            try {

                Log.e("the output is", json.toString());
                Result_Score=json.getString("marks");

                ResultPageAdapter resultPageAdapter=new ResultPageAdapter(getSupportFragmentManager(),Result_Score);

                //todo put the result activity here
                progDailog.cancel();
                for(int i=0;i<mViewPager.getAdapter().getCount();i++)
                {
                    getSupportFragmentManager().beginTransaction().remove(mSectionsPagerAdapter.getItem(i)).commit();
                }
                mViewPager.removeAllViews();
                mViewPager.setAdapter(null);
                mViewPager.setAdapter(resultPageAdapter);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                super.onPostExecute(json);
            }

        }
    }
    /**
     * ########################  adapters  ###############################
     **/
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        QuestionsFragments[] fragment=new QuestionsFragments[NUM_Questions];

        QuestionsStartFragments fragmentStart=QuestionsStartFragments.newInstance(0,Quiz_Data);

        QuestionsEndFragments fragmentEnd= QuestionsEndFragments.newInstance(NUM_Questions+1);

        public SectionsPagerAdapter(FragmentManager fm,ArrayList questions, ArrayList<String[][]> answers) {
            super(fm);

            for(int i=0;i< NUM_Questions;i++)
            {
                ArrayList<String> Question_Answers = new ArrayList<String>();

                Question_Answers.add(Questions.get(i));
                Question_Answers.add(answers.get(i)[0][0]);
                Question_Answers.add(answers.get(i)[1][0]);
                Question_Answers.add(answers.get(i)[2][0]);
                Question_Answers.add(answers.get(i)[3][0]);


//
                fragment[i]=QuestionsFragments.newInstance(i,Question_Answers);

            }
        }

        @Override
        public int getCount() {


                return NUM_Questions + 2;

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

    public class ResultPageAdapter extends FragmentPagerAdapter {

        answerQuizResult answerQuizResultFragment=answerQuizResult.newInstance(Result_Score,Quiz_Data);
        public ResultPageAdapter(FragmentManager fm,String score) {

            super(fm);

        }

        @Override
        public int getCount() {
            return 1;
        }


        @Override
        public Fragment getItem(int position) {

                // i return position-1 to compensate that my first page is taken by onther fragment
                return answerQuizResultFragment;

        }

    }
    /**
     * ########################  Fragments ###############################
     */
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

            TextView t1;

            return rootView;
        }

    }

    public static class answerQuizResult extends Fragment {

        private static final String ArG_Score = "score";

        public static answerQuizResult newInstance(String Score,HashMap<String, String> Quiz_Data) {
            answerQuizResult fragment = new answerQuizResult();

            Bundle args = new Bundle();
            args.putString(ArG_Score, Score);
            args.putString(KEY_Marks,Quiz_Data.get(KEY_Marks));
            args.putString(KEY_TITLE,Quiz_Data.get(KEY_TITLE));

            fragment.setArguments(args);


            return fragment;
        }


        public answerQuizResult() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //todo here
            View rootView = inflater.inflate(R.layout.fragment_answer_quiz__result, container, false);


            ((TextView) rootView.findViewById(R.id.Result_TextView)).setText(getArguments().getString(ArG_Score)+"/"+getArguments().getString(KEY_Marks));


            return rootView;
        }
    }


}
