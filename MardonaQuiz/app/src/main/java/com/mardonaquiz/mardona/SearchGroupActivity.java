package com.mardonaquiz.mardona;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchGroupActivity extends ActionBarActivity{

    private static final String TAG = SearchGroupActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);

        final Button searchGroupButton =(Button)findViewById(R.id.search_group_button);
        searchGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchListener(searchGroupButton);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_group, menu);
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

    private void searchListener(View searchGroupButton) {
        EditText groupNameEditText = (EditText) findViewById(R.id.search_group_edit_text);
        String mGroupName = groupNameEditText.getText().toString();

        if (mGroupName.isEmpty()) {
            // Group Name is empty
            Toast.makeText(this, "Please enter a group name", Toast.LENGTH_LONG).show();
        } else if (isNetworkAvailable()) {
            SearchGroupTask searchGroupTask = new SearchGroupTask();
            searchGroupTask.execute();
        } else {
            Toast.makeText(this, "Network is unavailable", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private class SearchGroupTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object[] params) {
            GroupObject groupObject = new GroupObject(2);
            Log.v(TAG, "Description is " + groupObject.getDescription());

            return groupObject.getTitle();
        }
    }
}
