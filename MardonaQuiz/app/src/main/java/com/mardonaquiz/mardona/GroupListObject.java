package com.mardonaquiz.mardona;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ali on 29-Jan-15.
 */
public class GroupListObject {

    private JSONArray jsonGroups;

    public GroupListObject() {
        GetApiResponse apiResponse = new GetApiResponse("es2alny.herokuapp.com/api/groups", "TAG TO BE ADDED");
        String dataResponse = apiResponse.getResponseData();

        try {
            jsonGroups = new JSONArray(dataResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public GroupObject getElementByID(int id) {
        try {
            int dataLength = jsonGroups.length();
            for (int i = 0; i < dataLength; i++) {
                JSONObject element = jsonGroups.getJSONObject(i);
                if (element.getString("id") == String.valueOf(id)) {
                    return new GroupObject(element);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GroupObject getElementByIndex(int index) {
        try {
            return new GroupObject(jsonGroups.getJSONObject(index));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
