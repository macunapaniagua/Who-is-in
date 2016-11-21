package com.soccer.whosin.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soccer.whosin.R;
import com.soccer.whosin.interfaces.OnListItemEventsListener;
import com.soccer.whosin.models.MatchRow;

import java.util.List;

/**
 * Created by Mario A on 20/11/16.
 **/

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {

    private List<MatchRow> mMatchRows;
    private final OnListItemEventsListener mItemEventsListener;

    public MatchesAdapter(List<MatchRow> pMatchRows, OnListItemEventsListener pItemClickListener) {
        mMatchRows = pMatchRows;
        mItemEventsListener = pItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mMatchRows.size();
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_item, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MatchViewHolder holder, int position) {
        final MatchRow matchRow = mMatchRows.get(position);
        int status = matchRow.isConfirmed() ? R.drawable.ic_approve_member
                                         : R.drawable.ic_remove_member;
        holder.vStatus.setImageResource(status);
        holder.vSoccerField.setText(matchRow.getSoccerField());
        holder.vDateTime.setText(matchRow.getDateTime());
        holder.vConfirmations.setText(matchRow.getConfirmations());
        holder.vRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemEventsListener.onItemClicked(view, matchRow.getId());
            }
        });
        holder.vRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mItemEventsListener.onItemLongClick(view, matchRow.getId());
                return true;
            }
        });
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {
        public final View vRow;
        public final ImageView vStatus;
        public final TextView vSoccerField, vDateTime, vConfirmations;

        public MatchViewHolder(View view) {
            super(view);
            vRow           = view;
            vStatus        = (ImageView) view.findViewById(R.id.match_confirmation_status);
            vDateTime      = (TextView) view.findViewById(R.id.match_datetime);
            vSoccerField   = (TextView) view.findViewById(R.id.match_soccer_field);
            vConfirmations = (TextView) view.findViewById(R.id.match_confirmations);
        }
    }
}
