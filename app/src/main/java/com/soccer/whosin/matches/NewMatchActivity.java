package com.soccer.whosin.matches;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.philliphsu.bottomsheetpickers.date.BottomSheetDatePickerDialog;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.philliphsu.bottomsheetpickers.time.BottomSheetTimePickerDialog;
import com.philliphsu.bottomsheetpickers.time.grid.GridTimePickerDialog;
import com.soccer.whosin.R;
import com.soccer.whosin.adapters.FieldsAdapter;
import com.soccer.whosin.fragments.fields.FieldsPresenter;
import com.soccer.whosin.fragments.matches.MatchesPresenter;
import com.soccer.whosin.interfaces.PermissionsCallback;
import com.soccer.whosin.models.CreateMatch;
import com.soccer.whosin.models.ErrorMessage;
import com.soccer.whosin.models.GroupMember;
import com.soccer.whosin.models.Match;
import com.soccer.whosin.models.SoccerField;
import com.soccer.whosin.utils.AndroidPermissionsHelper;
import com.soccer.whosin.utils.BusProvider;
import com.soccer.whosin.utils.Constants;
import com.soccer.whosin.utils.LocalStorageHelper;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

public class NewMatchActivity extends AppCompatActivity implements View.OnClickListener,
        BottomSheetTimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener,
        AdapterView.OnItemSelectedListener, PermissionsCallback {

    private Spinner vSoccerFields;
    private RelativeLayout vLoadingIndicator;
    private LinearLayout vFieldDetails;
    private ImageView vCall;
    private TextView vFieldCapacity, vFieldPrice, vFieldPhone;
    private EditText vDate, vTime, vPlayersLimit;
    private Button vCreateMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);
        this.setToolbar();
        this.loadViews();
        this.loadSoccerFields();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadViews() {
        vLoadingIndicator = (RelativeLayout) this.findViewById(R.id.new_match_loading);
        vSoccerFields = (Spinner) this.findViewById(R.id.new_match_fields);
        vFieldDetails = (LinearLayout) this.findViewById(R.id.new_match_field_details);
        vFieldCapacity = (TextView) this.findViewById(R.id.new_match_field_capacity);
        vFieldPrice = (TextView) this.findViewById(R.id.new_match_field_price);
        vFieldPhone = (TextView) this.findViewById(R.id.new_match_field_phone_number);
        vCall = (ImageView) this.findViewById(R.id.new_match_field_phone_call);
        vDate = (EditText) this.findViewById(R.id.new_match_date);
        vTime = (EditText) this.findViewById(R.id.new_match_time);
        vPlayersLimit = (EditText) this.findViewById(R.id.new_match_players_limit);
        vCreateMatch = (Button) this.findViewById(R.id.new_match_create);
    }

    private void setListeners() {
        vDate.setOnClickListener(this);
        vTime.setOnClickListener(this);
        vCall.setOnClickListener(this);
        vCreateMatch.setOnClickListener(this);
        vSoccerFields.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == vDate)
            this.loadCalendarPicker();
        else if (view == vTime)
            this.loadTimePicker();
        else if (view == vCreateMatch)
            this.createMatch();
        else if (view == vCall)
            AndroidPermissionsHelper.requestPermissions(this, Constants.CALL_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndroidPermissionsHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onAndroidPermissionsGranted(int requestCode) {
        if (requestCode == Constants.CALL_PERMISSION_REQUEST_CODE)
            this.callSoccerField();
    }

    protected void callSoccerField() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            String phoneNumber = vFieldPhone.getText().toString();
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            this.startActivity(intent);
        }
    }

    protected void loadCalendarPicker() {
        Calendar calendar = Calendar.getInstance();
        BottomSheetDatePickerDialog datePicker = BottomSheetDatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show(getSupportFragmentManager(), this.getApplication().getPackageName());
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = new java.util.GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
       // vDate.setText(DateFormat.getDateFormat(this).format(cal.getTime()));
        vDate.setText(DateFormat.format("dd-MM-yyyy", cal.getTime()));
    }

    protected void loadTimePicker() {
        Calendar calendar = Calendar.getInstance();
        GridTimePickerDialog timePicker = GridTimePickerDialog.newInstance(this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this));
        timePicker.show(getSupportFragmentManager(), this.getApplication().getPackageName());
    }

    @Override
    public void onTimeSet(ViewGroup viewGroup, int hourOfDay, int minute) {
        Calendar cal = new java.util.GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        vTime.setText(DateFormat.getTimeFormat(this).format(cal.getTime()));
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getBus().unregister(this);
    }

    private void loadSoccerFields() {
        this.showLoadingIndicator();
        // Get the Params to request and filter the list of Users
        GroupMember groupMember = LocalStorageHelper.getGroupMember(this);
        String groupId = groupMember.getGroup().getGroupId();
        String facebookId = groupMember.getMember().getFacebookId();
        // Start the Members request
        FieldsPresenter presenter = new FieldsPresenter();
        presenter.getSoccerFields(facebookId, groupId);
    }

    protected void createMatch() {
        if (vSoccerFields.getSelectedItemPosition() > 0) {
            String time         = vTime.getText().toString().trim();
            String date         = vDate.getText().toString().trim();
            SoccerField field   = (SoccerField) vSoccerFields.getSelectedItem();
            String playersLimit = vPlayersLimit.getText().toString().trim();
            if (!time.isEmpty() && !date.isEmpty() && !playersLimit.isEmpty() && field != null) {
                String fieldId     = field.getId();
                GroupMember member = LocalStorageHelper.getGroupMember(this);
                String groupId     = member.getGroup().getGroupId();
                String facebookId  = member.getMember().getFacebookId();
                CreateMatch match  = new CreateMatch(fieldId, groupId, time, date, playersLimit);
                MatchesPresenter presenter = new MatchesPresenter();
                this.showLoadingIndicator();
                presenter.createMatch(facebookId, match);
                return;
            }
        }
        Toast.makeText(this, this.getString(R.string.all_fields_required), Toast.LENGTH_LONG).show();
    }

    protected void showLoadingIndicator() {
        vLoadingIndicator.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingIndicator() {
        vLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onSoccerFieldsRequestSuccessful(ArrayList<SoccerField> pSoccerFields) {
        SoccerField defaultOption = new SoccerField(this.getString(R.string.select_soccer_field));
        pSoccerFields.add(0, defaultOption);
        vSoccerFields.setAdapter(new FieldsAdapter(this, pSoccerFields));
        this.setListeners();
        this.hideLoadingIndicator();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onSoccerMatchCreated(Match pSoccerMatch) {
        this.hideLoadingIndicator();
        this.setResult(RESULT_OK);
        this.finish();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onRequestFailed(ErrorMessage pErrorMessage) {
        this.hideLoadingIndicator();
        Toast.makeText(this, pErrorMessage.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {
            SoccerField soccerField = (SoccerField) adapterView.getItemAtPosition(i);
            vFieldPrice.setText(String.valueOf(soccerField.getPrice()));
            vFieldCapacity.setText(this.getString(R.string.players, soccerField.getPlayerCount()));
            vFieldDetails.setVisibility(View.VISIBLE);
            if (soccerField.getPhone() != null && !soccerField.getPhone().trim().isEmpty()) {
                vFieldPhone.setText(soccerField.getPhone().trim());
                vCall.setVisibility(View.VISIBLE);
            } else
                vCall.setVisibility(View.GONE);
        } else
            vFieldDetails.setVisibility(View.GONE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        vFieldDetails.setVisibility(View.GONE);
    }
}
