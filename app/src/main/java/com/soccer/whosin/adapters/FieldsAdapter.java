package com.soccer.whosin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soccer.whosin.models.SoccerField;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Mario A on 25/11/16.
 **/

public class FieldsAdapter extends BaseAdapter {

    private Context mContext;
    private List<SoccerField> mSoccerFields;

    public FieldsAdapter(Context pContext, List<SoccerField> pSoccerFields) {
        this.mContext      = pContext;
        this.mSoccerFields = pSoccerFields;
    }

    @Override
    public int getCount() {
        return mSoccerFields.size();
    }

    @Override
    public Object getItem(int i) {
        return this.mSoccerFields.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_spinner_dropdown_item, null);
        if (view instanceof TextView)
            ((TextView) view).setText(mSoccerFields.get(i).getName());
        return view;
    }
}
