package com.soccer.whosin.groups.create_group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.soccer.whosin.R;
import com.soccer.whosin.groups.GroupPresenter;
import com.soccer.whosin.main_content.MainActivity;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.utils.BusProvider;
import com.soccer.whosin.utils.LocalStorageHelper;
import com.squareup.otto.Subscribe;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText vGroupName;
    private Button vCreateGroup;
    private RelativeLayout vLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        this.loadViews();
        this.setListeners();
    }

    private void loadViews() {
        vGroupName   = (EditText) this.findViewById(R.id.create_group_name);
        vCreateGroup = (Button) this.findViewById(R.id.create_group_create);
        vLoadingIndicator = (RelativeLayout) this.findViewById(R.id.create_group_loading);
    }

    private void setListeners() {
        vCreateGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == vCreateGroup)
            this.createGroup();
    }

    protected void createGroup() {
        if (vGroupName.getText().toString().trim().equals(""))
            Toast.makeText(this, R.string.group_name_required, Toast.LENGTH_LONG).show();
        else {
            this.showLoadingIndicator();
            String groupName = vGroupName.getText().toString().trim();
            String facebookId = LocalStorageHelper.getUserFacebookId(this);
            GroupPresenter presenter = new GroupPresenter();
            presenter.createGroup(facebookId, groupName);
        }
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

    @Subscribe
    @SuppressWarnings("unused")
    public void onCreateGroupSuccessfully(GroupMember pGroupMember) {
        LocalStorageHelper.storeGroupMember(this, pGroupMember);
        this.hideLoadingIndicator();
        this.goToMainScreen();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onCreateGroupFailed(ErrorMessage pErrorMessage) {
        this.hideLoadingIndicator();
        Toast.makeText(this, pErrorMessage.getMessage(), Toast.LENGTH_LONG).show();
    }

    protected void goToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
    }
}
