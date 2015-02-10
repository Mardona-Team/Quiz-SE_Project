package com.mardonaquiz.mardona;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;

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
import java.util.HashMap;


public class publishedQuizListActivity extends ActionBarActivity {


    public static final String TAG = publishedQuizListActivity.class.getSimpleName();
    protected JSONObject mPublished;

    protected ListView mPublishedListView;

    protected SharedPreferences mPreferences;


    private final String keyTitle = "title";
    private final String keyPublished = "published";


    private final String keyId = "id";

    private ArrayList<HashMap<String, String>> allPublished =
            new ArrayList<HashMap<String, String>>();



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_published_quizzes);

        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);






        if (isNetworkAvailable()) {
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
        getMenuInflater().inflate(R.menu.menu_published_quizzes, menu);
        MenuItem item = menu.findItem(R.id.publish_quiz_actionBar);
        if(mPreferences.getString("Type","").equals("Student")) item.setVisible(false);
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

        if(id==R.id.publish_quiz_actionBar){
            Intent intent= new Intent(publishedQuizListActivity.this,QuizListActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void handleResponse(JSONObject responce) {
        ArrayList<PublishedQuizItem> PublishedItems = new ArrayList<PublishedQuizItem>();
        ArrayList<String> publishedTitles= new ArrayList<String>();
        ArrayList<String> publishedIds= new ArrayList<String>();


        if (responce == null) {
            updateDisplayForError();
        }


        else {
            try {
                JSONArray MPublished = responce.getJSONArray(keyPublished);

                for (int i = 0; i < MPublished.length(); i++) {
                    JSONObject post = MPublished.getJSONObject(i);
                    String ID = post.getString(keyId);
                    String title = post.getString(keyTitle);
                    String published = post.getString(keyPublished);
                    int pstatus=Integer.parseInt(published);

                    Log.e("json is",title+""+published);

                   if(pstatus==1){
                    publishedTitles.add(title);
                       publishedIds.add(ID);

                    }

                }

                for(int count=0;count<publishedTitles.size();count++){
                    PublishedQuizItem publishedQuizInstance=new PublishedQuizItem(publishedTitles.get(count),mPreferences.getString("Type",""),publishedIds.get(count));
                    PublishedItems.add(publishedQuizInstance);
                }

                mPublishedListView=(ListView) findViewById(R.id.published_list_view);
                PublishedQuizListCustomAdapter adapter = new PublishedQuizListCustomAdapter(this,R.layout.item_published_quiz,PublishedItems);
                mPublishedListView.setAdapter(adapter);
            }
            catch (JSONException e) {
                Log.e(TAG, "Exception caught!", e);
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
            scoreItemsObject.put("published",scoreItemsArray);
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


    private class GetPublishedList extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object... arg0) {
            return GET("https://es2alny.herokuapp.com/api/groups/2/quizzes/");
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mPublished = result;
            handleResponse(result);
        }

    }




}

