package io.alstonlin.pennapps_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
