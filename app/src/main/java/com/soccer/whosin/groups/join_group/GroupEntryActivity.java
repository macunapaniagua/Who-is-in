package com.soccer.whosin.groups.join_group;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soccer.whosin.R;
import com.soccer.whosin.groups.GroupPresenter;
import com.soccer.whosin.groups.create_group.CreateGroupActivity;
import com.soccer.whosin.main_content.MainActivity;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.utils.BusProvider;
import com.soccer.whosin.utils.LocalStorageHelper;
import com.squareup.otto.Subscribe;

public class GroupEntryActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText vGroupCode;
    private Button vJoinGroup;
    private TextView vCreateGroup;
    private RelativeLayout vLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_entry);
        this.loadViews();
        this.setListeners();
    }

    private void loadViews() {
        this.vGroupCode        = (EditText) this.findViewById(R.id.group_entry_code);
        this.vJoinGroup        = (Button) this.findViewById(R.id.group_entry_join);
        this.vCreateGroup      = (TextView) this.findViewById(R.id.group_entry_new);
        this.vLoadingIndicator = (RelativeLayout) this.findViewById(R.id.group_entry_loading);
        vGroupCode.setPaintFlags(vGroupCode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setListeners() {
        vJoinGroup.setOnClickListener(this);
        vCreateGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == vJoinGroup)
            this.joinGroup();
        else if (v == vCreateGroup)
            this.goToCreateGroup();
    }

    private void joinGroup() {
        if (vGroupCode.getText().toString().trim().equals(""))
            Toast.makeText(this, R.string.group_code_required, Toast.LENGTH_LONG).show();
        else {
            this.showLoadingIndicator();
            String groupCode = vGroupCode.getText().toString().trim();
            String facebookId = LocalStorageHelper.getUserFacebookId(this);
            GroupPresenter presenter = new GroupPresenter();
            presenter.joinGroup(facebookId, groupCode);
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
    public void onJoinGroupSuccessfully(GroupMember pGroupMember) {
        LocalStorageHelper.storeGroupMember(this, pGroupMember);
        this.hideLoadingIndicator();
        this.goToMainScreen();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onJoinGroupFailed(ErrorMessage pErrorMessage) {
        this.hideLoadingIndicator();
        Toast.makeText(this, pErrorMessage.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void goToCreateGroup() {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        this.startActivity(intent);
    }

    protected void goToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
    }
}
