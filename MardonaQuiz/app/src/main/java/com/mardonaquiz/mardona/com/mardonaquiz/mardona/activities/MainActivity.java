package com.mardonaquiz.mardona.com.mardonaquiz.mardona.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mardonaquiz.mardona.com.mardonaquiz.mardona.items.ObjectDrawerItem;
import com.mardonaquiz.mardona.com.mardonaquiz.mardona.fragments.ProfileFragement;
import com.mardonaquiz.mardona.R;
import com.mardonaquiz.mardona.com.mardonaquiz.mardona.adapters.DrawerItemCustomAdapter;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    private SharedPreferences mPreferences;
    private Button mLogOutButton;
    private final static String LOGOUT_API_ENDPOINT_URL = "http://es2alny.herokuapp.com/api/sessions";

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ArrayList<ObjectDrawerItem> drawerItem = new ArrayList<ObjectDrawerItem>();


        drawerItem.add(new ObjectDrawerItem(R.drawable.abc_ab_share_pack_holo_dark, "Profile"));
        if (mPreferences.getString("Type", "").equals("Instructor")){
            drawerItem.add(new ObjectDrawerItem(R.drawable.abc_ab_share_pack_holo_dark, "Create Group"));
            drawerItem.add(new ObjectDrawerItem(R.drawable.abc_ab_share_pack_holo_dark, "Create Quiz"));
    }
            drawerItem.add(new ObjectDrawerItem(R.drawable.abc_ab_share_pack_holo_dark, "Log Out"));



        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }




        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (!mPreferences.contains("AuthToken")) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
           }


        Bundle userData=new Bundle();
        userData.putString("user_fullname",mPreferences.getString("first_name","")+" "+mPreferences.getString("last_name",""));
        userData.putString("Type",mPreferences.getString("Type",""));
        Fragment fragment =new ProfileFragement();
        fragment.setArguments(userData);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new ProfileFragement();
                Bundle userData=new Bundle();
                userData.putString("user_fullname",mPreferences.getString("first_name","")+" "+mPreferences.getString("last_name",""));
                userData.putString("Type",mPreferences.getString("Type",""));
                fragment.setArguments(userData);
                break;
            case 1:
                if(mPreferences.getString("Type","").equals("Student")) logOut(mLogOutButton);
                else {
                    Intent createGroupintent = new Intent(this,CreateGroupActivity.class);
                    startActivity(createGroupintent);
                }
                break;
            case 2:
                Intent createQuizintent = new Intent(this, CreateQuizActivity.class);
                startActivity(createQuizintent);
                break;
            case 3:
                logOut(mLogOutButton);
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }




    public void logOut(View button) {

            LogOutTask logOutTask = new LogOutTask();
             logOutTask.execute(LOGOUT_API_ENDPOINT_URL+"?auth_token="+mPreferences.getString("AuthToken",""));

    }


    private class LogOutTask extends AsyncTask<String,Void,JSONObject> {

        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog progDailog = new ProgressDialog(MainActivity.this);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }


        @Override
        protected JSONObject doInBackground(String... urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpDelete delete = new HttpDelete(urls[0]);
            String response = null;
            JSONObject json = new JSONObject();



            try {
                try {
                    // setup the returned values in case
                    // something goes wrong
                    json.put("success", false);
                    json.put("info", "Something went wrong. Retry!");

                    delete.setHeader("Accept", "application/json");
                  	delete.setHeader("Content-Type", "application/json");
                    //delete.setHeader("Auth_Token",mPreferences.getString("AuthToken",""));


                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = client.execute(delete,responseHandler);
                    json = new JSONObject(response);

                } catch (HttpResponseException e) {
                    e.printStackTrace();
                    Log.e("ClientProtocol", "" + e);
                    json.put("info", "Email and/or password are invalid. Retry!");
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


                    SharedPreferences.Editor editor = mPreferences.edit();
                    // save the returned auth_token into
                    // the SharedPreferences
                    editor.remove("AuthToken");
                    editor.commit();
                    // launch the HomeActivity and close this one
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                Toast.makeText(getApplicationContext(), json.getString("info"), Toast.LENGTH_LONG).show();
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
