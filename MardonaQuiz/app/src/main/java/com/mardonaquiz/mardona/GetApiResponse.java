package com.mardonaquiz.mardona;

import android.util.Log;

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

/**
 * Created by Ali on 28-Jan-15.
 */
public class GetApiResponse {

    private int responseCode;
    private String responseData;
    private String TAG;
    private String responseError = null;

    //JSON Parsing
    private boolean isJSONObject = false;
    private boolean isJSONArray = false;
    private JSONObject jsonObject = null;
    private JSONArray jsonArray = null;

    public GetApiResponse(String url, String tag) {
        TAG = tag;
        responseCode = -1;
        try {

            //Establishing the connection
            URL blogFeedUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) blogFeedUrl.openConnection();
            connection.connect();

            // Getting the response code
            responseCode = connection.getResponseCode();
            setResponseError();

            // Setting JSON Response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Reader reader = new InputStreamReader(inputStream);
                int contentLength = connection.getContentLength();
                char[] charArray = new char[contentLength];
                reader.read(charArray);
                responseData = new String(charArray);
                checkJsonResponse();
            }
            Log.i(TAG, "Code: " + responseCode);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Exception Caught: ", e);
        } catch (IOException e) {
            Log.e(TAG, "Exception Caught: ", e);
        } catch (Exception e) {
            Log.e(TAG, "Exception Caught: ", e);
        }
    }

    private void setResponseError() {
        if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) { //Response = 500
            responseError = "Internal Server Error";
        } else if (responseCode == HttpURLConnection.HTTP_BAD_GATEWAY) { //Response = 502
            responseError = "Bad Gateway";
        } else if (responseCode == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) { //Response = 504
            responseError = "Gateway Timeout";
        } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) { //Response = 400
            responseError = "Bad Request";
        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) { //Response = 401
            responseError = "Unauthorized Request";
        } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) { //Response = 403
            responseError = "Forbidden Request";
        } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) { //Response = 404
            responseError = "Unfortunately! Not Found";
        } else if (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) { //Response = 408
            responseError = "Client Timeout";
        } else if (responseCode == HttpURLConnection.HTTP_REQ_TOO_LONG) { //Response = 414
            responseError = "Request is too long";
        } else if (responseCode == HttpURLConnection.HTTP_UNSUPPORTED_TYPE) { //Response = 415
            responseError = "Unsupported request type";
        } else {
            responseError = null;
        }

    }

    public String getResponseData(){
        return responseData;
    }

    public String getResponseError(){
        return responseError;
    }

    public int getResponseCode(){
        return responseCode;
    }


    //JSON Parsing Methods
    private void checkJsonResponse() {
        String trimmedResponseData = responseData.trim();
        String firstCharacterCheck = trimmedResponseData.substring(0,1);
        try {
            if(firstCharacterCheck == "{") {
                isJSONObject = true;
                jsonObject = new JSONObject(responseData);
            } else if (firstCharacterCheck == "[") {
                isJSONArray = true;
                jsonArray = new JSONArray(responseData);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Exception Caught: ", e);
        } catch (Exception e) {
            Log.e(TAG, "Exception Caught: ", e);
        }
    }

    public JSONObject getELementByID(String id) {
        try {
            if (isJSONArray) {
                int dataLength = jsonArray.length();
                for (int i = 0; i < dataLength; i++) {
                    JSONObject element = jsonArray.getJSONObject(i);
                    if (element.getString("id") == id) {
                        return element;
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Exception Caught: ", e);
        } catch (Exception e) {
            Log.e(TAG, "Exception Caught: ", e);
        }
        return null;
    }

    public JSONObject getJsonObject(){
        return jsonObject;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }
}
