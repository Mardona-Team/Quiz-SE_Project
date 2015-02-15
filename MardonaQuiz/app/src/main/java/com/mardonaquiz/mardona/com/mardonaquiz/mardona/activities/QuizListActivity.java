package com.mardonaquiz.mardona.com.mardonaquiz.mardona.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.mardonaquiz.mardona.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle(getString(R.string.comfirm_title))
                    .setMessage(getString(R.string.confirmation_msg))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            AddPublishedQuizToAPI addPublishedQuiztoapi = new AddPublishedQuizToAPI();
                            addPublishedQuiztoapi.execute("http://es2alny.herokuapp.com/api/groups/"+groupID+"/quizzes/"+publishedId);

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
                    Intent intent =new Intent(getApplicationContext(),ViewGroupActivity.class);
                    intent.putExtra("id",groupID);
                    startActivity(intent);
                    finish();


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
            scoreItemsObject.put("myQuiz",scoreItemsArray);
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
                    String ID = post.getString("refrence_id");
                    String title = post.getString(keyTitle);


                    HashMap<String, String> myQuizzes = new HashMap<String, String>();



                    String published = post.getString(keyPublished);
                    int pstatus=Integer.parseInt(published);

                    Log.e("json is",title+""+published);

                    if(pstatus==0){
                        QuizTitles.add(title);
                        myQuizzes.put(keyID, ID);
                        myQuizzes.put(keyTitle, title);
                        allQuizzes.add(myQuizzes);
                    }


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
            return GET("http://es2alny.herokuapp.com/api/groups/"+groupID+"/quizzes");
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mQuiz = result;
            handleResponse();
        }




    }




}

