package com.mardonaquiz.mardona;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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


public class GroupListActivity extends ListActivity {


    public static final String TAG = GroupListActivity.class.getSimpleName();
    protected JSONObject mGroups;
    protected ProgressBar mProgressBar;

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
        intent.putExtra(KEY_TITLE,allGroups.get(position).get(KEY_TITLE));
        intent.putExtra(KEY_SUBJECT,allGroups.get(position).get(KEY_SUBJECT));
        intent.putExtra(KEY_YEAR,allGroups.get(position).get(KEY_YEAR));
        intent.putExtra(KEY_Desc,allGroups.get(position).get(KEY_Desc));

        startActivity(intent);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);


        if (isNetworkAvailable()) {
            mProgressBar.setVisibility(View.VISIBLE);
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



    public void openCreateGroup(View view) {
        //TODO replace this function with a function calling the group creation activity

    }

    public void openViewGroup(View view) {
        //TODO replace this function with a function calling the group creation activity
        Intent intent = new Intent(this, ViewGroup.class);
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
        mProgressBar.setVisibility(View.INVISIBLE);

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
                    String Subject = post.getString(KEY_SUBJECT);
                    String year = post.getString(KEY_YEAR);
                    String description = post.getString(KEY_Desc);



                    HashMap<String, String> mygroup = new HashMap<String, String>();

                    mygroup.put(KEY_ID, ID);
                    mygroup.put(KEY_TITLE, title);
                    mygroup.put(KEY_SUBJECT, Subject);
                    mygroup.put(KEY_YEAR, year);
                    mygroup.put(KEY_Desc, description);



                    allGroups.add(mygroup);
                }

                String[] keys = { KEY_TITLE, KEY_SUBJECT};
                int[] ids = { android.R.id.text1, android.R.id.text2 };
                SimpleAdapter adapter = new SimpleAdapter(this, allGroups,
                        android.R.layout.simple_list_item_2,
                        keys, ids);

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

        TextView emptyTextView = (TextView) getListView().getEmptyView();
        emptyTextView.setText("no items to be displayed");
    }

    private class GET_Group_list extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object... arg0) {
            int responseCode = -1;
            JSONObject jsonResponse = null;

            try {
                URL blogFeedUrl = new URL("http://es2alny.herokuapp.com/api/groups");
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

                    JSONArray j1=new JSONArray(responseData);
                   JSONObject j2=new JSONObject();
                    j2.put(KEY_group,j1);
                    jsonResponse = j2;
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
            mGroups = result;
            handleResponse();
        }

    }

}
