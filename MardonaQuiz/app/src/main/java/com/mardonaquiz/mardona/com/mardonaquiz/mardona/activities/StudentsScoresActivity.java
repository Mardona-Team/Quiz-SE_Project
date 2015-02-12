package com.mardonaquiz.mardona.com.mardonaquiz.mardona.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mardonaquiz.mardona.R;
import com.mardonaquiz.mardona.com.mardonaquiz.mardona.items.StudentScoreItem;
import com.mardonaquiz.mardona.com.mardonaquiz.mardona.adapters.StudentsScoresCustomAdapter;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
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


public class StudentsScoresActivity extends ActionBarActivity {

    public static final String TAG = StudentsScoresActivity.class.getSimpleName();


    protected ListView mStudentScoresListView;
    protected Button showStatstics;
    protected TextView quizTitle;

    protected JSONObject mScoreItems;

    protected final String KEY_SCORE="score_item";
    protected final String KEY_MARK="marks";
    protected final String KEY_STUDENT="student";
    protected final String KEY_ID="id";
    protected final String KEY_NAME="full_name";
    protected final String KEY_QUIZ="quiz";

    private String group_id,quiz_id,user_id;
    protected SharedPreferences mPreferences;

    private ArrayList<StudentScoreItem> scoreItemsArrayList=new ArrayList<StudentScoreItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_scores);

        group_id=getIntent().getStringExtra("Gid");
        quiz_id=getIntent().getStringExtra("Qid");

        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

        user_id=mPreferences.getString("id","");


        String quizTitleString=getIntent().getStringExtra("quiz_title");
        quizTitle=(TextView) findViewById(R.id.quiz_title_header);
        quizTitle.setText(quizTitleString);



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
        float fullMark=0;

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

                    studentScoreItem=new StudentScoreItem(studentFullName,studentMarks,studentID,quiz_id,group_id,passing);

                    scoreItemsArrayList.add(studentScoreItem);
                    fullMark=floatFullMarks;

                }


                mStudentScoresListView= (ListView)findViewById(R.id.listView_students_scores);
                StudentsScoresCustomAdapter scoresAdapter= new StudentsScoresCustomAdapter(this,R.layout.item_student_score,scoreItemsArrayList);
                mStudentScoresListView.setAdapter(scoresAdapter);

                final float pieChartFullMark=fullMark;
                showStatstics=(Button)findViewById(R.id.pie_chart_button);
                showStatstics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openChart(getPieChartData(scoreItemsArrayList, pieChartFullMark));
                    }
                });
            }
            catch (JSONException e) {
                Log.e(TAG, "Exception caught!", e);
            }
        }
    }

    public int[] getPieChartData(ArrayList<StudentScoreItem> scores,float fullMark){
        int excellent=0,veryGood=0,good=0,passed=0,failed=0;
        float grade=0;

         for (int i=0;i< scores.size();i++){
             grade=Float.parseFloat(scores.get(i).grade)/fullMark;

             if(grade>0.85) excellent++;
             else if (grade>0.75) veryGood++;
             else if (grade>0.65) good++;
             else if (grade>0.5) passed++;
             else if (grade<0.5) failed++;
         }
        int[] gradesDistibution={excellent,veryGood,good,passed,failed};
        return gradesDistibution;
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
                return GET("http://es2alny.herokuapp.com/api/groups/"+group_id+"/quizzes/"+quiz_id+"/users");
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mScoreItems = result;
            handleResponse();
            progDailog.cancel();
        }

    }


    private void openChart(int[] pieChartData) {

        // Pie Chart Section Names
        String[] code = new String[]{
                "Excellent", "Very Good", "Good", "Passed",
                "Failed"
        };

        // Pie Chart Section Value
        double[] distribution = {pieChartData[0], pieChartData[1],pieChartData[2],pieChartData[3],pieChartData[4]};

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

        Intent intent = ChartFactory.getPieChartIntent(StudentsScoresActivity.this, distributionSeries, defaultRenderer, "AChartEnginePieChartDemo");
        startActivity(intent);
    }


}
