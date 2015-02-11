package com.mardonaquiz.mardona.com.mardonaquiz.mardona.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.mardonaquiz.mardona.R;
import com.mardonaquiz.mardona.com.mardonaquiz.mardona.items.StudentAnswerItem;
import com.mardonaquiz.mardona.com.mardonaquiz.mardona.items.StudentScoreItem;
import com.mardonaquiz.mardona.com.mardonaquiz.mardona.adapters.StudentAnswerCustomAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class StudentAnswersActivity extends ActionBarActivity {

    public static final String TAG = StudentAnswersActivity.class.getSimpleName();

    protected ListView mStudentAnswerList;
    protected JSONObject mStudentAnswers;

    protected final String KEY_STUDENT_ANSWERS="student_answers";
    protected final String KEY_QUIZ="quiz";
    protected final String KEY_QUESTIONS="questions";
    protected final String KEY_TITLE="title";
    protected final String KEY_RIGHT_ANSWER="right_answer";
    protected final String KEY_ANSWERS="answers";




    //Dummy DAta
//    StudentAnswerItem[] dummyanswers= {new StudentAnswerItem("Q1","Paris","Paris"),new StudentAnswerItem("Q2","Cairo","Paris"),new StudentAnswerItem("Q3","London","London"),new StudentAnswerItem("Q4","LiverPool","Paris")};
    ArrayList<StudentAnswerItem> mStudentAnswersArrayList=new ArrayList<StudentAnswerItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_answers);

        if (isNetworkAvailable()) {
            GetStudentAnswersTask getStudentAnswersTask=new GetStudentAnswersTask();
            getStudentAnswersTask.execute();
        }
        else {
            Toast.makeText(this, "Network is unavailable!", Toast.LENGTH_LONG).show();
        }
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

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    public void handleResponse() {

        ArrayList<String> questionTitles = new ArrayList<String>();
        ArrayList<String> rightAnswers = new ArrayList<String>();
        ArrayList<String> studentAnswers = new ArrayList<String>();


        StudentScoreItem studentAnswerItem=null;

        if (mStudentAnswers == null) {

        }
        else {
            try {

                    JSONObject quizJSONObject = mStudentAnswers.getJSONObject(KEY_QUIZ);

                    JSONArray questionJSONArray=quizJSONObject.getJSONArray(KEY_QUESTIONS);
                    for(int j=0; j< questionJSONArray.length();j++){
                        JSONObject questionJSONObject = questionJSONArray.getJSONObject(j);
                        String questionTitle=questionJSONObject.getString(KEY_TITLE);
                        questionTitles.add(j,questionTitle);

                        JSONObject rightAnswerJSONObject=questionJSONObject.getJSONObject(KEY_RIGHT_ANSWER);
                        String rightAnswerTitle=rightAnswerJSONObject.getString(KEY_TITLE);
                        rightAnswers.add(j,rightAnswerTitle);
                    }

                    JSONArray answerJSONArray=quizJSONObject.getJSONArray(KEY_ANSWERS);
                    for(int k=0; k< answerJSONArray.length();k++){
                        JSONObject answerJSONObject = answerJSONArray.getJSONObject(k);
                        String answerTitle=answerJSONObject.getString(KEY_TITLE);
                        studentAnswers.add(k,answerTitle);
                    }

                 for(int count=0;count< studentAnswers.size();count++){
                     StudentAnswerItem studentAnswerInstance=new StudentAnswerItem(questionTitles.get(count),
                                                                                    studentAnswers.get(count),
                                                                                     rightAnswers.get(count));
                     mStudentAnswersArrayList.add(studentAnswerInstance);
                 }




                mStudentAnswerList= (ListView)findViewById(R.id.student_answers_listview);
                StudentAnswerCustomAdapter answersAdapter= new StudentAnswerCustomAdapter(this,R.layout.item_student_answer,mStudentAnswersArrayList);
                mStudentAnswerList.setAdapter(answersAdapter);
            }
            catch (JSONException e) {
                Log.e(TAG, "Exception caught!", e);
            }
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

            JSONObject scoreItemsObject=new JSONObject(result);
            jsonResponse = scoreItemsObject;

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }


        return jsonResponse;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class GetStudentAnswersTask extends AsyncTask<Object, Void, JSONObject> {

        ProgressDialog progDailog = new ProgressDialog(StudentAnswersActivity.this);

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
            return GET("http://es2alny.herokuapp.com/api/groups/1/quizzes/1/users/15");
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mStudentAnswers = result;
            handleResponse();
            progDailog.cancel();
        }

    }


}
