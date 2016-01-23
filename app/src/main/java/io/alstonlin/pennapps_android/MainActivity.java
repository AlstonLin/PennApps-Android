package io.alstonlin.pennapps_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickSignUp(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void clickLogin(View v){
        JSONRunnable callback = new JSONRunnable() {
            @Override
            public void run(JSONObject json) {
                try {
                    if (json.getBoolean("res")) {
                        Intent intent = new Intent(MainActivity.this, AppActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, json.getString("response"), Toast.LENGTH_LONG).show();
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        };
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        DAO.getInstance().login(email, password, callback);
    }
}
