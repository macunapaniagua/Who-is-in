package com.soccer.whosin.interfaces;

import android.view.View;

/**
 * Created by Mario A on 20/11/16.
 **/
public interface OnListItemEventsListener {
    void onItemClicked(View view, String id);
    void onItemLongClick(View view, String id);
}
