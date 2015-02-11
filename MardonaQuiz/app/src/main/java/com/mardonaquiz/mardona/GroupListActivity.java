package com.mardonaquiz.mardona;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;


public class GroupListActivity extends ListActivity {


    public static final String TAG = GroupListActivity.class.getSimpleName();
    protected JSONObject mGroups;

    private final String KEY_group = "groups";
    private final String KEY_TITLE = "title";
    private final String KEY_SUBJECT = "subject";
    private final String KEY_YEAR = "year";
    private final String KEY_ID = "id";
    private final String KEY_Desc = "description";
    private  ArrayList<HashMap<String, String>> allGroups =
            new ArrayList<HashMap<String, String>>();

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


                Intent intent = new Intent(this, ViewGroup.class);

        intent.putExtra(KEY_ID,allGroups.get(position).get(KEY_ID));

        startActivity(intent);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);



        if (isNetworkAvailable()) {
            GET_Group_list GETGrouplist = new GET_Group_list();
            GETGrouplist.execute();
        }
        else {
            Toast.makeText(this, "Network is unavailable!", Toast.LENGTH_LONG).show();
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
            scoreItemsObject.put("groups",scoreItemsArray);
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




    public void openCreateGroup(View view) {


        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivity(intent);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_list, menu);
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
        ArrayList<String> GroupTitles = new ArrayList<String>();

        if (mGroups == null) {
            updateDisplayForError();
        }
        else {
            try {
                JSONArray Mgroups = mGroups.getJSONArray(KEY_group);

                for (int i = 0; i < Mgroups.length(); i++) {
                    JSONObject post = Mgroups.getJSONObject(i);


                    String ID = post.getString(KEY_ID);
                    String title = post.getString(KEY_TITLE);




                    HashMap<String, String> mygroup = new HashMap<String, String>();

                    mygroup.put(KEY_ID, ID);
                    mygroup.put(KEY_TITLE, title);

                    GroupTitles.add(title);


                    allGroups.add(mygroup);
                }





                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, GroupTitles);
                setListAdapter(adapter);

            }
            catch (JSONException e) {
                Log.e(TAG, "Exception caught!", e);
            }
        }
    }

    private void updateDisplayForError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("connection error");
        builder.setMessage("there is a connection error");
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private class GET_Group_list extends AsyncTask<Object, Void, JSONObject> {

        ProgressDialog progDailog = new ProgressDialog(GroupListActivity.this);


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
                return GET("http://es2alny.herokuapp.com/api/groups/");
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mGroups = result;
            handleResponse();
            progDailog.cancel();
        }

    }

}
