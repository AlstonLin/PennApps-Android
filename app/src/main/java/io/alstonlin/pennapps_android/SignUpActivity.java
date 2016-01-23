package io.alstonlin.pennapps_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void clickSignUp(View v){
        String name = ((EditText)findViewById(R.id.name)).getText().toString();
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        String confirm = ((EditText)findViewById(R.id.confirm)).getText().toString();
        if (password.equals(confirm)){
            try {
                DAO.getInstance().signUp(name, email, password, new JSONRunnable() {
                    @Override
                    public void run(JSONObject json) {
                        try {
                            if (json.has("success")){
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            Toast.makeText(SignUpActivity.this, json.getString("response"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "There was an error while Signing Up", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "The password and confirmation do match!", Toast.LENGTH_LONG).show();
        }
    }

    public void clickCancel(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
