package io.alstonlin.pennapps_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class DAO {
    public static final String SERVER_URL = "insert some path here";
    private static DAO instance;
    private SharedPreferences mPreferences;
    private Context context;


    protected JSONObject doInBackground(String... urls) {
        URL url = null;
        try {
            url = new URL(SERVER_URL);
        }
        catch (MalformedURLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        try {
        HttpURLConnection client = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        // HttpPost post = new HttpPost(urls[0]);
        JSONObject holder = new JSONObject();
        JSONObject userObj = new JSONObject();
        String response = null;
        JSONObject json = new JSONObject();
        return null;
    }

    private DAO(){
        // TODO: something
    }
    public static DAO getInstance(){
        if (instance == null) instance = new DAO();
        return instance;
    }
    // TODO: Add public non-static methods to use as interface
    // TODO: Add private non-static methods to externalize code used for all HTTP actions

    protected void onPostExecute(JSONObject json) {
        try {
            if (json.getBoolean("success")) {
                // everything is ok
                SharedPreferences.Editor editor = mPreferences.edit();
                // save the returned auth_token into
                // the SharedPreferences
                editor.putString("AuthToken", json.getJSONObject("data").getString("auth_token"));
                editor.commit();

                // launch the HomeActivity and close this one
                //Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                //startActivity(intent);
                //finish();
            }
            Toast.makeText(context, json.getString("info"), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // something went wrong: show a Toast
            // with the exception message
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            //super.onPostExecute(json);
        }
    }
}
