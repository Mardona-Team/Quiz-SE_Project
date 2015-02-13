package com.mardonaquiz.mardona.com.mardonaquiz.mardona.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import android.widget.Toast;

import com.mardonaquiz.mardona.com.mardonaquiz.mardona.items.PublishedQuizItem;
import com.mardonaquiz.mardona.R;
import com.mardonaquiz.mardona.com.mardonaquiz.mardona.adapters.PublishedQuizListCustomAdapter;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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


public class ViewGroupActivity extends ActionBarActivity {
    private final String KEY_TITLE = "title";
    private final String KEY_SUBJECT = "subject";
    private final String KEY_YEAR = "year";
    private final String KEY_ID = "id";
    private final String KEY_Desc = "description";


    protected String group_id;
    protected String student_id;
    protected String title;
    protected String subject ;
    protected String Year ;
    protected String description ;

    protected String membershipStatus="";


    private String TAG="View Group";


    protected JSONObject mPublished;

    protected ListView mPublishedListView;

    protected SharedPreferences mPreferences;


    private final String keyTitle = "title";
    private final String keyPublished = "published";


    private final String keyId = "refrence_id";

    private ArrayList<HashMap<String, String>> allPublished =
            new ArrayList<HashMap<String, String>>();



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

        student_id=mPreferences.getString("id","");

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            group_id    =  extras.getString(KEY_ID);
            //todo get student id from login

        }

        GetMembershipTask getMembershipTask=new GetMembershipTask();
        getMembershipTask.execute("http://es2alny.herokuapp.com/api/groups/"+group_id+"?student_id="+student_id);
        try {
            membershipStatus = getMembershipTask.get();
        }catch(Exception e){Log.d(TAG, e.getLocalizedMessage());}

        if(mPreferences.getString("Type","").equals("Instructor")) {membershipStatus= "true";}

        if(membershipStatus.equals("false")){
            setContentView(R.layout.activity_view_group_not_joined);
            joinGroup();

        }
        else {
            setContentView(R.layout.activity_view_group);
            TextView addQuiz = (TextView) findViewById(R.id.add_quiz_icon);
            if(mPreferences.getString("Type","").equals("Student"))  addQuiz.setVisibility(View.GONE);
            addQuiz.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent publishedListIntent = new Intent(ViewGroupActivity.this, QuizListActivity.class);
                    publishedListIntent.putExtra(KEY_ID,group_id);
                    startActivity(publishedListIntent);
                }
            });

            if (isNetworkAvailable()) {
                GetPublishedList getPublishedQuizList = new GetPublishedList();
                getPublishedQuizList.execute();
            }
            else {
                Toast.makeText(this, getString(R.string.networkMsg), Toast.LENGTH_LONG).show();
            }
        }
        Get_Group_info_from_server get_group_info_from_server = new Get_Group_info_from_server();
        get_group_info_from_server.execute("https://es2alny.herokuapp.com/api/groups/"+group_id); // getting questions from server


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




protected void joinGroup() {

    final Button joinGroup = (Button) findViewById(R.id.joinGroup);
           joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewGroupActivity.this)
                        .setIcon(android.R.drawable.ic_input_add)
                        .setTitle(getString(R.string.confirmTitle1))
                        .setMessage(getString(R.string.confirmationMsg1))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("this is", "join group");
                                JoinGroupAPI joinGroupAPI = new JoinGroupAPI();
                                joinGroupAPI.execute("http://es2alny.herokuapp.com/api/memberships");
                            }
                        })
                        .setNegativeButton(getString(R.string.no_msg), null)
                        .show();
            }
        });
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
        ((TextView) findViewById(R.id.Gdesc)).setText(description);



        ///end


    }

    private class Get_Group_info_from_server extends AsyncTask<String, Void, JSONObject> {

        ProgressDialog progDailog = new ProgressDialog(ViewGroupActivity.this);

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
               return GET(urls[0]);
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            progDailog.cancel();
            handleResponse(result);
        }
    }



    public static String GET_Membership(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        }catch (Exception e) {}

        return result;
    }
    private class GetMembershipTask extends AsyncTask<String, Void, String> {

        ProgressDialog progDailog = new ProgressDialog(ViewGroupActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }



        @Override
        protected String doInBackground(String... urls) {
            return GET_Membership(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            progDailog.cancel();
            membershipStatus =result ;
        }}



    //this is post class
    private class JoinGroupAPI extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {

            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urls[0]);

            String response = null;

            JSONObject joinGtoupObj = new JSONObject();

            JSONObject json =new JSONObject();

            try{
                try {

                    joinGtoupObj.put("student_id",student_id);
                    joinGtoupObj.put("group_id",group_id);

                    StringEntity se = new StringEntity(joinGtoupObj.toString());
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

            Log.e("return json is ",json.toString());

            Bundle extras = getIntent().getExtras();

            if (extras != null) {

                group_id    =  extras.getString(KEY_ID);
                student_id="1";
                //todo get student id from login

            }
            Intent intent= new Intent(ViewGroupActivity.this,ViewGroupActivity.class);
            intent.putExtra("id",group_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    }



    public void handleResponse_published(JSONObject responce) {
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
                    PublishedQuizItem publishedQuizInstance=new PublishedQuizItem(publishedTitles.get(count),mPreferences.getString("Type",""),publishedIds.get(count),group_id);
                    PublishedItems.add(publishedQuizInstance);
                }

                mPublishedListView=(ListView) findViewById(R.id.groupsListView);
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


    public static JSONObject GET_Published(String url){
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


    private class GetPublishedList extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object... arg0) {
            return GET_Published("https://es2alny.herokuapp.com/api/groups/"+group_id+"/quizzes/");
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mPublished = result;
            handleResponse_published(result);
        }

    }


}

