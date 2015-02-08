package com.mardonaquiz.mardona;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
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


public class QuizListActivity extends ListActivity {


    public static final String TAG = QuizListActivity.class.getSimpleName();
    protected JSONObject mQuiz;
    protected ProgressBar mProgressBar;


    private final String keyTitle = "title";
    private final String keyQuiz = "myQuiz";
    private final String keyPublished = "published";


    private String  groupID;
    private String instructorID;
    private final String keyID = "id";

     private String publishedId = null;
    private String publishedTitle = null;
    private String publishedFlag = null;
    final int position =0;

    private ArrayList<HashMap<String, String>> allQuizzes =
            new ArrayList<HashMap<String, String>>();



    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);


        publishedId = allQuizzes.get(position).get(keyID);
        publishedTitle = allQuizzes.get(position).get(keyTitle);
        publishedFlag = allQuizzes.get(position).get(keyPublished);

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.comfirm_title))
                    .setMessage(getString(R.string.confirmation_msg))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            AddPublishedQuizToAPI addPublishedQuiztoapi = new AddPublishedQuizToAPI();
                            addPublishedQuiztoapi.execute("http://es2alny.herokuapp.com/api/groups/2/quizzes/3");

                          /*  Intent intent = new Intent(QuizListActivity.this, publishedQuizListActivity.class);
                            intent.putExtra(keyID, allQuizzes.get(position).get(keyID));

                            startActivity(intent);
                            finish(); */




                        }

                    })
                    .setNegativeButton(getString(R.string.no_msg), null)
                    .show();



    }




       private class AddPublishedQuizToAPI extends AsyncTask<String,Void,JSONObject> {

            @Override
            protected JSONObject doInBackground(String... urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpPut put = new HttpPut(urls[0]);
                String response = null;
                JSONObject json=new JSONObject();

                try{
                    try {
                        put.setHeader("Accept", "application/json");
                        put.setHeader("Content-Type", "application/json");

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        response = client.execute(put, responseHandler);
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
                    // launch the HomeActivity and close this one
Log.e("the responce is ",json.toString());


                    Toast.makeText(getApplicationContext(),"Quiz has been published successfully!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    super.onPostExecute(json);
                }

            }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_quiz_list);


          mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);


          if (isNetworkAvailable()) {
              mProgressBar.setVisibility(View.VISIBLE);
              GetQuizList GetQuizList = new QuizListActivity.GetQuizList();
              GetQuizList.execute();
          }
          else {
              Toast.makeText(this, getString(R.string.networkMsg), Toast.LENGTH_LONG).show();
          }

          groupID = getIntent().getStringExtra(keyID);
        instructorID="1";
        //todo get instuctor id


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
        ArrayList<String> QuizTitles = new ArrayList<String>();

        if (mQuiz == null) {
            updateDisplayForError();
        }
        else {
            try {
                JSONArray MQuiz = mQuiz.getJSONArray(keyQuiz);

                for (int i = 0; i < MQuiz.length(); i++) {
                    JSONObject post = MQuiz.getJSONObject(i);
                    String ID = post.getString(keyID);
                    String title = post.getString(keyTitle);


                    HashMap<String, String> myQuizzes = new HashMap<String, String>();
                    myQuizzes.put(keyID, ID);
                    myQuizzes.put(keyTitle, title);


                    String published = post.getString(keyPublished);
                    int pstatus=Integer.parseInt(published);

                    Log.e("json is",title+""+published);

                    if(pstatus==0){
                        QuizTitles.add(title);

                    }


                    allQuizzes.add(myQuizzes);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, QuizTitles);
                setListAdapter(adapter);
            }
            catch (JSONException e) {
                //Log.e(TAG, "Exception caught!", e);
            }
        }
    }

    private void updateDisplayForError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.connectionError));
        builder.setMessage(getString(R.string.networkMsg));
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class GetQuizList extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object... arg0) {
            int responseCode = -1;
            JSONObject jsonResponse = null;

            try {
                URL blogFeedUrl = new URL("http://es2alny.herokuapp.com/api/groups/"+groupID+"/quizzes");
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
                    jsonObject.put(keyQuiz,jsonArray);
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
            mQuiz = result;
            handleResponse();
        }

    }




}

