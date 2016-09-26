package com.soccer.whosin.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soccer.whosin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceholderFragment extends Fragment {

    public static final String ARG_SECTION_TITLE = "section_number";

    public PlaceholderFragment() {
        // Required empty public constructor
    }

    /**
     * Crea una instancia prefabricada de {@link PlaceholderFragment}
     *
     * @param sectionTitle Título usado en el contenido
     * @return Instancia dle fragmento
     */
    public static PlaceholderFragment newInstance(String sectionTitle) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.section_fragment, container, false);
        // Ubicar argumento en el text view de section_fragment.xml
        String title = getArguments().getString(ARG_SECTION_TITLE);
        TextView vTitle = (TextView) view.findViewById(R.id.title);
        vTitle.setText(title);
        return view;
    }

}
