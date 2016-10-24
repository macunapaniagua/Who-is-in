package com.soccer.whosin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.soccer.whosin.R;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private LoginButton vLogin;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        this.loadViews();
        this.setReadPermissions();
        this.registerFacebookCallback();
    }

    protected void loadViews() {
        vLogin = (LoginButton) this.findViewById(R.id.login_button);
    }

    protected void setReadPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        vLogin.setReadPermissions(permissions);
    }

    protected void registerFacebookCallback() {
        callbackManager = CallbackManager.Factory.create();
        vLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // LoginResult tiene la nueva clase AccessToken, así como los permisos concedidos o rechazados más recientemente.
//                Profile.getCurrentProfile()

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
