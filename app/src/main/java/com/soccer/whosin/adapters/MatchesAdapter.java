package com.soccer.whosin.adapters;

import android.content.Context;
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

    private Context mContext;
    private List<MatchRow> mMatchRows;
    private final OnListItemEventsListener mItemEventsListener;

    public MatchesAdapter(Context pContext, List<MatchRow> pMatchRows, OnListItemEventsListener pItemClickListener) {
        mContext            = pContext;
        mMatchRows          = pMatchRows;
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
        int status = matchRow.isConfirmed() ? R.drawable.ic_toggle_star
                                         : R.drawable.ic_toggle_star_outline;
        holder.vStatus.setImageResource(status);
        holder.vSoccerField.setText(mContext.getString(R.string.place_format, matchRow.getSoccerField()));
        holder.vDate.setText(mContext.getString(R.string.date_format, matchRow.getDate()));
        holder.vTime.setText(mContext.getString(R.string.time_format, matchRow.getTime()));
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
        public final TextView vSoccerField, vDate, vTime, vConfirmations;

        public MatchViewHolder(View view) {
            super(view);
            vRow           = view;
            vStatus        = (ImageView) view.findViewById(R.id.match_confirmation_status);
            vDate          = (TextView) view.findViewById(R.id.match_date);
            vTime          = (TextView) view.findViewById(R.id.match_time);
            vSoccerField   = (TextView) view.findViewById(R.id.match_soccer_field);
            vConfirmations = (TextView) view.findViewById(R.id.match_confirmations);
        }
    }
}
