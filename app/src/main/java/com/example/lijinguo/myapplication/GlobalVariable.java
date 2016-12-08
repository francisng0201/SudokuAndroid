package com.example.lijinguo.myapplication;
import android.app.Application;
//import com.google.android.gms.common.api.GoogleApiClient;
import android.content.Context;
/**
 * Created by lijinguo on 11/9/16.
 */

public class GlobalVariable extends Application {

    private static String username;
    private static String password;

    public static String getSessionUsername(){
        return username;
    }
    public static void setSessionUsername(String name){
        GlobalVariable.username = name;
    }

//    private GoogleApiClient mGoogleApiClient;

    private static Context context;

    public void onCreate() {
        super.onCreate();
        GlobalVariable.context = getApplicationContext();
        setSessionUsername("");
    }

    public static Context getAppContext() {
        return GlobalVariable.context;
    }

//    public GoogleApiClient getClient() {
//        return mGoogleApiClient;
//    }
//
//    public void setClient(GoogleApiClient mGoogleApiClient) {
//        this.mGoogleApiClient = mGoogleApiClient;
//    }
}