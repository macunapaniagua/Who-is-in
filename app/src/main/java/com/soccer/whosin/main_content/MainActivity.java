package com.soccer.whosin.main_content;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.soccer.whosin.R;
import com.soccer.whosin.fragments.PlaceholderFragment;
import com.soccer.whosin.fragments.fields.FieldsFragment;
import com.soccer.whosin.fragments.members.MembersFragment;
import com.soccer.whosin.groups.join_group.GroupEntryActivity;
import com.soccer.whosin.login.LoginActivity;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.utils.LocalStorageHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout vDrawerLayout;
    private NavigationView vNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setToolbar();
        this.subscribeNotifications();
        this.initializeDrawer();
        // Set home item as default
        if (savedInstanceState == null && vNavigationView != null) {
            this.selectItem(vNavigationView.getMenu().getItem(0));
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
    }

    protected void subscribeNotifications() {
        String topic = String.format("soccer_%1$s", LocalStorageHelper.getGroupId(this));
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    private void initializeDrawer() {
        vDrawerLayout   = (DrawerLayout) findViewById(R.id.drawer_layout);
        vNavigationView = (NavigationView) findViewById(R.id.nav_view);
        this.loadUserInfo();
        this.setListeners();
    }

    protected void loadUserInfo() {
        View header            = vNavigationView.getHeaderView(0);
        CircleImageView avatar = (CircleImageView) header.findViewById(R.id.drawer_avatar);
        TextView userName      = (TextView) header.findViewById(R.id.drawer_user_name);
        TextView groupAdmin    = (TextView) header.findViewById(R.id.drawer_is_admin);
        Member loggedUser      = LocalStorageHelper.getLoggedUser(this);
        userName.setText(loggedUser.getName());
        groupAdmin.setText(loggedUser.isAdmin() ? R.string.administrator : R.string.participant);
        Glide.with(this)
                .load(loggedUser.getAvatar())
                .into(avatar);
    }

    private void setListeners() {
        vNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        MainActivity.this.selectItem(menuItem);
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (vDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.navigation_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                vDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(MenuItem pMenuItem) {
        Fragment fragment;
        String title = pMenuItem.getTitle().toString();
        switch (pMenuItem.getItemId()) {
            case R.id.nav_members:
                fragment = MembersFragment.newInstance();
                break;
            case R.id.nav_fields:
                fragment = FieldsFragment.newInstance();
                break;
            case R.id.nav_log_out:
                this.logoutUser();
                return;
            case R.id.nav_change_group:
                this.changeGroupMember();
                return;
            default:
                fragment = PlaceholderFragment.newInstance(title);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.main_content, fragment)
                       .commit();
        vDrawerLayout.closeDrawers();
        setTitle(title);
    }

    private void logoutUser() {
        LoginManager.getInstance().logOut();
        LocalStorageHelper.removeUserData(this);
        this.goToLoginView();
    }

    protected void goToLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
    }

    private void changeGroupMember() {
        LocalStorageHelper.removeGroupMember(this);
        this.goToGroupSection();
    }

    protected void goToGroupSection() {
        Intent intent = new Intent(this, GroupEntryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
    }
}
