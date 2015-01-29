package com.mardonaquiz.mardona;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ali on 29-Jan-15.
 */
public class GroupObject {

    private JSONObject groupObject;

    public GroupObject(int id) {
        GetApiResponse apiResponse = new GetApiResponse("http://es2alny.herokuapp.com/api/groups/" + id, "TAG TO BE ADDED");
        String dataResponse = apiResponse.getResponseData();
        Log.v("Message", dataResponse);
        try {
            groupObject = new JSONObject(dataResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public GroupObject(JSONObject jsonObject){
        groupObject = jsonObject;
    }

    private String getValueByKey(String key) {
        try {
            return groupObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getID() {
        return getValueByKey("id");
    }
    public String getInstructor() {
        return getValueByKey("instructor_id");
    }
    public String getTitle() {
        return getValueByKey("title");
    }
    public String getYear() {
        return getValueByKey("year");
    }
    public String getSubject() {
        return getValueByKey("subject");
    }
    public String getDescription() {
        return getValueByKey("description");
    }
    private String getTimeCreated() {
        return getValueByKey("created_at");
    }
    private String getTimeUpdated() {
        return getValueByKey("updated_at");
    }
}
