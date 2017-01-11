package com.tc2r.facebookbasics;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private final static String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize FB SDK
        // THIS LINE MUST BE PUT HERE BEFORE THE SETCONTENTVIEW
        // OR ELSE YOU WILL GET NULL OBJECT ERRORS!
        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {

            @Override
            public void onInitialized() {
                // Accesstoken is for us to check wheater we have previously logged into this app
                // and this information is saved in Shared Preference and sets it during SDK Inital

                accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken == null) {
                    Log.d(TAG, "Not logged in yet");

                } else {
                    Log.d(TAG, "Logged In");
                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(main);
                }

            }
        });

        setContentView(R.layout.activity_login);

        // Register a callback to respond to a login result;
        callbackManager = CallbackManager.Factory.create();

        // Register access token to check wheater user logged in before
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
            }
        };

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        // Callback Registration

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Once authorized from facebook, will directly go to MainActivity
                accessToken = loginResult.getAccessToken();
                Intent main = new Intent(LoginActivity.this, MainActivity.class);

                startActivity(main);
            }

            @Override
            public void onCancel() {
                // APP CODE
            }

            @Override
            public void onError(FacebookException error) {

                // APP CODE
                Log.i("Error" , "Error");

            }
        });

        // Set permissions to use in this app
        List<String> permissionNeeds = Arrays.asList("public_profile", "user_friends", "email", "user_birthday");
        loginButton.setReadPermissions(permissionNeeds);

        accessTokenTracker.startTracking();
        // Generate Hash Key, need get this key update
        // into https://developers.facebook.com/quickstarts/1584671128490867/?platform=android

        try {
            showHashKey(this);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static void showHashKey(Context context) throws NoSuchAlgorithmException {

        try {
            //Your package name here
            PackageInfo info = context.getPackageManager().getPackageInfo("com.tc2r.facebookbasics", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Facebook Login
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}