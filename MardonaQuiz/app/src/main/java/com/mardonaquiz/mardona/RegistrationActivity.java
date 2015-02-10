package com.mardonaquiz.mardona;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class RegistrationActivity extends ActionBarActivity {

    private final static String REGISTER_API_ENDPOINT_URL = "http://es2alny.herokuapp.com/api/registrations";
    private SharedPreferences mPreferences;
    private String mUserEmail;
    private String mFirstName;
    private String mLastName;
    private String mRole;
    private String mUserPassword;
    private String mUserPasswordConfirmation;

    private Spinner mRoleSpinner;
    private Button mSignUpButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


       getSupportActionBar().hide();

        addItemsOnRoleSpinner();

        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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


    public void addItemsOnRoleSpinner() {

        mRoleSpinner = (Spinner) findViewById(R.id.role);
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Student");
        list.add("Instructor");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRoleSpinner.setAdapter(dataAdapter);
        mRoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mRole=list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mRole=list.get(0);

            }
        });


        mSignUpButton=(Button)findViewById(R.id.signUp_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewAccount(mSignUpButton);
            }
        });

    }


    public void registerNewAccount(View button) {
        EditText userEmailField = (EditText) findViewById(R.id.email_SignUp_Field);
        mUserEmail = userEmailField.getText().toString();
        EditText firstNameField = (EditText) findViewById(R.id.first_name);
        mFirstName = firstNameField.getText().toString();
        EditText lastNameField = (EditText) findViewById(R.id.last_name);
        mLastName = lastNameField.getText().toString();
        EditText userPasswordField = (EditText) findViewById(R.id.password_signUp);
        mUserPassword = userPasswordField.getText().toString();
        EditText userPasswordConfirmationField = (EditText) findViewById(R.id.password_confirm);
        mUserPasswordConfirmation = userPasswordConfirmationField.getText().toString();


        if (mUserEmail.length() == 0 || mFirstName.length() == 0 ||mLastName.length() == 0
                || mUserPassword.length() == 0 || mUserPasswordConfirmation.length() == 0 || mRole==null) {
            // input fields are empty
            Toast.makeText(this, "Please complete all the fields",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            if (!mUserPassword.equals(mUserPasswordConfirmation)) {
                // password doesn't match confirmation
                Toast.makeText(this, "Your password doesn't match confirmation, check again",
                        Toast.LENGTH_LONG).show();
                return;
            } else {
                // everything is ok!
                RegisterTask registerTask = new RegisterTask();
                registerTask.execute(REGISTER_API_ENDPOINT_URL);
            }
        }
    }



    private class RegisterTask extends AsyncTask<String,Void,JSONObject> {

        ProgressDialog progDailog = new ProgressDialog(RegistrationActivity.this);

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
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urls[0]);
            JSONObject holder = new JSONObject();
            JSONObject userObj = new JSONObject();
            String response = null;
            JSONObject json = new JSONObject();

            try {
                try {
                    // setup the returned values in case
                    // something goes wrong
                    json.put("success", false);
                    json.put("info", "Something went wrong. Retry!");

                    // add the users's info to the post params
                    userObj.put("email", mUserEmail);
                    userObj.put("first_name", mFirstName);
                    userObj.put("last_name", mLastName);
                    userObj.put("password", mUserPassword);
                    userObj.put("password_confirmation", mUserPasswordConfirmation);
                    userObj.put("type",mRole);
                    userObj.put("username",mFirstName+mLastName);
                    holder.put("user", userObj);
                    StringEntity se = new StringEntity(holder.toString());
                    post.setEntity(se);

                    // setup the request headers
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-Type", "application/json");

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = client.execute(post, responseHandler);
                    json = new JSONObject(response);

                } catch (HttpResponseException e) {
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
                if (json.getBoolean("success")) {
                    // everything is ok
                    SharedPreferences.Editor editor = mPreferences.edit();
                    // save the returned auth_token into
                    // the SharedPreferences
                    editor.putString("AuthToken", json.getJSONObject("data").getString("auth_token"));
                    editor.putString("Type",mRole);
                    editor.putString("first_name", json.getJSONObject("data").getString("first_name"));
                    editor.putString("last_name", json.getJSONObject("data").getString("last_name"));
                    editor.putString("id", json.getJSONObject("data").getString("id"));
                    editor.commit();

                    // launch the HomeActivity and close this one
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                Toast.makeText(getApplicationContext(), json.getString("info"), Toast.LENGTH_LONG).show();
                progDailog.cancel();
            } catch (Exception e) {
                // something went wrong: show a Toast
                // with the exception message
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                super.onPostExecute(json);
            }
        }
    }
}
