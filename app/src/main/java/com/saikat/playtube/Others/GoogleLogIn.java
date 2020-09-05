package com.saikat.playtube.Others;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.saikat.playtube.Config;
import com.saikat.playtube.R;

import java.io.IOException;


public class GoogleLogIn {
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1001;
    private Activity activity;
    private GoogleEventListener listener;
    private GoogleSignInAccount googleSignInAccount;

    public void setListener(GoogleEventListener listener) {
        this.listener = listener;
    }

    private String TAG = getClass().getSimpleName();

    public GoogleLogIn(Activity activity) {
        this.activity = activity;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Config.youTubeScopeUrl))
                .requestServerAuthCode(activity.getString(R.string.server_client_id))
                .requestEmail()

                .build();
        mGoogleSignInClient = GoogleSignIn.getClient((Context) this.activity, gso);

    }


    public void signIn() {

        //if (!isSignedIn()) {
            activity.startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
       //}


    }


    public void onSignInActivityResult(Intent intent) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        //if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                String authCode = account.getServerAuthCode();
                GoogleTokenResponse tokenResponse =
                        new GoogleAuthorizationCodeTokenRequest(
                                new NetHttpTransport(),
                                JacksonFactory.getDefaultInstance(),
                                Config.tokenUrl,
                                activity.getString(R.string.server_client_id),
                                activity.getString(R.string.client_secret),
                                authCode,
                                "") // Specify the same redirect URI that you use with your web
                                // app. If you don't have a web version of your app, you can
                                // specify an empty string.
                                .execute();
                Config.accessToken = tokenResponse.getAccessToken();
                Log.d(TAG, "AccessToken: " + tokenResponse);
                Config.refreshToken = tokenResponse.getRefreshToken();
                Log.d(TAG, "Authcode: " + authCode);
                Log.d(TAG, "onSignedIn: Data send");
                Toast.makeText(activity, "Successful" + mGoogleSignInClient, Toast.LENGTH_SHORT).show();
                listener.onSignInSuccess(account);
            } catch (ApiException e) {

                listener.onFailureSignIn(e.getMessage());
                Log.w(TAG, "Google sign in failed", e);
                // ...
            } catch (IOException e) {
                e.printStackTrace();
            }
        //}


    }


    private boolean isSignedIn() {
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(activity);
        if (googleSignInAccount != null) {
            signOut();
            return true;
        } else {
            return false;
        }

    }




    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               signIn();
            }
        });

    }


}
