package com.mardonaquiz.mardona;

import android.app.AlertDialog;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class StudentsScoresActivity extends ActionBarActivity {

    public static final String TAG = StudentsScoresActivity.class.getSimpleName();


    protected ListView mStudentScoresListView;

    protected JSONObject mScoreItems;

    protected final String KEY_SCORE="score_item";
    protected final String KEY_MARK="marks";
    protected final String KEY_STUDENT="student";
    protected final String KEY_ID="id";
    protected final String KEY_NAME="full_name";
    protected final String KEY_QUIZ="quiz";

    private ArrayList<StudentScoreItem> scoreItemsArrayList=new ArrayList<StudentScoreItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_scores);

        if (isNetworkAvailable()) {
            GetStudentScoreTask getStudentScoreTask=new GetStudentScoreTask();
            getStudentScoreTask.execute();
        }
        else {
            Toast.makeText(this, "Network is unavailable!", Toast.LENGTH_LONG).show();
        }

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

        StudentScoreItem studentScoreItem=null;

        if (mScoreItems == null) {

        }
        else {
            try {
                JSONArray scoreItemsJSONArray = mScoreItems.getJSONArray(KEY_SCORE);

                for (int i = 0; i < scoreItemsJSONArray.length(); i++) {
                    JSONObject scoreItemJSONObject = scoreItemsJSONArray.getJSONObject(i);


                    String studentMarks = scoreItemJSONObject.getString(KEY_MARK);

                    JSONObject studentJSONObject = scoreItemJSONObject.getJSONObject(KEY_STUDENT);
                    String studentFullName=studentJSONObject.getString(KEY_NAME);
                    String studentID=studentJSONObject.getString(KEY_ID);

                    JSONObject quizJSONObject = scoreItemJSONObject.getJSONObject(KEY_QUIZ);
                    String quizFullMark=quizJSONObject.getString(KEY_MARK);


                    float floatFullMarks = Float.parseFloat(quizFullMark);
                    float floatStudentMarks= Float.parseFloat(studentMarks);

                    boolean passing=((floatFullMarks/2)<=floatStudentMarks);

                    studentScoreItem=new StudentScoreItem(studentFullName,studentMarks,studentID,passing);

                    scoreItemsArrayList.add(studentScoreItem);

                }


                mStudentScoresListView= (ListView)findViewById(R.id.listView_students_scores);
                StudentsScoresCustomAdapter scoresAdapter= new StudentsScoresCustomAdapter(this,R.layout.item_student_score,scoreItemsArrayList);
                mStudentScoresListView.setAdapter(scoresAdapter);
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

            JSONArray scoreItemsArray=new JSONArray(result);
            JSONObject scoreItemsObject=new JSONObject();
            scoreItemsObject.put("score_item",scoreItemsArray);
            jsonResponse = scoreItemsObject;

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

    private class GetStudentScoreTask extends AsyncTask<Object, Void, JSONObject> {

        ProgressDialog progDailog = new ProgressDialog(StudentsScoresActivity.this);

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
                return GET("http://es2alny.herokuapp.com/api/groups/1/quizzes/1/users");
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mScoreItems = result;
            handleResponse();
            progDailog.cancel();
        }

    }




}
