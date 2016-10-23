package com.soccer.whosin.activities;

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

import com.soccer.whosin.R;
import com.soccer.whosin.fragments.MembersFragment;
import com.soccer.whosin.fragments.PlaceholderFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout vDrawerLayout;
    private NavigationView vNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setToolbar();
        this.initializeDrawer();
        // Set home item as default
        if (savedInstanceState == null && vNavigationView != null) {
            vNavigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu);
        }
    }

    private void initializeDrawer() {
        vDrawerLayout   = (DrawerLayout) findViewById(R.id.drawer_layout);
        vNavigationView = (NavigationView) findViewById(R.id.nav_view);
        this.setListeners();
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
        if (!vDrawerLayout.isDrawerOpen(GravityCompat.START)) {
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
}
