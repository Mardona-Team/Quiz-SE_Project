package com.mardonaquiz.mardona;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class publishedQuizListActivity extends ListActivity {


    public static final String TAG = publishedQuizListActivity.class.getSimpleName();
    protected JSONObject mPublished;
    protected ProgressBar mProgressBar;


    private final String keyTitle = "title";
    private final String keyPublished = "published";


    private final String keyId = "id";

    private ArrayList<HashMap<String, String>> allPublished =
            new ArrayList<HashMap<String, String>>();



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);


        if (isNetworkAvailable()) {
            mProgressBar.setVisibility(View.VISIBLE);
            GetPublishedList getPublishedQuizList = new GetPublishedList();
            getPublishedQuizList.execute();
        }
        else {
            Toast.makeText(this, getString(R.string.networkMsg), Toast.LENGTH_LONG).show();
        }


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





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz_list, menu);
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

    public void handleResponse() {
        mProgressBar.setVisibility(View.INVISIBLE);
        ArrayList<String> PublishedTitles = new ArrayList<String>();

        if (mPublished == null) {
            updateDisplayForError();
        }


        else {
            try {
                JSONArray MPublished = mPublished.getJSONArray(keyTitle);

                for (int i = 0; i < MPublished.length(); i++) {
                    JSONObject post = MPublished.getJSONObject(i);
                    String ID = post.getString(keyId);
                    String title = post.getString(keyTitle);
                    String published =post.getString(keyPublished);

                    HashMap<String, String> myPublished = new HashMap<String, String>();
                    myPublished.put(keyId, ID);
                    myPublished.put(keyTitle, title);
                    myPublished.put(keyPublished, published);
                    PublishedTitles.add(title);
                    allPublished.add(myPublished);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, PublishedTitles);
                setListAdapter(adapter);
            }
            catch (JSONException e) {
                //Log.e(TAG, "Exception caught!", e);
            }
        }
    }

    private void updateDisplayForError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("connection error");
        builder.setMessage(getString(R.string.Error_msg));
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private class GetPublishedList extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object... arg0) {
            int responseCode = -1;
            JSONObject jsonResponse = null;

            try {
                URL blogFeedUrl = new URL(" https://es2alny.herokuapp.com/api/groups/2/quizzes");
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
                    JSONArray jsonArray=new JSONArray(responseData);
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put(keyPublished,jsonArray);
                    jsonResponse = jsonObject;

                }
                else {
                    Log.i(TAG, "Unsuccessful HTTP Response Code: " + responseCode);
                }
            }
            catch (MalformedURLException e) {
                // Log.e(TAG, "Exception caught: ", e);
            }
            catch (IOException e) {
                //  Log.e(TAG, "Exception caught: ", e);
            }
            catch (Exception e) {
                //  Log.e(TAG, "Exception caught: ", e);
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mPublished = result;
            handleResponse();
        }

    }




}

