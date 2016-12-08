package com.example.lijinguo.myapplication;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.EditText;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
////import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
//
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import android.widget.Toast;

/**
 * This controls the front page. Login/Signup, Google sign in features.
 */
//public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener {
public class MainActivity extends AppCompatActivity{

    int RC_SIGN_IN = 0;

    EditText edit_username;
    EditText edit_password;
    DataSource datasource;

//    private static GoogleApiClient mGoogleApiClient;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_main);


        // normal login

        edit_username   = (EditText)findViewById(R.id.username);
        edit_password   = (EditText)findViewById(R.id.password);


        datasource = new DataSource(this);
        datasource.open();


        Button signup = (Button) findViewById(R.id.signup);

        // Capture button clicks
        signup.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                String username = edit_username.getText().toString();
                String password = edit_password.getText().toString();
                datasource.createUser(username, password);
            }
        });


        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                String username = edit_username.getText().toString();
                String password = edit_password.getText().toString();

                boolean userExist = datasource.isUserExist(username, password);
                if(userExist){
                    GlobalVariable.setSessionUsername(username);
                    System.out.println("Successfully login");
                    datasource.close();
                    Intent profileIntent = new Intent(MainActivity.this,
                        profileActivity.class);
                    startActivity(profileIntent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "User not found",
                            Toast.LENGTH_LONG).show();
                    System.out.println("User not found");
                }
            }
        });

        // google sign in

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        // Build a GoogleApiClient with access to the Google Sign-In API and the
//        // options specified by gso.
//
//        mGoogleApiClient= new GoogleApiClient.Builder(this)
//                .enableAutoManage(this ,  this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//
//        ((GlobalVariable) this.getApplication()).setClient(mGoogleApiClient);
//
//        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
//        signInButton.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//
//                switch (v.getId()) {
//                    case R.id.sign_in_button:
//                        mGoogleApiClient.connect();
//                        signIn();
//
//                    // break;
//                }
//
//            }
//        });

        // Locate the button in activity_main.xml
        Button startGame = (Button) findViewById(R.id.MyButton);

        // Capture button clicks
        startGame.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // close database before moving on to next activity
                datasource.close();
                Intent myIntent = new Intent(MainActivity.this,
                        DifficultyActivity.class);
                startActivity(myIntent);
            }
        });

    }

//    private void signIn() {
//        GoogleApiClient mGoogleApiClient = ((GlobalVariable) this.getApplication()).getClient();
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }

//
//    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from
//        //   GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (result.isSuccess()) {
//                GoogleSignInAccount acct = result.getSignInAccount();
//                // Get account information
//                String mFullName = acct.getDisplayName();
//                String mEmail = acct.getEmail();
//                datasource.open();
//                GlobalVariable.setSessionUsername(mFullName);
//                boolean userExist = datasource.isUserExist(mFullName, mEmail);
//                if(userExist){
//                    datasource.close();
//                    System.out.println("Successfully login");
//                    Intent profileIntent = new Intent(MainActivity.this,
//                            profileActivity.class);
//                    startActivity(profileIntent);
//                }
//                else{
//                    datasource.createUser(mFullName, mEmail);
//                    datasource.close();
//                    Intent profileIntent = new Intent(MainActivity.this,
//                                profileActivity.class);
//                    startActivity(profileIntent);
//                }
//            }
//        }
//    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        System.out.println("Location services connected.");
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
////        mGoogleApiClient.connect();
//        System.out.println("Location services suspended. Please reconnect.");
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

    @Override
    protected void onStart() {
        super.onStart();
        datasource.open();
//        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
//        mGoogleApiClient.disconnect();
        datasource.close();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        datasource.open();
//        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        datasource.close();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
    }


//
//    private boolean mResolvingConnectionFailure = false;
//    private boolean mAutoStartSignInflow = true;
//    private boolean mSignInClicked = false;
//
//// ...
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        if (mResolvingConnectionFailure) {
//            // Already resolving
//            return;
//        }
//
//        // If the sign in button was clicked or if auto sign-in is enabled,
//        // launch the sign-in flow
//        if (mSignInClicked || mAutoStartSignInFlow) {
//            mAutoStartSignInFlow = false;
//            mSignInClicked = false;
//            mResolvingConnectionFailure = true;
//
//            // Attempt to resolve the connection failure using BaseGameUtils.
//            // The R.string.signin_other_error value should reference a generic
//            // error string in your strings.xml file, such as "There was
//            // an issue with sign in, please try again later."
//            if (!BaseGameUtils.resolveConnectionFailure(this,
//                    mGoogleApiClient, connectionResult,
//                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
//                mResolvingConnectionFailure = false;
//            }
//        }
//
//        // Put code here to display the sign-in button
//    }
//

}