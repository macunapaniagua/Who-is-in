package com.soccer.whosin.fragments.join_code;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soccer.whosin.R;
import com.soccer.whosin.utils.LocalStorageHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinCodeFragment extends Fragment {


    public JoinCodeFragment() {
        // Required empty public constructor
    }

    public static JoinCodeFragment newInstance() {
        return new JoinCodeFragment();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view          = inflater.inflate(R.layout.fragment_join_code, container, false);
        String code        = LocalStorageHelper.getGroupInvitationCode(getContext());
        TextView codeField = (TextView) view.findViewById(R.id.invitation_code);
        codeField.setText(code);
        return view;
    }

}
