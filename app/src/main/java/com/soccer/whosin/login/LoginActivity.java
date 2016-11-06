package com.soccer.whosin.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.soccer.whosin.R;
import com.soccer.whosin.groups.join_group.GroupEntryActivity;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.utils.BusProvider;
import com.soccer.whosin.utils.LocalStorageHelper;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private LoginButton vLogin;
    private RelativeLayout vLoadingIndicator;
    private CallbackManager mCallbackManager;
    private ProfileTracker mProfileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        if (Profile.getCurrentProfile() != null) {
            LocalStorageHelper.storeLoggedUser(this, Profile.getCurrentProfile());
            this.goToGroupSection();
            return;
        }
        setContentView(R.layout.activity_login);
        this.loadViews();
        this.setReadPermissions();
        this.registerFacebookCallback();
    }

    protected void loadViews() {
        vLogin            = (LoginButton) this.findViewById(R.id.login_button);
        vLoadingIndicator = (RelativeLayout) this.findViewById(R.id.login_loading);
    }

    protected void setReadPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        vLogin.setReadPermissions(permissions);
    }

    protected void registerFacebookCallback() {
        mCallbackManager = CallbackManager.Factory.create();
        vLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            mProfileTracker.stopTracking();
                            LoginActivity.this.createUser();
                        }
                    };
                } else
                    LoginActivity.this.createUser();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onPostResume();
        BusProvider.getBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getBus().unregister(this);
    }

    protected void showLoadingIndicator() {
        vLoadingIndicator.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingIndicator() {
        vLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    protected void createUser() {
        Profile loggedUser = Profile.getCurrentProfile();
        if (loggedUser != null) {
            this.showLoadingIndicator();
            Member facebookMember = new Member(
                    loggedUser.getId(),
                    loggedUser.getName(),
                    loggedUser.getProfilePictureUri(200, 200).toString()
            );
            LoginPresenter presenter = new LoginPresenter();
            presenter.createUser(facebookMember);
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    protected void onUserCreated(Member pMember) {
        LocalStorageHelper.storeLoggedUser(this, pMember);
        this.hideLoadingIndicator();
        this.goToGroupSection();
    }

    @Subscribe
    @SuppressWarnings("unused")
    protected void onUserCreationFail(ErrorMessage pErrorMessage) {
        this.hideLoadingIndicator();
        Toast.makeText(this, pErrorMessage.getMessage(), Toast.LENGTH_LONG).show();
    }

    protected void goToGroupSection() {
        Intent intent = new Intent(this, GroupEntryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
    }
}
