package io.alstonlin.pennapps_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void clickSignUp(View v){
        // TODO: Add the Signup stuff
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clickCancel(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
