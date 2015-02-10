package com.mardonaquiz.mardona;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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



public class ViewGroup extends ActionBarActivity {
    private SharedPreferences mPreferences;
    private final String KEY_TITLE = "title";
    private final String KEY_SUBJECT = "subject";
    private final String KEY_YEAR = "year";
    private final String KEY_ID = "id";
    private final String KEY_Desc = "description";


    protected String group_id;
    protected String title;
    protected String subject ;
    protected String Year ;
    protected String description ;


    private String TAG="View Group";



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);



        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            group_id    =  extras.getString(KEY_ID);

            Get_Group_info_from_server get_group_info_from_server = new Get_Group_info_from_server();
            get_group_info_from_server.execute(); // getting questions from server


        }
        Button PublishedList = (Button) findViewById(R.id.published);

        PublishedList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent publishedListIntent = new Intent(ViewGroup.this, publishedQuizListActivity.class);
                String keyQuiz = null;
                publishedListIntent.putExtra(KEY_ID,group_id);
                startActivity(publishedListIntent);

            }
        });


    }
    public void openAnswersQuiz(View view) {
        //TODO replace this function with a function calling the group creation activity
        Intent intent = new Intent(this, AnswerQuiz.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_group, menu);
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

    public void handleResponse(JSONObject result) {



        if (result == null) {
            //Todo here the errore messege
        }
        else {
            try {


                    title  = result.getString(KEY_TITLE);
                    subject = result.getString(KEY_SUBJECT);
                    Year = result.getString(KEY_YEAR);
                    description = result.getString(KEY_Desc);



                initiate_view();

            }
            catch (JSONException e) {
                Log.e("", "Exception caught!", e);
            }
        }
    }

    private void initiate_view()   {

        //start
        ((TextView) findViewById(R.id.Gtitle)).setText(title);
        ((TextView) findViewById(R.id.Gsubject)).setText(subject);
        ((TextView) findViewById(R.id.Gyear)).setText(Year);
        ((TextView) findViewById(R.id.Gdesc)).setText(description);



        ///end


    }

    private class Get_Group_info_from_server extends AsyncTask<Object, Void, JSONObject> {

        ProgressDialog progDailog = new ProgressDialog(ViewGroup.this);

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
               return GET("https://es2alny.herokuapp.com/api/groups/"+group_id);
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            progDailog.cancel();
            handleResponse(result);
        }
    }

}