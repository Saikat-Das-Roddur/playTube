package com.saikat.playtube.Others;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface GoogleEventListener {
    void onSignInSuccess(GoogleSignInAccount account);
    void onFailureSignIn(String error);
    void onSignOutSuccess(Class activityClass);
    void onSignOutFailure(Class activityClass);



}
