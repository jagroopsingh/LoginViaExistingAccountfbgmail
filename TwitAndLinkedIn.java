package com.apps.gill.loginviaexistingaccount.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apps.gill.loginviaexistingaccount.R;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

public class TwitAndLinkedIn extends AppCompatActivity implements View.OnClickListener {
    Button btLinkedIn, btBack;
    String email, firstName, lastName, picUrl, msg, name, id;
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(email-address,firstName,lastName,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    Intent intent;
    private static final String TWITTER_KEY = "kc2FD0y32lpOH5HcLm21YOqih";
    private static final String TWITTER_SECRET = "Btmz1S3hKzDS5GoJaGwUQToXBX73t6gN4YGnI6Cy3Enak8vz7B";
    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_twit_and_linked_in);
        btLinkedIn = (Button) findViewById(R.id.bt_linkedin);
        btBack = (Button) findViewById(R.id.bt_back);
        btLinkedIn.setOnClickListener(this);
        btBack.setOnClickListener(this);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new LoginHandler());
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    private void loginLinkedIn() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                Log.v("TAG", "auth success");
                getUserData();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                Log.v("TAG", error.toString());
            }
        }, true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserData() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                try {
                    email = jsonObject.getString("emailAddress");
                    firstName = jsonObject.getString("firstName");
                    lastName = jsonObject.getString("lastName");
                    picUrl = jsonObject.getString("pictureUrl");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("TAG", email);
                intent = new Intent(TwitAndLinkedIn.this, RegFirstStage.class);
                intent.setAction("linkedin");
                intent.putExtra("firstName",firstName);
                intent.putExtra("lastName",lastName);
                intent.putExtra("email",email);
                intent.putExtra("picUrl",picUrl);
                startActivity(intent);
                finish();
            }

            @Override
            public void onApiError(LIApiError LIApiError) {
                Log.v("TAG", LIApiError.getLocalizedMessage());
            }
        });
    }

    private class LoginHandler extends Callback<TwitterSession> {
        @Override
        public void success(Result<TwitterSession> result) {
            TwitterSession session = result.data;
            name = session.getUserName();
            id = String.valueOf(session.getUserId());
            msg = "@" + name + " logged in! (#" + id + ")";
            Log.v("TAG", msg);
            final TwitterSession twitterSession = Twitter.getSessionManager().getActiveSession();
            Twitter.getApiClient(twitterSession).getAccountService().verifyCredentials(true, false, new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    picUrl = result.data.profileImageUrl;
                    Log.v("TAG", picUrl);
                    Log.v("TAG", name);
                    Log.v("TAG", String.valueOf(result.data.status));
                    intent = new Intent(TwitAndLinkedIn.this, RegFirstStage.class);
                    intent.setAction("twitter");
                    intent.putExtra("picUrl",picUrl);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void failure(TwitterException e) {

                }
            });
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void failure(TwitterException e) {
            Log.v("TwitterKit", "Login with Twitter failure", e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                intent = new Intent(TwitAndLinkedIn.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bt_linkedin:
                loginLinkedIn();
                break;
        }
    }
}
