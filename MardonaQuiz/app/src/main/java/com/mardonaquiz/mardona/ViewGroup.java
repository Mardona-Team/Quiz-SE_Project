package com.mardonaquiz.mardona;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ViewGroup extends ActionBarActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            group_id    =  extras.getString(KEY_ID);

            Get_Group_info_from_server get_group_info_from_server = new Get_Group_info_from_server();
            get_group_info_from_server.execute(); // getting questions from server


        }





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
            int responseCode = -1;
            JSONObject jsonResponse = null;

            try {
                URL blogFeedUrl = new URL("https://es2alny.herokuapp.com/api/groups/"+group_id);
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

}
