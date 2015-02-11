package com.mardonaquiz.mardona;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;


public class ProfileFragement extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static final String TAG = GroupListActivity.class.getSimpleName();
    protected JSONObject mGroups;

    private final String KEY_group = "groups";
    private final String KEY_TITLE = "title";
    private final String KEY_SUBJECT = "subject";
    private final String KEY_YEAR = "year";
    private final String KEY_ID = "id";
    private final String KEY_Desc = "description";
    private ArrayList<HashMap<String, String>> allGroups =
            new ArrayList<HashMap<String, String>>();


    protected TextView userFullName,userRole;
    protected ListView groupsListView;
    protected SharedPreferences mPreferences;


    public ProfileFragement() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

            GET_Group_list GETGrouplist = new GET_Group_list();
            GETGrouplist.execute();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        userFullName=(TextView) rootView.findViewById(R.id.user_name_profile);
        userRole=(TextView) rootView.findViewById(R.id.user_role_profile);
        userFullName.setText(getArguments().getString("user_fullname"));
        userRole.setText(getArguments().getString("Type"));

        groupsListView=(ListView) rootView.findViewById(R.id.groupsListView);
        groupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), com.mardonaquiz.mardona.ViewGroup.class);

                intent.putExtra(KEY_ID,allGroups.get(i).get(KEY_ID));

                startActivity(intent);
            }
        });
        return rootView;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


//    private boolean isNetworkAvailable() {
//        ConnectivityManager manager = (ConnectivityManager)
//        getSystemServic(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
//
//        boolean isAvailable = false;
//        if (networkInfo != null && networkInfo.isConnected()) {
//            isAvailable = true;
//        }
//
//        return isAvailable;
//    }

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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }




    public void openCreateGroup(View view) {


        Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
        startActivity(intent);

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




                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, GroupTitles);
                groupsListView.setAdapter(adapter);

            }
            catch (JSONException e) {
                Log.e(TAG, "Exception caught!", e);
            }
        }
    }

    private void updateDisplayForError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("connection error");
        builder.setMessage("there is a connection error");
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();

//        TextView emptyTextView = (TextView) getListView().getEmptyView();
//        emptyTextView.setText("no items to be displayed");
    }

    private class GET_Group_list extends AsyncTask<Object, Void, JSONObject> {

        ProgressDialog progDailog = new ProgressDialog(getActivity());

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
            return GET("http://es2alny-test.herokuapp.com/api/groups/");
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mGroups = result;
            handleResponse();
            progDailog.cancel();
        }

    }

}
