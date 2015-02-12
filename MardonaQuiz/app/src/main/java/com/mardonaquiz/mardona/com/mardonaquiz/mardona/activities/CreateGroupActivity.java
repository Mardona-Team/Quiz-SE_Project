package com.mardonaquiz.mardona.com.mardonaquiz.mardona.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.mardonaquiz.mardona.R;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class CreateGroupActivity extends ActionBarActivity {

    private final static String REGISTER_API_ENDPOINT_URL = "http://es2alny.herokuapp.com/api/groups";
    //TODO Get instructor ID
    private String instructor_id;
    private String GroupName;
    private String GroupTitle;
    private String GroupYear;
    private String GroupSubj;
    private String GroupDesc;
    private SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        instructor_id=mPreferences.getString("id","");
        Button b1=(Button) findViewById(R.id.submit);
        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                create_group();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
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
    public void create_group(){

        GroupTitle=((EditText) findViewById(R.id.Group_title)).getText().toString();
        GroupYear=((EditText) findViewById(R.id.Group_year)).getText().toString();
        GroupSubj=((EditText) findViewById(R.id.Group_subject)).getText().toString();
        GroupDesc=((EditText) findViewById(R.id.Group_desc)).getText().toString();
        //TODO get a uniq group name
        GroupName=GroupTitle;


        if(GroupTitle.length()==0||GroupYear.length()==0||GroupSubj.length()==0)
        {
            Toast.makeText(this, getString(R.string.empty_field_error), Toast.LENGTH_LONG).show();
        }
        else{
            AddGroupToAPI addGrouptoapi = new AddGroupToAPI();
            addGrouptoapi.execute(REGISTER_API_ENDPOINT_URL);
        }
    }
    private class AddGroupToAPI extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {

            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urls[0]);
            String response = null;

            JSONObject groupObj = new JSONObject();
            JSONObject holder = new JSONObject();
            JSONObject json = new JSONObject();
            try{
                try {
                    groupObj.put("instructor_id",instructor_id);
                    groupObj.put("group_name",GroupName);
                    groupObj.put("title",GroupTitle);
                    groupObj.put("year",GroupYear);
                    groupObj.put("subject",GroupSubj);
                    groupObj.put("description",GroupDesc);

                    StringEntity se = new StringEntity(groupObj.toString());
                    post.setEntity(se);

                    Log.e("group json is",groupObj.toString());
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-Type", "application/json");
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = client.execute(post, responseHandler);
                    json = new JSONObject(response);

                    Log.e("JSON sent", "" + json);



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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();


                Toast.makeText(getApplicationContext(),GroupTitle+ " " +getString(R.string.Group_Created), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                super.onPostExecute(json);
            }

        }
    }

}
