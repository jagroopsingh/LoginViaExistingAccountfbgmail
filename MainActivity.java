package com.apps.gill.loginviaexistingaccount.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.apps.gill.loginviaexistingaccount.R;
import com.apps.gill.loginviaexistingaccount.utils.CommonData;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    LoginButton loginButton;
    String firstName, lastName, picUrl, email, gender;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    Intent intent;
    Button twitAndLinkedin, btSignUp, btToolbar;

    public void init() {
        twitAndLinkedin = (Button) findViewById(R.id.loginusing_twit_linkedin);
        btSignUp = (Button) findViewById(R.id.bt_signup);
        btToolbar = (Button) findViewById(R.id.bt_back);
        btToolbar.setVisibility(View.GONE);
    }

    FacebookCallback facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.v("TAG", "Callbacksuccess");
            Profile profile = Profile.getCurrentProfile();
            firstName = profile.getFirstName();
            lastName = profile.getLastName();
            picUrl = profile.getProfilePictureUri(150, 150) + "";
            Log.v("TAG", firstName);
            Log.v("TAG", lastName);
            GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.v("TAG", response.toString());
                    try {
                        email = object.getString("email");
                        gender = object.getString("gender");
                        Log.v("Tag", email);
                        Log.v("Tag", gender);
                        intent = new Intent(MainActivity.this, RegFirstStage.class);
                        intent.setAction("facebook");
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("lastName", lastName);
                        intent.putExtra("email", email);
                        intent.putExtra("gender", gender);
                        intent.putExtra("picUrl", picUrl);
                        startActivity(intent);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.v("Tag", "inside catch");
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "email,gender");
            graphRequest.setParameters(parameters);
            graphRequest.executeAsync();
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {
            Log.v("TAG", String.valueOf(error));
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        if (CommonData.getUserDetails(getApplicationContext()) != null) {
            intent = new Intent(getApplicationContext(), RegistrationComplete.class);
            startActivity(intent);
            finish();
        }
        init();
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, facebookCallback);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            }
        };
        accessTokenTracker.startTracking();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                profileTracker.stopTracking();
            }
        };
        profileTracker.startTracking();
        googleSignIn();
        twitAndLinkedin.setOnClickListener(this);
        btSignUp.setOnClickListener(this);
    }

    private void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.gmail_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(this);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.v("TAG", "handleSignInResult:" + result.isSuccess());
        GoogleSignInAccount acct = result.getSignInAccount();
        String email = acct.getEmail();
        Log.v("TAG", email);
        picUrl = String.valueOf(acct.getPhotoUrl());
        Log.v("TAG", picUrl);
        intent = new Intent(this, RegFirstStage.class);
        intent.setAction("gmail");
        intent.putExtra("Email", email);
        intent.putExtra("picUrl", picUrl);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginusing_twit_linkedin:
                intent = new Intent(MainActivity.this, TwitAndLinkedIn.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bt_signup:
                CommonData.clearAllAppData(MainActivity.this);
                intent = new Intent(MainActivity.this, RegFirstStage.class);
                intent.setAction("signup");
                startActivity(intent);
                finish();
                break;
            case R.id.gmail_button:
                signIn();
                break;
        }
    }
}
